package com.scu.highestpeak.child.fly_advice.orm;

import com.scu.highestpeak.child.fly_advice.domain.DO.AirportInAreaDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author highestpeak
 * future: 通用mapper
 */
@Mapper
@Repository
public interface FlightMapper {
    @Select("select name,city,latitude,longitude,level from airport_table where name like #{likeStr}")
    List<AirportInAreaDO> selectAirports(String likeStr);

    @Select("select name,city,latitude,longitude,level from airport where " +
            "latitude between #{latitudeRange[0] } and #{latitudeRange[1] } and" +
            "longitude between #{longitudeRange[0]} and #{longitudeRange[1]}")
    List<AirportInAreaDO> boundAirports(
            double[] latitudeRange,
            double[] longitudeRange
    );

    @Select("select name,city,latitude,longitude,level from airport where province = #{province}")
    List<AirportInAreaDO> airportInArea(String province);
}
