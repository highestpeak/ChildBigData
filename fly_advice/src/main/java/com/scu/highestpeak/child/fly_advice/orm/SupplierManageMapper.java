package com.scu.highestpeak.child.fly_advice.orm;


import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @author gtrong
 */
@Mapper
@Repository
public interface SupplierManageMapper {
    @Insert({"INSERT into &{table}(IATA,ICAO,name,country,area,airline,AirLineAlliance,fleetsNum,navigableCity,discription,wiki,website) value(${IATA},${ICAO},${name},${country,${are},${airline},${AirLineAlliance},${fleetsNum},${navigableCity},${description},${wiki},${website}"})
    int addSupplier(String IATA, String ICAO, String name, String country, String area, String airline, String AirLineAlliance, String fleetsNum, String navigableCity, String discription, String wiki, String website);

    @Select({"SELECT ${selectCol} from ${table} where ${wherecase}"})
    List<Map<String,String>> find(String selectCol, String table, String wherecase);


    @Delete("DELETE from ${table} where suppplier_id=${id}")
    int deleteSppplier(String table, int id);

    @Update({"UPDATE ${table} set IATA=${IATA},ICAO=${ICAO},name=${name},country=${country,area=${area},airline=${airline},AirLineAlliance=${AirLineAlliance},fleetsNum=${fleetsNum},navigableCity=${NavigableCity},description=${description},wiki=${wiki},website=${website}, where id=${supplier_id}"})
    int UpdateSuppplier(String IATA, String ICAO, String name, String country, String area, String airline, String AirLineAlliance, String fleetsNum, String navigableCity, String discription, String wiki, String website);

}
