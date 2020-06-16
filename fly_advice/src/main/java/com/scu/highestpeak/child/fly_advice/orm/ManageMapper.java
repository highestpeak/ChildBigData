package com.scu.highestpeak.child.fly_advice.orm;

import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

/**
 * @author gtrong
 */
@Mapper
@Repository
public interface ManageMapper {

    @Insert({"INSERT into supplier  values ('#{id}','${name)','#(distrust}')"})
    int addSupplier(int id,String name,int distrust);

    @Insert({"INSERT into user  values ('${email}','${password}','${nickname}'"})
    int addUser(String email, String password, String nickname);

    @Select({"SELECT ${selectCol} from ${table} where ${wherecase}"})
    List<Map<String,String>> find(String selectCol, String table, String wherecase);

    @Delete({"DELETE from supplier where id=#{id}"})
    int deleteSupplier( int id);

    @Delete({"DELETE from user where id=#{id}"})
    int deleteUser(int id);

    @Update({"UPDATE supplier set name=${name},distrust=#{distrust}, where id=#{id}"})
    int updateSupplier(String name, String distrust,int id);


    @Update({"UPDATE user set nickname=${nickname},email=${email},password=${password},admin=#{admin} where id=#{id}"})
    int updateUser( int id, String email, String password,int admin);

    @Select({"SELECT * from user_history where id =#{id}"})
    List<Map<String ,String>> get(int id);

    @Insert({"INSERT into user_history values('#{id}','#{plan_id}','#{plan_order}','${cabin_class}''#{user_id}','#{flight_id}','${flight_type}','#{supplier_id}','#{buy_datae}'"})
    int addHistory(int id,int plan_id,int plan_order,String cabin_class,int user_id,int flight_id,String flight_type,int supplier_id,int buy_date);

    @Delete({"Delete from user_history where id=#{id}"})
//    @ResponseBody
    int deleteHistory(int id);

    @Select({"SELECT * from history_flight where id =#{id}"})
    List<Map<String ,String>> getFlight(int id);

    @Insert({"INSERT into history_flight values('#{id}','${flight_no}','${aircraft_model}','#{start_time}''#{end_time}','#{source_airport}','#{destination_airport','#{price}','#{stops}'"})
    int addFlight(int id,String flight_no,String aircraft_model,int start_time,int end_time,int source_airport,int destination_airport,double price,int stops);

    @Delete({"Delete from history_flight where id=#{id}"})
    int deleteFlight(int id);

}
