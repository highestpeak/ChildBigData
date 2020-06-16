package com.scu.highestpeak.child.fly_advice.domain.BO;

import java.util.Date;

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

    private double price;

    public Flight(String airline, String flightNumber, String aircraftModel, Date start, Date end, double price) {
        this.airline = airline;
        this.flightNumber = flightNumber;
        this.aircraftModel = aircraftModel;
        this.start = start;
        this.end = end;
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
}
