package com.scu.highestpeak.child.fly_advice.orm;

import com.scu.highestpeak.child.fly_advice.domain.BO.Airport;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author highestpeak
 * future: 通用mapper
 */
@Mapper
@Repository
public interface AirportMapper {
    @Select("select * from airport where name like #{likeStr}")
    @Results(id="AirportMap", value={
            @Result(column="IATA", property="IATACode", jdbcType=JdbcType.VARCHAR),
            @Result(column="city", property="cityName", jdbcType=JdbcType.VARCHAR),
            @Result(column="city_code ", property="cityCode", jdbcType=JdbcType.VARCHAR)
    })
    List<Airport> selectAirports(@Param("likeStr") String likeStr);

    @Select("select * from airport")
    @ResultMap(value="AirportMap")
    List<Airport> selectAllAirports();

    @Select("select * from airport where " +
            "latitude between #{latitudeRange[0]} and #{latitudeRange[1]} and" +
            "longitude between #{longitudeRange[0]} and #{longitudeRange[1]}")
    @ResultMap(value="AirportMap")
    List<Airport> boundAirports(
            double[] latitudeRange,
            double[] longitudeRange
    );

    @Select("select * from airport where province = #{province}")
    @ResultMap(value="AirportMap")
    List<Airport> airportInArea(String province);
}
