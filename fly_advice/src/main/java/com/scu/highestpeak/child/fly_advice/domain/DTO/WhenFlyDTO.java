package com.scu.highestpeak.child.fly_advice.domain.DTO;

import java.util.Date;

/**
 * @author highestpeak
 */
public class WhenFlyDTO {
    private String source;
    private String destination;
    private Date rangeStart;
    private Date rangeEnd;

    public WhenFlyDTO(String source, String destination, Date rangeStart, Date rangeEnd) {
        this.source = source;
        this.destination = destination;
        this.rangeStart = rangeStart;
        this.rangeEnd = rangeEnd;
    }

    public String getSource() {
        return source;
    }

    public String getDestination() {
        return destination;
    }

    public Date getRangeStart() {
        return rangeStart;
    }

    public Date getRangeEnd() {
        return rangeEnd;
    }

    @Override
    public String toString() {
        return "WhenFlyDTO{" +
                "source='" + source + '\'' +
                ", destination='" + destination + '\'' +
                ", rangeStart=" + rangeStart +
                ", rangeEnd=" + rangeEnd +
                '}';
    }
}
