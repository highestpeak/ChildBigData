package com.scu.highestpeak.child.fly_advice.domain.RVO;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * @author gtrong
 */
public class ChunQiuSpiderRequestVO {
    private String sourceAirportCode;
    private String arriveAirportCode;
    private String sourceCity;
    private String arriveCity;
    private Date dtime;
    private static final String SEARCH_ITEM_FORMAT = "Active9s=&IsJC=false&IsShowTaxprice=false&Currency=0&SType=0&" +
            "Departure=%s&Arrival=%s&DepartureDate=%s&ReturnDate=null&" +
            "IsIJFlight=false&IsBg=false&IsEmployee=false&" +
            "IsLittleGroupFlight=false&SeatsNum=1&ActId=0&IfRet=false&" +
            "IsUM=false&CabinActId=null&SpecTravTypeId=&" +
            "IsContains9CAndIJ=false&DepCityCode=&ArrCityCode=&" +
            "DepAirportCode=%s&ArrAirportCode=%s&IsSearchDepAirport=false&" +
            "IsSearchArrAirport=false&isdisplayold=false";

    private static DateFormat df = new SimpleDateFormat("yyyy-MM-dd");

    public ChunQiuSpiderRequestVO() {
    }

    public ChunQiuSpiderRequestVO setSourceAirportCode(String sourceAirportCode) {
        this.sourceAirportCode = sourceAirportCode;
        return this;
    }

    public ChunQiuSpiderRequestVO setArriveAirportCode(String arriveAirportCode) {
        this.arriveAirportCode = arriveAirportCode;
        return this;
    }

    public ChunQiuSpiderRequestVO setSourceCity(String sourceCity) {
        this.sourceCity = sourceCity;
        return this;
    }

    public ChunQiuSpiderRequestVO setArriveCity(String arriveCity) {
        this.arriveCity = arriveCity;
        return this;
    }

    public ChunQiuSpiderRequestVO setDtime(Date dtime) {
        this.dtime = dtime;
        return this;
    }

    @Override
    public String toString() {
        return String.format(SEARCH_ITEM_FORMAT,
                sourceCity,arriveCity,df.format(dtime),
                sourceAirportCode,arriveAirportCode
        );
    }
}
