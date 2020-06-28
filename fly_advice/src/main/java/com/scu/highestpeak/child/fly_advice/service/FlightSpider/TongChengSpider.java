package com.scu.highestpeak.child.fly_advice.service.FlightSpider;

import com.scu.highestpeak.child.fly_advice.domain.BO.Airport;
import com.scu.highestpeak.child.fly_advice.domain.BO.Flight;
import com.scu.highestpeak.child.fly_advice.domain.RVO.FlightCrawl;
import com.scu.highestpeak.child.fly_advice.domain.RVO.TongChengSpiderRequestVO;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import org.json.JSONArray;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
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
    private static final String SUPPLIER_NAME = "同城";
    private static final String FORMAT_DATE_STR = "yyyy-MM-dd HH:mm";
    private static DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(FORMAT_DATE_STR);

    public TongChengSpider(Airport source, Airport destination, Date startDate) {
        super(source, destination, startDate);
    }

    @Override
    Request buildRequest() {
        MediaType mediaType = MediaType.parse("application/json");
        // 转换城市为三字码
        String dccode = this.getSource();
        String accode = this.getDestination();
        Date startDate = this.getStart();
        // new TongChengSpiderRequestVO.SearchItem("CTU", "SHA", new SimpleDateFormat("yyyy-MM-dd").parse
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
    synchronized List<Flight> parseDataToFlights(String jsonData) {
        JSONArray flightArray = new JSONObject(jsonData).getJSONObject("body").getJSONArray("FlightInfoSimpleList");
        List<Flight> flights = new ArrayList<>();
        if (flightArray.length() <= 0) {
            return flights;
        }
        for (int i = 0; i < flightArray.length(); i++) {
            JSONObject jsonObject = flightArray.getJSONObject(i);

            JSONObject prices = jsonObject.getJSONObject("productPrices");
            FlightCrawl flightCrawl = null;
            flightCrawl = new FlightCrawl(
                    jsonObject.getString("airCompanyName"),
                    jsonObject.getString("flightNo"),
                    jsonObject.getString("equipmentName"),
                    generateDate(jsonObject.getString("flyOffTime")),
                    generateDate(jsonObject.getString("arrivalTime"))
            );
            if (flightCrawl == null) {
                continue;
            }
            Iterator<String> stringIterator = prices.keys();
            while (stringIterator.hasNext()) {
                String key = stringIterator.next();
                try {
                    flightCrawl.newPriceEntry(key, prices.getDouble(key));
                }catch (Exception e){
                    System.out.println("xxx");
                }
            }
            flightCrawl.addSupplier(SUPPLIER_NAME);
            flights.add(flightCrawl);
        }
        return flights;
    }

    private Date generateDate(String toParse) {
        return Date.from(LocalDateTime.parse(toParse, dateTimeFormatter).atZone(ZoneId.systemDefault()).toInstant());
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
