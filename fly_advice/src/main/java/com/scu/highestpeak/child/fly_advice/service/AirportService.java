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
     *
     * @param query 查询字符串
     * @return 机场 if not exist null
     */
    public Airport searchAirport(String query) {
        return airportMapper.selectAirportByNameOrIATA(query);
    }

    public List<Airport> searchHotAirport() {
        return airportMapper.selectHotAirports();
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

    private static final double EARTH_RADIUS = 6367000.0; // Radius of the earth

    /**
     * Calculate distance between two points in latitude and longitude taking
     * into account height difference. If you are not interested in height
     * difference pass 0.0. Uses Haversine method as its base.
     * <p>
     * lat1, lon1 Start point lat2, lon2 End point el1 Start altitude in meters
     * el2 End altitude in meters
     *
     * @returns Distance in Meters
     */
    public static double distance(double lat1, double lat2, double lon1,
                                  double lon2, double el1, double el2) {

        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = EARTH_RADIUS * c * 1000; // convert to meters

        double height = el1 - el2;

        distance = Math.pow(distance, 2) + Math.pow(height, 2);

        return Math.sqrt(distance)/1000000;
    }

    public static double distance(double lat1, double lat2, double lon1,
                                  double lon2) {
        return distance(lat1, lat2, lon1, lon2, 0, 0);
    }

    /**
     * 默认临近机场搜索距离
     */
    private static double DEFAULT_BOUND_CIRCLE_RADIUS_KM = 100;
    /**
     * 默认临近机场搜索经纬度差距
     */
    private static double DEFAULT_BOUND_DEGREE_RADIUS = 5;

    /**
     * 指定范围: 经纬度、高度、地域（省份）
     * todo:
     * 距离计算经纬度
     *
     * @param airport center airport
     * @return bound airport name string list
     */
    public List<Airport> boundAltAirport(String airport) {
        return airportDistance(airport, DEFAULT_BOUND_CIRCLE_RADIUS_KM);
    }

    public List<Airport> airportInArea(List<String> provinceList) {
        return provinceList.stream()
                .flatMap(province -> airportInArea(province).stream())
                .collect(Collectors.toList());
    }

    public List<Airport> airportInArea(String province) {
        return airportMapper.airportInArea(province);
    }

    public List<Airport> airportDistance(String centerAirport, double kmDistance) {
        Airport centerAirportDO = airportMapper.selectAirportByNameOrIATA(centerAirport);
        List<Airport> airportList = airportMapper.selectAllAirportsExcept(centerAirportDO.getIATACode());
        List<Airport> collect = airportList.stream().filter(airport ->
                distance(
                        centerAirportDO.getLatitude(), airport.getLatitude(),
                        centerAirportDO.getLongitude(), airport.getLongitude()
                ) < kmDistance
        ).collect(Collectors.toList());
        return collect;
    }
}
