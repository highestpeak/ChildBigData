package com.scu.highestpeak.child.fly_advice.service;

import com.scu.highestpeak.child.fly_advice.GlobalStaticFactory;
import com.scu.highestpeak.child.fly_advice.domain.DO.AirportInAreaDO;
import com.scu.highestpeak.child.fly_advice.orm.FlightMapper;
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
    FlightMapper flightMapper;

    /**
     * future: 多种 contains type 可以一起起作用，那就是责任链？
     * 是否选用正则表达式 还是继续使用 like
     * 暂时采用单一 contains type
     * 每个类型有各自的正则字符串？
     */
    public List<String> airportContainsSubstr(String subStr, int containsType) {
        List<AirportInAreaDO> airportList = null;
        switch (containsType) {
            case GlobalStaticFactory.SUBSTR_PREFIX:
                airportList = flightMapper.selectAirports(subStr + "%");
                break;
            case GlobalStaticFactory.SUBSTR_IN:
                airportList = flightMapper.selectAirports("%" + subStr + "%");
                break;
            default:
                break;
        }
        return airportList.stream().map(AirportInAreaDO::getName).collect(Collectors.toList());
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
    public List<String> boundAltAirport(String airport) {
        return airportDistance(airport,new double[]{DEFAULT_BOUND_DEGREE_RADIUS,DEFAULT_BOUND_DEGREE_RADIUS});
    }

    public List<AirportInAreaDO> airportInArea(List<String> provinceList) {
        return provinceList.stream()
                .flatMap(province -> airportInArea(province).stream())
                .collect(Collectors.toList());
    }

    public List<AirportInAreaDO> airportInArea(String province) {
        return flightMapper.airportInArea(province);
    }

    public List<String> airportDistance(String airport, double kmDistance) {
        return airportDistance(airport,distanceToDegree(kmDistance));
    }

    public List<String> airportDistance(String airport, double[] degreeDiff) {
        AirportInAreaDO airportInAreaDO = flightMapper.selectAirports(airport).stream().findFirst().orElse(null);
        if (airportInAreaDO==null){
            return null;
        }
        double[] latitudeRange = {
                airportInAreaDO.getLatitude()-degreeDiff[0],
                airportInAreaDO.getLatitude()+degreeDiff[0]
        };
        double[] longitudeRange = {
                airportInAreaDO.getLongitude()-degreeDiff[1],
                airportInAreaDO.getLongitude()+degreeDiff[1]
        };
        return flightMapper.boundAirports(latitudeRange, longitudeRange).stream()
                .map(AirportInAreaDO::getName)
                .collect(Collectors.toList());
    }
}
