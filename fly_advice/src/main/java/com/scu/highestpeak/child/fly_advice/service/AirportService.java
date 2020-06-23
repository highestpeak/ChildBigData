package com.scu.highestpeak.child.fly_advice.service;

import com.scu.highestpeak.child.fly_advice.GlobalStaticFactory;
import com.scu.highestpeak.child.fly_advice.domain.BO.Airport;
import com.scu.highestpeak.child.fly_advice.orm.AirportMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static java.lang.Math.toRadians;

/**
 * @author highestpeak
 */
@Service
public class AirportService {
    @Autowired
    AirportMapper airportMapper;

    private static final int IATA_LEN = 3;
    /**
     * 搜索机场，搜索字段有多种可能,只能搜索一个机场
     * 1. 为机场名称
     * 2. 为机场三字码
     * @param query 查询字符串
     * @return 机场 if not exist null
     */
    public Airport searchAirport(String query){
        return airportMapper.selectAirportByNameOrIATA(query);
    }

    /**
     * future: 多种 contains type 可以一起起作用，那就是责任链？
     * 是否选用正则表达式 还是继续使用 like
     * 暂时采用单一 contains type
     * 每个类型有各自的正则字符串？
     */
    public List<String> airportContainsSubstr(String subStr, int containsType) {
        List<Airport> airportList = null;
        switch (containsType) {
            case GlobalStaticFactory.SUBSTR_PREFIX:
                airportList = airportMapper.selectAirportsNameMatch(subStr + "%");
                break;
            case GlobalStaticFactory.SUBSTR_IN:
                airportList = airportMapper.selectAirportsNameMatch("%" + subStr + "%");
                break;
            default:
                break;
        }
        return airportList.stream().map(Airport::getName).collect(Collectors.toList());
    }

    private static final double EARTH_RADIUS = 6367000.0;

    /**
     * 根据距离计算 经度 维度 差值
     * 公式来源：https://www.jianshu.com/p/ed6ea376911e
     * @return {经度差，维度差}
     */
    private double[] distanceToDegree(double kmDistance) {
        double dlng = 2 * Math.asin(Math.sin(kmDistance / (2 * EARTH_RADIUS)));
        dlng = Math.toRadians(dlng); // 弧度转换成角度
        // 然后求南北两侧的范围边界，在 haversin 公式中令 Δλ = 0
        double dlat = kmDistance / EARTH_RADIUS;
        dlat = Math.toDegrees(dlat);// 弧度转换成角度
        return new double[]{dlng, dlat};
    }

    /**
     * 根据经纬度计算两点距离
     * 公式来源：https://tech.meituan.com/2014/09/05/lucene-distance.html
     * @return 两地距离
     */
    private static double pointDistance(double lat1, double lng1, double lat2, double lng2, double[] a) {
        double dx = lng1 - lng2; // 经度差值
        double dy = lat1 - lat2; // 纬度差值
        double b = (lat1 + lat2) / 2.0; // 平均纬度
        double lx = toRadians(dx) * EARTH_RADIUS * Math.cos(toRadians(b)); // 东西距离
        double ly = EARTH_RADIUS * toRadians(dy); // 南北距离
        return Math.sqrt(lx * lx + ly * ly);  // 用平面的矩形对角距离公式计算总距离
    }

    /**
     * 默认临近机场搜索距离
     */
    private static double DEFAULT_BOUND_CIRCLE_RADIUS_KM = 100;
    /**
     * 默认临近机场搜索经纬度差距
     */
    private static double DEFAULT_BOUND_DEGREE_RADIUS = 10;

    /**
     * 指定范围: 经纬度、高度、地域（省份）
     * 距离计算经纬度
     * @param airport center airport
     * @return bound airport name string list
     */
    public List<Airport> boundAltAirport(String airport) {
        return airportDistance(airport,new double[]{DEFAULT_BOUND_DEGREE_RADIUS,DEFAULT_BOUND_DEGREE_RADIUS});
    }

    public List<Airport> airportInArea(List<String> provinceList) {
        return provinceList.stream()
                .flatMap(province -> airportInArea(province).stream())
                .collect(Collectors.toList());
    }

    public List<Airport> airportInArea(String province) {
        return airportMapper.airportInArea(province);
    }

    public List<Airport> airportDistance(String airport, double kmDistance) {
        return airportDistance(airport,distanceToDegree(kmDistance));
    }

    public List<Airport> airportDistance(String airport, double[] degreeDiff) {
        Airport airportDO = airportMapper.selectAirportsNameMatch(airport).stream().findFirst().orElse(null);
        if (airportDO==null){
            return null;
        }
        double[] latitudeRange = {
                airportDO.getLatitude()-degreeDiff[0],
                airportDO.getLatitude()+degreeDiff[0]
        };
        double[] longitudeRange = {
                airportDO.getLongitude()-degreeDiff[1],
                airportDO.getLongitude()+degreeDiff[1]
        };
        return airportMapper.boundAirports(latitudeRange, longitudeRange);
    }
}
