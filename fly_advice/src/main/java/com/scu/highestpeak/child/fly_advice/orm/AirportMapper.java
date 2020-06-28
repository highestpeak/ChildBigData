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
    @Select("select * from airport where name like '${likeStr}'")
    @Results(id="AirportMap", value={
            @Result(column="IATA", property="IATACode"),
            @Result(column="city", property="cityName"),
            @Result(column="city_code", property="cityCode")
    })
    List<Airport> selectAirportsNameMatch(String likeStr);

    @Select("select * from airport")
    @ResultMap(value="AirportMap")
    List<Airport> selectAllAirports();

    @Select("select * from airport where IATA <> #{IATACode} ")
    @ResultMap(value="AirportMap")
    List<Airport> selectAllAirportsExcept(String IATACode);

    @Select("select * from airport where level = 1")
    @ResultMap(value="AirportMap")
    List<Airport> selectHotAirports();

    @Select("select * from airport where name = #{query} or IATA = #{query}")
    @ResultMap(value="AirportMap")
    Airport selectAirportByNameOrIATA(String query);

    @Select("select * from airport where " +
            "latitude between #{latitudeRange[0]} and #{latitudeRange[1]} and " +
            "longitude between #{longitudeRange[0]} and #{longitudeRange[1]} and " +
            "IATA <> #{IATACode} ")
    @ResultMap(value="AirportMap")
    List<Airport> boundAirports(
            String IATACode,
            double[] latitudeRange,
            double[] longitudeRange
    );

    @Select("select * from airport where province = #{province}")
    @ResultMap(value="AirportMap")
    List<Airport> airportInArea(String province);
}
