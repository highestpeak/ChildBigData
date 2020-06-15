package com.scu.highestpeak.child.fly_advice.orm;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author highestpeak
 * future: 通用mapper
 * fixme: 更正sql语句
 */
@Mapper
@Repository
public interface FlightMapper {
    @Select("select name from airport_table where name like #{likeStr}")
    List<String> selectAirports(String likeStr);

    @Select("select name from airport_table where " +
            "latitude between #{latitudeRange[0] } and #{latitudeRange[1] } and" +
            "longitude between #{longitudeRange[0]} and #{longitudeRange[1]} and" +
            "altitude between #{altitudeRange[0] } and #{altitudeRange[1] } and" +
            "areaStr in (#{areaStr})")
    List<String> boundAirports(
            double[] latitudeRange,
            double[] longitudeRange,
            double[] altitudeRange,
            String areaStr
    );
}
