package com.scu.highestpeak.child.fly_advice.domain.RVO;

import com.scu.highestpeak.child.fly_advice.domain.BO.Flight;

import java.util.*;

/**
 * @author highestpeak
 */
public class FlightCrawl extends Flight {
    private Map<String,Double> supplier;

    public FlightCrawl() {
    }

    public FlightCrawl(String airline, String flightNumber, String aircraftModel, Date start, Date end) {
        super(airline, flightNumber, aircraftModel, start, end);
    }

    public FlightCrawl(String airline, String flightNumber, String aircraftModel, Date start, Date end, double price) {
        super(airline, flightNumber, aircraftModel, start, end, price);
    }

    public  Map.Entry<String,Double> peekOnlySupplier() {
        if (supplier==null || supplier.size()<1){
            return null;
        }
        return supplier.entrySet().iterator().next();
    }

    public Map<String,Double> getSupplier() {
        return supplier;
    }

    public FlightCrawl addSupplier(String supplier) {
        if (this.supplier==null){
            this.supplier = new HashMap<>();
        }
        this.supplier.put(supplier,getPrice()) ;
        return this;
    }

    public FlightCrawl addSupplier(String supplier,Double price) {
        if (supplier==null){
            this.supplier = new HashMap<>();
        }
        this.supplier.put(supplier,price) ;
        return this;
    }
}
