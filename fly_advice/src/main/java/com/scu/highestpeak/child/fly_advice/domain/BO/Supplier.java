package com.scu.highestpeak.child.fly_advice.domain.BO;

/**
 * @author highestpeak
 */
public class Supplier {
    private Integer id;
    private String name;
    private Integer distrust;
    private String website;

    public Supplier(Integer id, String name, Integer distrust, String website) {
        this.id = id;
        this.name = name;
        this.distrust = distrust;
        this.website = website;
    }

    public Integer getId() {
        return id;
    }

    public Supplier setId(Integer id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public Supplier setName(String name) {
        this.name = name;
        return this;
    }

    public Integer getDistrust() {
        return distrust;
    }

    public Supplier setDistrust(Integer distrust) {
        this.distrust = distrust;
        return this;
    }

    public String getWebsite() {
        return website;
    }

    public Supplier setWebsite(String website) {
        this.website = website;
        return this;
    }
}
