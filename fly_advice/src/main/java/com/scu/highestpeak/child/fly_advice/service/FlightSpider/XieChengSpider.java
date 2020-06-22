package com.scu.highestpeak.child.fly_advice.service.FlightSpider;

import com.scu.highestpeak.child.fly_advice.domain.BO.Airport;
import com.scu.highestpeak.child.fly_advice.domain.BO.Flight;
import com.scu.highestpeak.child.fly_advice.domain.RVO.XieChengSpiderRequestVO;
import okhttp3.*;
import org.json.JSONArray;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author highestpeak
 */
public class XieChengSpider extends AbstractCrawlTask {
    private static final String XIE_CHENG_TARGET_URL = "https://m.ctrip.com/restapi/soa2/14022/flightListSearch";
    // private static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");


    public XieChengSpider(Airport source, Airport destination, Date startDate) {
        super(source, destination, startDate);
    }

    @Override
    Request buildRequest() {
        MediaType mediaType = MediaType.parse("application/json");
        String dccode = this.getSource();
        String accode = this.getDestination();
        Date startDate = this.getStart();
        XieChengSpiderRequestVO xieChengSpiderRequestVO = new XieChengSpiderRequestVO(
                new ArrayList<XieChengSpiderRequestVO.SearchItem>(){{
                    add(new XieChengSpiderRequestVO.SearchItem(dccode, accode, startDate));
                }}
        );
        RequestBody body = RequestBody.create(mediaType,xieChengSpiderRequestVO.toString());
        Request request = new Request.Builder()
                .url(XIE_CHENG_TARGET_URL)
                .method("POST", body)
                .addHeader("Content-Type", "application/json")
                .addHeader("Cookie", "JSESSIONID=261CD2681368BE00581F7480372CFFEF")
                .build();
        return request;
    }

    @Override
    List<Flight> parseDataToFlights(String jsonData) {
        JSONArray airportArray = new JSONObject(jsonData).getJSONArray("fltitem");
        List<Flight> flights = new ArrayList<>();
        for (int i = 0; i < airportArray.length(); i++) {
            JSONObject jsonObject = airportArray.getJSONObject(i);

            JSONObject firstMutilstn = jsonObject.getJSONArray("mutilstn").getJSONObject(0);
            JSONObject basinfo = firstMutilstn.getJSONObject("basinfo");
            JSONObject craftinfo = firstMutilstn.getJSONObject("craftinfo");
            String aircraftModel = craftinfo.getString("cname") +
                    craftinfo.getString("kind") +
                    craftinfo.getString("craft");
            JSONObject dateinfo = firstMutilstn.getJSONObject("dateinfo");
            JSONObject dportinfo = firstMutilstn.getJSONObject("dportinfo");
            JSONObject aportinfo = firstMutilstn.getJSONObject("aportinfo");

            JSONObject firstPolicyinfo = jsonObject.getJSONArray("policyinfo").getJSONObject(0);
            JSONObject firstAportinfo = firstPolicyinfo.getJSONArray("priceinfo").getJSONObject(0);

            try {
                flights.add(new Flight(
                        basinfo.getString("airsname"),
                        basinfo.getString("flgno"),
                        aircraftModel,
                        new SimpleDateFormat("yyyy-MM-dd").parse(dateinfo.getString("ddate")),
                        new SimpleDateFormat("yyyy-MM-dd").parse(dateinfo.getString("adate")),
                        firstAportinfo.getDouble("price")
                ));
            } catch (ParseException e) {
                System.out.println("parse error");
            }

        }
        return flights;
    }

    @Override
    Date getStart() {
        // return simpleDateFormat.parse("2020-06-30") start;
        return start;
    }

    @Override
    String getSource() {
        // "BJS"
        return source.getIATACode();
    }

    @Override
    String getDestination() {
        // "SHA"
        return destination.getIATACode();
    }
}
