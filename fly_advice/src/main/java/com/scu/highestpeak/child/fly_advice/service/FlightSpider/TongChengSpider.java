package com.scu.highestpeak.child.fly_advice.service.FlightSpider;

import com.scu.highestpeak.child.fly_advice.domain.BO.Flight;
import com.scu.highestpeak.child.fly_advice.domain.RVO.TongChengSpiderRequestVO;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import org.json.JSONArray;
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
public class TongChengSpider extends AbstractCrawlTask {
    private static final String TONG_CHENG_TARGET_URL = "https://www.ly.com/flights/api/getflightlist";
    private static SimpleDateFormat formatResponseDate = new SimpleDateFormat("yyyy-MM-dd hh:mm");

    public TongChengSpider(Date start, String source, String destination) {
        super(start, source, destination);
    }

    @Override
    Request buildRequest() {
        MediaType mediaType = MediaType.parse("application/json");
        // fixme: 转换城市为三字码
        String dccode = this.getSource();
        String accode = this.getDestination();
        Date startDate = this.getStart();
        // fixme:  new TongChengSpiderRequestVO.SearchItem("CTU", "SHA", new SimpleDateFormat("yyyy-MM-dd").parse
        //  ("2020-06-25"))
        TongChengSpiderRequestVO tongChengSpiderRequestVO = new TongChengSpiderRequestVO(
                new TongChengSpiderRequestVO.SearchItem(dccode, accode, startDate)
        );

        RequestBody body = RequestBody.create(mediaType, tongChengSpiderRequestVO.toString());
        Request request = new Request.Builder()
                .url(TONG_CHENG_TARGET_URL)
                .method("POST", body)
                .addHeader("Content-Type", "application/json")
                .addHeader("Cookie", "__ftoken=uFXSdfnhZCN0VCTmGLwcwRlHNB%2Fay1S65QGORkfv2uAYETWvEURg8VlsY%2BA" +
                        "%2Fom6zlw%2BQLXY6SZJ133M8HbxJsw%3D%3D; __ftrace=d0769572-0ce9-46f3-b965-32f4a3cc4519")
                .build();
        return request;
    }

    @Override
    List<Flight> parseDataToFlights(String jsonData) {
        JSONArray flightArray = new JSONObject(jsonData).getJSONObject("body").getJSONArray("FlightInfoSimpleList");
        List<Flight> flights = new ArrayList<>();
        if (flightArray.length() > 0) {
            for (int i = 0; i < flightArray.length(); i++) {
                System.out.println(i);
                JSONObject jsonObject = flightArray.getJSONObject(i);

                JSONObject prices = jsonObject.getJSONObject("productPrices");
                Iterator<String> stringIterator = prices.keys();
                while (stringIterator.hasNext()) {
                    String key = stringIterator.next();
                    try {
                        flights.add(
                                new Flight(
                                        jsonObject.getString("awmsn"),
                                        jsonObject.getString("flightNo"),
                                        jsonObject.getString("equipmentName"),
                                        formatResponseDate.parse(jsonObject.getString("flyOffTime")),
                                        formatResponseDate.parse(jsonObject.getString("arrivalTime"))
                                ).newPriceEntry(
                                        key,
                                        prices.getDouble(key)
                                )
                        );
                    } catch (ParseException e) {
                        System.out.println("parse error");
                    }
                }
            }
        } else {
            return null;
        }
        return flights;
    }
}
