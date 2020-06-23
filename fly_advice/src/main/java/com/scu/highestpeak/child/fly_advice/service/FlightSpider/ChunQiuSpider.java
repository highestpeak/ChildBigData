package com.scu.highestpeak.child.fly_advice.service.FlightSpider;

import com.scu.highestpeak.child.fly_advice.domain.BO.Airport;
import com.scu.highestpeak.child.fly_advice.domain.BO.Flight;
import com.scu.highestpeak.child.fly_advice.domain.RVO.ChunQiuSpiderRequestVO;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import org.json.JSONArray;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author gtrong
 * @author highesteak
 */
public class ChunQiuSpider extends AbstractCrawlTask {
    private static final String CHUN_QIU_TARGET_URL = "https://flights.ch.com/Flights/SearchByTime";
    private static SimpleDateFormat formatResponseDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

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
    List<Flight> parseDataToFlights(String jsonData) {
        JSONArray flightArray = new JSONObject(jsonData).getJSONArray("Route");
        List<Flight> flights = new ArrayList<>();
        for (int i = 0; i < flightArray.length(); i++) {
            JSONObject routeObject = flightArray.getJSONArray(i).getJSONObject(0);
            JSONArray cabinArray = routeObject.getJSONArray("AircraftCabins");
            for (int j = 0; j < cabinArray.length(); j++) {
                JSONObject currAircraftCabin = cabinArray.getJSONObject(j);
                JSONObject firstAircraftCabinInfo =
                        currAircraftCabin.getJSONArray("AircraftCabinInfos").getJSONObject(0);
                try {
                    flights.add(
                            new Flight(
                                    routeObject.getString("CompanyName"),
                                    routeObject.getString("No"),
                                    routeObject.getString("Type"),
                                    formatResponseDate.parse(routeObject.getString("DepartureTime")),
                                    formatResponseDate.parse(routeObject.getString("ArrivalTime"))
                            ).newPriceEntry(
                                    currAircraftCabin.get("CabinLevel").toString(),
                                    firstAircraftCabinInfo.getDouble("Price")
                            )
                    );
                } catch (ParseException e) {
                    System.out.println("parse error");
                }
            }
        }
        return flights;
    }
}
