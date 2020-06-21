package com.scu.highestpeak.child.fly_advice.domain.RVO;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * @author highestpeak
 */
public class XieChengSpiderRequestVO {
    //    private int trptpe = 1;
    //    private Object head;

    private List<SearchItem> searchitem;

    private static final String XIE_CHENG_FORMAT =
            "{\r\n    " +
                "\"trptpe\": 1,\r\n    " +
                "\"searchitem\": [\r\n" +
                "%s" +
                "],\r\n    " +
                "\"head\": {},\r\n" +
                "\"contentType\": \"json\"\r\n"+
            "}";
    private static final String SEARCH_ITEM_FORMAT =
            "{\r\n            " +
                "\"dccode\": \"%s\",\r\n            " +
                "\"accode\": \"%s\",\r\n            " +
                "\"dtime\": \"%s\"\r\n        " +
            "}\r\n";
    private static DateFormat df = new SimpleDateFormat("yyyy-MM-dd");

    public XieChengSpiderRequestVO(List<SearchItem> searchitem) {
        this.searchitem = searchitem;
    }

    /**
     * https://stackoverflow.com/questions/3395286/remove-last-character-of-a-stringbuilder
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (SearchItem item :
                searchitem) {
            builder.append(
                    String.format(SEARCH_ITEM_FORMAT,
                            item.dccode,item.accode,df.format(item.dtime)
                    )
            );
            builder.append(",");
        }
        builder.setLength(builder.length() - 1);
        return String.format(XIE_CHENG_FORMAT,builder.toString());
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
