package com.scu.highestpeak.child.fly_advice.service.FlightSpider;

import com.scu.highestpeak.child.fly_advice.domain.BO.Flight;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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
    public static List<AbstractCrawlTask> crawlTaskList(Date start, String source, String destination) {
        List<AbstractCrawlTask> crawlTaskList = new ArrayList<>();
        crawlTaskList.add(new XieChengSpider(start, source, destination));
        crawlTaskList.add(new ChunQiuSpider(start, source, destination));
        crawlTaskList.add(new ExpediaSpider(start, source, destination));
        crawlTaskList.add(new TongChengSpider(start, source, destination));
        return crawlTaskList;
    }

    /**
     * todo: okhttp3 请求航班
     * 默认请求直飞航班
     *
     * @param start       出发日期
     * @param source      出发地
     * @param destination 到达地
     * @return 出发地到到达地的航班列表
     */
    public static List<Flight> crawl(Date start, String source, String destination) {
        List<Flight> results = new ArrayList<>();
        List<Future<List<Flight>>> futures = new ArrayList<>();
        crawlTaskList(start, source, destination).forEach(crawlTask -> futures.add(spiderExec.submit(crawlTask)));
        for (Future<List<Flight>> fs : futures) {
            try {
                results.addAll(fs.get());
            } catch (InterruptedException e) {
                System.out.println("InterruptedException");
            } catch (ExecutionException e) {
                System.out.println("ExecutionException");
            }
        }
        return results;
    }
}
