package com.scu.highestpeak.child.fly_advice.domain.BO;

import java.util.Objects;

/**
 * @author highestpeak
 */
public class Airport {
    private String IATACode;
    private String name;
    private String cityName;
    private String cityCode;
    private double longitude;
    private double latitude;

    public Airport() {
    }

    public Airport(String IATACode, String name, String cityName, String cityCode, double longitude, double latitude) {
        this.IATACode = IATACode;
        this.name = name;
        this.cityName = cityName;
        this.cityCode = cityCode;
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public String getIATACode() {
        return IATACode;
    }

    public Airport setIATACode(String IATACode) {
        this.IATACode = IATACode;
        return this;
    }

    public String getName() {
        return name;
    }

    public Airport setName(String name) {
        this.name = name;
        return this;
    }

    public String getCityName() {
        return cityName;
    }

    public Airport setCityName(String cityName) {
        this.cityName = cityName;
        return this;
    }

    public String getCityCode() {
        return cityCode;
    }

    public Airport setCityCode(String cityCode) {
        this.cityCode = cityCode;
        return this;
    }

    public double getLongitude() {
        return longitude;
    }

    public Airport setLongitude(double longitude) {
        this.longitude = longitude;
        return this;
    }

    public double getLatitude() {
        return latitude;
    }

    public Airport setLatitude(double latitude) {
        this.latitude = latitude;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Airport airport = (Airport) o;
        return IATACode.equals(airport.IATACode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(IATACode);
    }
}
