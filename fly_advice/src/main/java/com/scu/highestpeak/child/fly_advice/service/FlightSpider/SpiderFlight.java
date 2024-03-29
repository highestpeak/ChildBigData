package com.scu.highestpeak.child.fly_advice.service.FlightSpider;

import com.scu.highestpeak.child.fly_advice.domain.BO.Airport;
import com.scu.highestpeak.child.fly_advice.domain.BO.Flight;
import com.scu.highestpeak.child.fly_advice.domain.RVO.FlightCrawl;

import java.util.*;
import java.util.concurrent.*;

/**
 * @author highestpeak
 */
public class SpiderFlight {
    private static final int CPU_CORE_NUM = 4;
    private static final int SPIDERS_NUM = 10;
    private static final int KEEP_ALIVE_TIME = 2;
    private static final int MAX_SPIDER_NUM = 8;
    private static ThreadFactory spiderThreadFactory = r -> new Thread();

    private static ExecutorService spiderExec = Executors.newFixedThreadPool(MAX_SPIDER_NUM);

    /**
     * future: 一个特色：用户可以选取信息源，指定从哪个地方爬取
     */
    public static List<AbstractCrawlTask> crawlTaskList(Airport source, Airport destination, Date startDate) {
        List<AbstractCrawlTask> crawlTaskList = new ArrayList<>();
        crawlTaskList.add(new XieChengSpider(source, destination, startDate));
        crawlTaskList.add(new ChunQiuSpider(source, destination, startDate));
        crawlTaskList.add(new ExpediaSpider(source, destination, startDate));
        crawlTaskList.add(new TongChengSpider(source, destination, startDate));
        return crawlTaskList;
    }

    /**
     * okhttp3 请求航班
     * 默认请求直飞航班
     * @param source 出发地
     * @param destination 到达地
     * @param startDate 出发日期
     * @return 出发地到到达地的航班列表
     * todo: 把所有 供应商 的异常都抛出，在这个类里处理这一级异常
     *  添加供应商模块
     *  修改flight为子类
     *  机票购买页面由前端拼接
     */
    public static List<Flight> crawl(Airport source, Airport destination, Date startDate) {
        List<Future<List<Flight>>> futures = new ArrayList<>();
        ConcurrentHashMap<String, Flight> codeFlightMap = new ConcurrentHashMap<>();
        crawlTaskList(source, destination, startDate).forEach(crawlTask -> futures.add(spiderExec.submit(crawlTask)));
        for (Future<List<Flight>> fs : futures) {
            try {
                List<Flight> searched = fs.get();
                searched.forEach(flight -> {
                    Map.Entry<String, Double> onlySupplier = ((FlightCrawl)flight).peekOnlySupplier();
                    String flightNumber = flight.getFlightNumber();
                    if (!codeFlightMap.containsKey(flightNumber)){
                        codeFlightMap.put(flightNumber,flight);
                    }else {
                        ((FlightCrawl)codeFlightMap.get(flightNumber))
                                .addSupplier(onlySupplier.getKey(),onlySupplier.getValue());
                    }
                });
            } catch (InterruptedException e) {
                System.out.println("InterruptedException");
            } catch (ExecutionException e) {
                System.out.println("ExecutionException");
            } catch (Exception e){
                System.out.println("UNKNOWN Exception");
            }
        }
        return new ArrayList<>(codeFlightMap.values());
    }
}
