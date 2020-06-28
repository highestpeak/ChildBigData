package com.scu.highestpeak.child.fly_advice.orm;

import com.scu.highestpeak.child.fly_advice.domain.DO.AirportHistoryFlowDO;
import com.scu.highestpeak.child.fly_advice.domain.DO.FlightHistoryFlowDO;
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

    /**
     * @param src  target
     * @param type "dst_port" or "src_port"
     */
    @Select("SELECT sum(num),day " +
            "FROM m_airline_day_analysis WHERE ${type} like '${src}%' GROUP BY day;")
    List<AirportHistoryFlowDO> airlineDayFlow(String src, String type);

    @Select("SELECT SUBSTRING_INDEX(SUBSTRING_INDEX(src_port, 'T', 1), '机场', 1) as src," +
            " SUBSTRING_INDEX(SUBSTRING_INDEX(dst_port, 'T', 1), '机场', 1) as dst," +
            " MAX(price_h) price_h,MIN(price_l) price_l,AVG(price_av) price_av," +
            " ${column} " +
            "FROM ${table} " +
            "WHERE src_port like '${src}%' and dst_port like '${dst}%' " +
            "GROUP BY src,dst,`${column}`;")
    List<FlightHistoryFlowDO> flightHistoryPrice(String src, String dst, String table,String column);
}
