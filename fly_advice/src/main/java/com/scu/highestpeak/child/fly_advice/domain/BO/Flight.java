package com.scu.highestpeak.child.fly_advice.domain.BO;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author highestpeak
 */
public class Flight {
    /**
     * 航空公司
     */
    private String airline;
    /**
     * 航班号
     */
    private String flightNumber;
    /**
     * 机型
     */
    private String aircraftModel;

    private Date start;
    private Date end;
    /**
     * 分舱位类型的爬取到的价格
     */
    private Map<String,Double> priceMap;
    /**
     * 没有分舱位类型的爬取到的价格
     */
    private double price;

    public Flight() {
    }

    public Flight(String airline, String flightNumber, String aircraftModel, Date start, Date end) {
        this.airline = airline;
        this.flightNumber = flightNumber;
        this.aircraftModel = aircraftModel;
        this.start = start;
        this.end = end;
    }

    public Flight(String airline, String flightNumber, String aircraftModel, Date start, Date end, double price) {
        this(airline, flightNumber, aircraftModel, start, end);
        this.price = price;
    }

    public String getAirline() {
        return airline;
    }

    public String getFlightNumber() {
        return flightNumber;
    }

    public String getAircraftModel() {
        return aircraftModel;
    }

    public Date getStart() {
        return start;
    }

    public Date getEnd() {
        return end;
    }

    public double getPrice() {
        return price;
    }

    public Flight newPriceEntry(String name,Double price){
        if (this.priceMap==null){
            this.priceMap=new HashMap<>();
        }
        this.priceMap.put(name,price);
        return this;
    }

    public Flight setAirline(String airline) {
        this.airline = airline;
        return this;
    }

    public Flight setFlightNumber(String flightNumber) {
        this.flightNumber = flightNumber;
        return this;
    }

    public Flight setAircraftModel(String aircraftModel) {
        this.aircraftModel = aircraftModel;
        return this;
    }

    public Flight setStart(Date start) {
        this.start = start;
        return this;
    }

    public Flight setEnd(Date end) {
        this.end = end;
        return this;
    }

    public Flight setPrice(double price) {
        this.price = price;
        return this;
    }
}
