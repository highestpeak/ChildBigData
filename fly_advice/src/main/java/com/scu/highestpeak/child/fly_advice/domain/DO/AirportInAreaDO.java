package com.scu.highestpeak.child.fly_advice.domain.DO;

/**
 * @author highestpeak
 */
public class AirportInAreaDO {
    private String  name;
    private String  city;
    private Double  latitude;
    private Double  longitude;
    private Integer  level;

    public AirportInAreaDO(String name, String city, Double latitude, Double longitude, Integer level) {
        this.name = name;
        this.city = city;
        this.latitude = latitude;
        this.longitude = longitude;
        this.level = level;
    }

    public String getName() {
        return name;
    }

    public String getCity() {
        return city;
    }

    public Double getLatitude() {
        return latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public Integer getLevel() {
        return level;
    }

    @Override
    public String toString() {
        return "AirportInAreaDO{" +
                "name='" + name + '\'' +
                ", city='" + city + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", level=" + level +
                '}';
    }
}
