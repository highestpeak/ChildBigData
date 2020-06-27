package com.scu.highestpeak.child.fly_advice.orm;

import com.scu.highestpeak.child.fly_advice.domain.DO.AirportHistoryFlowDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author highestpeak
 */
@Mapper
@Repository
public interface HistoryMapper {
    /**
     * @param src format: 上海虹桥，成都双流，（不包含机场、国际机场 几字）
     * @return dst键值对
     */
    @Select("SELECT SUBSTRING_INDEX(SUBSTRING_INDEX(dst_port, 'T', 1), '机场', 1) as dst," +
            "sum(num) as num " +
            "FROM history_airline_analysis " +
            "WHERE src_port like '${src}%' GROUP BY dst ORDER BY num DESC;")
    List<AirportHistoryFlowDO> airlineHistory(String src);

    @Select("SELECT SUBSTRING_INDEX(SUBSTRING_INDEX(dst_port, 'T', 1), '机场', 1) as dst," +
            "sum(num) as num " +
            "FROM history_airline_analysis " +
            "WHERE src_port like '${src}%' GROUP BY dst ORDER BY num DESC;")
    List<AirportHistoryFlowDO> airlineDayFlow(String src,String type);
}
