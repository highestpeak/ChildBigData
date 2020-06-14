package com.scu.highestpeak.child.fly_advice.dao;

import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Mapper
@Repository
public interface UserMangerMapper {
    @Insert({"INSERT into &{table}(email,password,nickname) value(${email},${password},${nickname}"})
    int addUser(String table, String email, String password, String nickname);

    @Select({"SELECT ${selectCol} from ${table} where ${wherecase}"})
    List<Map<String,String>> findUser(String selectCol, String table, String wherecase);

    @Delete("DELETE from ${table} where user_id=${id}")
    int deleteUser(String table, int id);

    @Update({"UPDATE ${table} set nickname=${nickname},email=${email},password=${password} where id=${user_id}"})
    int updateUser(String table, int user_id, String email, String password);
}
