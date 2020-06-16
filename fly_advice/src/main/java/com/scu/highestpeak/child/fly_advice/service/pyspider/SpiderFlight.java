package com.scu.highestpeak.child.fly_advice.service.pyspider;

import com.scu.highestpeak.child.fly_advice.domain.BO.Flight;

import java.util.Date;
import java.util.List;

/**
 * @author highestpeak
 */
public class SpiderFlight {
    /**
     * todo:只返回 出发地 目的地 出发时间 完全和参数相同的 航班
     *   如果含有经停,则返回每一段，格式未定
     * @param start
     * @param source
     * @param destination
     * @return
     */
    public static List<Flight> crawl(Date start, String source, String destination){
        return null;
    }
}
