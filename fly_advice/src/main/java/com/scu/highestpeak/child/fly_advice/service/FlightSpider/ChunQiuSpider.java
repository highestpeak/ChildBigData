package com.scu.highestpeak.child.fly_advice.service.FlightSpider;

import com.scu.highestpeak.child.fly_advice.domain.BO.Airport;
import com.scu.highestpeak.child.fly_advice.domain.BO.Flight;
import com.scu.highestpeak.child.fly_advice.domain.RVO.ChunQiuSpiderRequestVO;
import com.scu.highestpeak.child.fly_advice.domain.RVO.FlightCrawl;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import org.json.JSONArray;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author gtrong
 * @author highesteak
 */
public class ChunQiuSpider extends AbstractCrawlTask {
    private static final String CHUN_QIU_TARGET_URL = "https://flights.ch.com/Flights/SearchByTime";
    private static final String SUPPLIER_NAME = "春秋航空";
    private static final String FORMAT_DATE_STR = "yyyy-MM-dd HH:mm:ss";
    private static DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(FORMAT_DATE_STR);

    public ChunQiuSpider(Airport source, Airport destination, Date startDate) {
        super(source, destination, startDate);
    }

    @Override
    Request buildRequest() {
        MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
        Airport source = this.source;
        Airport destination = this.destination;
        Date startDate = this.getStart();
        ChunQiuSpiderRequestVO chunQiuSpiderRequestVO = new ChunQiuSpiderRequestVO()
                .setSourceCity(source.getCityName())
                .setArriveCity(destination.getCityName())
                .setSourceAirportCode(source.getIATACode())
                .setArriveAirportCode(destination.getIATACode())
                .setDtime(startDate);
        RequestBody body = RequestBody.create(mediaType, chunQiuSpiderRequestVO.toString());
        Request request = new Request.Builder()
                .url(CHUN_QIU_TARGET_URL)
                .method("POST", body)
                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                .addHeader("Cookie", "_os=bcb3411faf0021cf4bf8e0912f5260e8; " +
                        "acw_tc=2f624a2e15927406875016521e7adf4d151c650ede67e67677fdf55b64e53b")
                .build();
        return request;
    }

    @Override
    synchronized List<Flight> parseDataToFlights(String jsonData) {
        JSONArray flightArray = new JSONObject(jsonData).getJSONArray("Route");
        List<Flight> flights = new ArrayList<>();
        for (int i = 0; i < flightArray.length(); i++) {
            JSONObject routeObject = flightArray.getJSONArray(i).getJSONObject(0);
            JSONArray cabinArray = routeObject.getJSONArray("AircraftCabins");
            FlightCrawl flightCrawl = null;
            flightCrawl = new FlightCrawl(
                    routeObject.getString("CompanyName"),
                    routeObject.getString("No"),
                    routeObject.getString("Type"),
                    generateDate(routeObject.getString("DepartureTime")),
                    generateDate(routeObject.getString("ArrivalTime"))
            );
            if (flightCrawl == null) {
                continue;
            }
            for (int j = 0; j < cabinArray.length(); j++) {
                JSONObject currAircraftCabin = cabinArray.getJSONObject(j);
                JSONObject firstAircraftCabinInfo =
                        currAircraftCabin.getJSONArray("AircraftCabinInfos").getJSONObject(0);
                flightCrawl.newPriceEntry(
                        currAircraftCabin.get("CabinLevel").toString(),
                        firstAircraftCabinInfo.getDouble("Price")
                );

            }
            flights.add(flightCrawl.addSupplier(SUPPLIER_NAME));
        }
        return flights;
    }

    private Date generateDate(String toParse) {
        return Date.from(LocalDateTime.parse(toParse, dateTimeFormatter).atZone(ZoneId.systemDefault()).toInstant());
    }
}
