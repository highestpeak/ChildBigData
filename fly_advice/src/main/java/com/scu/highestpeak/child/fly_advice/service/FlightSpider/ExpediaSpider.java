package com.scu.highestpeak.child.fly_advice.service.FlightSpider;

import com.scu.highestpeak.child.fly_advice.domain.BO.Flight;
import com.scu.highestpeak.child.fly_advice.domain.RVO.ExpediaSpiderRequestVO;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 * @author gtrong
 * @author highesteak
 */
public class ExpediaSpider extends AbstractCrawlTask {
    private static final String EXPEDIA_TARGET_URL = "https://www.expedia.com/flight/search/";
    private static SimpleDateFormat formatResponseDate = new SimpleDateFormat("d/M/y");

    public ExpediaSpider(Date start, String source, String destination) {
        super(start, source, destination);
    }

    @Override
    Request buildRequest() {
        MediaType mediaType = MediaType.parse("application/json");
        // fixme: 转换城市为三字码
        String dccode = this.getSource();
        String accode = this.getDestination();
        Date startDate = this.getStart();
        // fixme:add(new ExpediaSpiderRequestVO.SearchItem("CTU","PVG",new SimpleDateFormat("yyyy-MM-dd").parse
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

    @Override
    List<Flight> parseDataToFlights(String jsonData) {
        int count = 0;
        List<Flight> flights = new ArrayList<Flight>();
        JSONObject jsonObject = new JSONObject(jsonData);
        JSONObject jsonObject1 = jsonObject.getJSONObject("content").getJSONObject("legs");
        Iterator<String> stringIterator = jsonObject1.keys();
        while (stringIterator.hasNext()) {
            count++;
            String key = stringIterator.next();
            JSONObject value = jsonObject1.getJSONObject(key);
            try {
                flights.add(new Flight(
                        value.getJSONArray("timeline").getJSONObject(0).getJSONObject("carrier").getString(
                                "airlineName"),
                        value.getJSONArray("timeline").getJSONObject(0).getJSONObject("carrier").getString(
                                "flightNumber"),
                        value.getJSONArray("timeline").getJSONObject(0).getJSONObject("carrier").getString("plane"),
                        formatResponseDate.parse(
                                value.getJSONObject("departureTime").getString("date")
                        ),
                        formatResponseDate.parse(
                                value.getJSONObject("arrivalTime").getString("date")),
                        value.getJSONObject("price").getDouble("exactPrice")
                ));
            } catch (ParseException e) {
                System.out.println("parse error");
            }
        }
        if (count == 0) {
            return null;
        }
        return flights;
    }
}
