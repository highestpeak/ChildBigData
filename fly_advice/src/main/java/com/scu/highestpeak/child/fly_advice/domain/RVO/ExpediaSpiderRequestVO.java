package com.scu.highestpeak.child.fly_advice.domain.RVO;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * @author gtrong
 * @author highestpeak
 */
public class ExpediaSpiderRequestVO {
    private List<SearchItem> searchitem;
    private static final String EXPEDIA_FORMAT = "{\r\n    \"trips\": [\r\n        \r\n    ],\r\n    " +
            "\"numberOfAdults\": 1,\r\n    " +
            "\"childAges\": [],\r\n    \"infantInLap\": [\r\n        false\r\n    ],\r\n    \"isRefundableFlight\": " +
            "false,\r\n    \"isNonStopFlight\": true,\r\n    \"airlinePreference\": \"\",\r\n    \"cabinClass\": " +
            "\"coach\",\r\n    \"pageSelectionParameters\": {},\r\n    \"packageType\": \"f\",\r\n    \"routeType\": " +
            "\"OneWay\",\r\n    \"hashCodeToCheckValidation\": \"\",\r\n    \"stubFile\": null\r\n}";
    private static final String SEARCH_ITEM_FORMAT = "{\r\n            \" +\n" +
            "            \"\"departureAirportCode\": \"%s\",\r\n            \"arrivalAirportCode\": " +
            "\"%s\",\r\n            \" +\n" +
            "            \"\"departureDate\": \"%s\"\r\n        }";

    private static DateFormat df = new SimpleDateFormat("yyyy-MM-dd");

    public ExpediaSpiderRequestVO(List<SearchItem> searchitem) {
        this.searchitem = searchitem;
    }


    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (SearchItem item :
                searchitem) {
            builder.append(
                    String.format(SEARCH_ITEM_FORMAT,
                            item.dccode, item.accode, df.format(item.dtime)
                    )
            );
            builder.append(",");
        }
        builder.setLength(builder.length() - 1);
        return String.format(EXPEDIA_FORMAT, builder.toString());
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
