package com.scu.highestpeak.child.fly_advice.domain.DTO;

import java.util.Date;

/**
 * @author highestpeak
 */
public class FlightSearchListDTO {
    private String sourcePlace;
    private String destinationPlace;
    private Date startDate;

    public FlightSearchListDTO(String sourcePlace, String destinationPlace, Date startDate) {
        this.sourcePlace = sourcePlace;
        this.destinationPlace = destinationPlace;
        this.startDate = startDate;
    }

    public String getSourcePlace() {
        return sourcePlace;
    }

    public FlightSearchListDTO setSourcePlace(String sourcePlace) {
        this.sourcePlace = sourcePlace;
        return this;
    }

    public String getDestinationPlace() {
        return destinationPlace;
    }

    public FlightSearchListDTO setDestinationPlace(String destinationPlace) {
        this.destinationPlace = destinationPlace;
        return this;
    }

    public Date getStartDate() {
        return startDate;
    }

    public FlightSearchListDTO setStartDate(Date startDate) {
        this.startDate = startDate;
        return this;
    }
}
