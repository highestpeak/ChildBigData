package com.scu.highestpeak.child.fly_advice.controller;


import com.scu.highestpeak.child.fly_advice.dao.SupplierManageMapper;
import com.scu.highestpeak.child.fly_advice.dao.UserMangerMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "*")
@RestController

public class ManagerContoller {
    @Autowired
    private UserMangerMapper userMangerMapper;

    @Autowired
    private SupplierManageMapper supplierManageMapper;
    @DeleteMapping("api/manage/")
    @ResponseBody
    public void delete(@RequestBody Map<String,String> map,@RequestParam(value="role",required =false,defaultValue ="")String role){
        if (role=="user") {
            userMangerMapper.deleteUser("tablename", Integer.parseInt(map.get("user_id")));
        }if (role=="supplier"){
            supplierManageMapper.deleteSUppplier("tablename",Integer.parseInt(map.get("supplier_id")));
        }

    }

    @PutMapping("api/manage/")
    @ResponseBody
    public void update(@RequestBody Map<String ,String> map,@RequestParam(value="role",required =false,defaultValue ="")String role) {
        if (role == "user") {
            userMangerMapper.updateUser("tablename", Integer.parseInt(map.get("user_id")), map.get("email"), map.get("password"));
        }if(role=="supplier"){
            supplierManageMapper.Updatesuppplier(map.get("IATA"),map.get("ICMO"),map.get("name"),map.get("country"),map.get("area"),map.get("airline"),map.get("AirLineAlliance"),map.get("fleetsNum"),map.get("navigableCity"),map.get("discription"),map.get("wiki"),map.get("website"));
        }
    }

    @PostMapping("api/manage/")
    @ResponseBody
    public void addUser(@RequestBody Map<String ,String>map,@RequestParam(value="role",required =false,defaultValue ="")String role) {
        if (role == "user") {
            userMangerMapper.addUser("tablename", map.get("email"), map.get("password"), map.get("nickname"));
        }if (role=="supplier"){
            supplierManageMapper.addSupplier(map.get("IATA"),map.get("ICAO"),map.get("name"),map.get("country"),map.get("area"),map.get("airline"),map.get("AirLineAlliance"),map.get("fleetsNum"),map.get("navigableCity"),map.get("discription"),map.get("wiki"),map.get("website"));
        }
    }

    @GetMapping("api/manage/")
    @ResponseBody
    public List<Map<String,String>> find(@RequestBody Map<String,String> map,@RequestParam(value="role",required =false,defaultValue ="")String role) {
        if (role == "user") {
            if (map.get("user_id") != null) {
                String user_id = map.get("user_id");
                return userMangerMapper.findUser("SELECT *", "tablename", "user_id='" + user_id + "'");
            } else {
                return null;
            }
        }
        if (role=="supplier"){
            if (map.get("user_id") != null) {
                String supplier_id = map.get("supplier_id");
                return userMangerMapper.findUser("SELECT *", "tablename", "supplier_id='" + supplier_id + "'");
            } else {
                return null;
            }
        }
        return null;
    }

}
