package com.scu.highestpeak.child.fly_advice.service.FlightSpider;

import com.scu.highestpeak.child.fly_advice.domain.BO.Airport;
import com.scu.highestpeak.child.fly_advice.domain.BO.Flight;
import com.scu.highestpeak.child.fly_advice.domain.RVO.ExpediaSpiderRequestVO;
import com.scu.highestpeak.child.fly_advice.domain.RVO.FlightCrawl;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.function.Function;

/**
 * @author gtrong
 * @author highesteak
 */
public class ExpediaSpider extends AbstractCrawlTask {
    private static final String EXPEDIA_TARGET_URL = "https://www.expedia.com/flight/search/";
    private static DateTimeFormatter timeFormatter = DateTimeFormatter.ISO_DATE_TIME;
    private static final String SUPPLIER_NAME = "Expedia";

    public ExpediaSpider(Airport source, Airport destination, Date startDate) {
        super(source, destination, startDate);
    }

    @Override
    Request buildRequest() {
        MediaType mediaType = MediaType.parse("application/json");
        // 转换城市为三字码
        String dccode = this.getSource();
        String accode = this.getDestination();
        Date startDate = this.getStart();
        // add(new ExpediaSpiderRequestVO.SearchItem("CTU","PVG",new SimpleDateFormat("yyyy-MM-dd").parse
        //  ("2020-06-25")));

        ExpediaSpiderRequestVO expediaSpiderRequestVO = new ExpediaSpiderRequestVO(
                new ArrayList<ExpediaSpiderRequestVO.SearchItem>() {{
                    add(new ExpediaSpiderRequestVO.SearchItem(dccode, accode, startDate));
                }}
        );
        RequestBody body = RequestBody.create(mediaType, expediaSpiderRequestVO.toString());
        Request request = new Request.Builder()
                .url(EXPEDIA_TARGET_URL)
                .method("POST", body)
                .addHeader("Content-Type", "application/json")
                .addHeader("User-Agent", "iamman/7.25.0")
                .addHeader("Accept", "*/*")
                .addHeader("Connection", "keep-alive")
                .build();
        return request;
    }

    private Date parseDate(String jsonTime){
        TemporalAccessor accessor = timeFormatter.parse(jsonTime);
        Date date = Date.from(Instant.from(accessor));
        return date;
    }

    @Override
    synchronized List<Flight> parseDataToFlights(String jsonData) {
        List<Flight> flights = new ArrayList<>();
        JSONObject jsonObject = new JSONObject(jsonData);
        JSONObject jsonObject1 = jsonObject.getJSONObject("content").getJSONObject("legs");
        Iterator<String> stringIterator = jsonObject1.keys();
        while (stringIterator.hasNext()) {
            String key = stringIterator.next();
            JSONObject value = jsonObject1.getJSONObject(key);
            JSONObject carrier = value.getJSONArray("timeline").getJSONObject(0).getJSONObject("carrier");
            flights.add(new FlightCrawl(
                            carrier.getString("airlineName"),
                            carrier.getString("airlineCode") + carrier.getString("flightNumber"),
                            carrier.getString("plane"),
                            parseDate(value.getJSONObject("departureTime").getString("isoStr")),
                            parseDate(value.getJSONObject("arrivalTime").getString("isoStr")),
                            value.getJSONObject("price").getDouble("exactPrice")
                    ).addSupplier(SUPPLIER_NAME)
            );
        }
        return flights;
    }

    @Override
    String getSource() {
        return this.source.getIATACode();
    }

    @Override
    String getDestination() {
        return this.destination.getIATACode();
    }
}
