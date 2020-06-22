package com.scu.highestpeak.child.fly_advice.domain.RVO;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class TongChengSpiderRequestVO {
    private SearchItem searchitem;

    private static final String SEARCH_ITEM_FORMAT = "{\r\n    " +
            "\"Departure\": \"%s\",\r\n" +
            "\"Arrival\": \"%s\",\r\n"+
            "\"GetType\": \"1\",\r\n    \"QueryType\": \"1\",\r\n"+
            "\"DepartureDate\": \"%s\",\r\n"+
            "\"IsBaby\": 0\r\n}";
    private static DateFormat df = new SimpleDateFormat("yyyy-MM-dd");

    public TongChengSpiderRequestVO(SearchItem searchitem) {
        this.searchitem = searchitem;
    }

    @Override
    public String toString() {
        SearchItem item = searchitem;
        return String.format(SEARCH_ITEM_FORMAT,item.dccode,item.accode,df.format(item.dtime));
    }

    public static class SearchItem {
        private String dccode;
        private String accode;
        private Date dtime;

        public SearchItem(String dccode, String accode, Date dtime) {
            this.dccode = dccode;
            this.accode = accode;
            this.dtime = dtime;
        }
    }

}
