package com.highestpeakscu.child_bigdata_smallpractice.domain.DO;

public class AnalysisDO {
    private String analy_key;
    private String analy_value;

    public AnalysisDO() {
    }

    public AnalysisDO(String analy_key, String analy_value) {
        this.analy_key = analy_key;
        this.analy_value = analy_value;
    }

    public String getAnaly_key() {
        return analy_key;
    }

    public void setAnaly_key(String analy_key) {
        this.analy_key = analy_key;
    }

    public String getAnaly_value() {
        return analy_value;
    }

    public void setAnaly_value(String analy_value) {
        this.analy_value = analy_value;
    }
}
