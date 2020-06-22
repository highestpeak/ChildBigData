package com.scu.highestpeak.child.fly_advice.domain.RVO;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * @author gtrong
 */
public class ChunQiuSpiderRequestVO {

    private SearchItem searchitem;
    private static final String SEARCH_ITEM_FORMAT = "Active9s=&IsJC=false&IsShowTaxprice=false&Currency=0&SType=0&" +
            "Departure=%s&Arrival=%s&DepartureDate=%s&ReturnDate=null&" +
            "IsIJFlight=false&IsBg=false&IsEmployee=false&" +
            "IsLittleGroupFlight=false&SeatsNum=1&ActId=0&IfRet=false&" +
            "IsUM=false&CabinActId=null&SpecTravTypeId=&" +
            "IsContains9CAndIJ=false&DepCityCode=CTU&ArrCityCode=&" +
            "DepAirportCode=CTU&ArrAirportCode=SHA&IsSearchDepAirport=false&" +
            "IsSearchArrAirport=false&isdisplayold=false";

    private static DateFormat df = new SimpleDateFormat("yyyy-MM-dd");


    public ChunQiuSpiderRequestVO(SearchItem searchitem) {
        this.searchitem = searchitem;
    }

    @Override
    public String toString() {
        SearchItem item = searchitem;
        return String.format(SEARCH_ITEM_FORMAT, item.dccode, item.accode, df.format(item.dtime));
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
