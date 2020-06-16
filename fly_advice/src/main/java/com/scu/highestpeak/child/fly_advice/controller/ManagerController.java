package com.scu.highestpeak.child.fly_advice.controller;



import com.scu.highestpeak.child.fly_advice.orm.ManageMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @author gtrong
 */
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/manage")
public class ManagerController {
    private String user="user";
    private String supplier="supplier";

    private String userIdParam="user_id";
    private String supplierIdParam="supplier_id";
    @Autowired
    private ManageMapper manageMapper;

    @DeleteMapping("/")
    @ResponseBody
    public void delete(@RequestBody Map<String,String> map,@RequestParam(value="role",required =false,defaultValue ="")String role){
        if (user.equals(role)) {
            manageMapper.deleteUser( Integer.parseInt(map.get(userIdParam)));
        }if (supplier.equals(role)){
            manageMapper.deleteSupplier(Integer.parseInt(map.get(userIdParam)));
        }

    }

    @PutMapping("/")
    @ResponseBody
    public void update(@RequestBody Map<String ,String> map,@RequestParam(value="role",required =false,defaultValue ="")String role) {
        if (user.equals(role)) {
            manageMapper.updateUser(Integer.parseInt(map.get("user_id")), map.get("email"), map.get("password"),0);
        }if(supplier.equals(role)) {
            manageMapper.updateSupplier(map.get("name"), map.get("email"), Integer.parseInt(map.get(supplierIdParam)));
        }
    }

    @PostMapping("/")
    @ResponseBody
    public void addUser(@RequestBody Map<String ,String>map,@RequestParam(value="role",required =false,defaultValue ="")String role) {
        if (user.equals(role)) {
            manageMapper.addUser(map.get("email"), map.get("password"), map.get("nickname"));
        }if (supplier.equals(role)){
            manageMapper.addSupplier(Integer.parseInt(map.get(supplierIdParam)),map.get("name"),1);
        }
    }

    @GetMapping("/")
    @ResponseBody
    public List<Map<String,String>> find(@RequestBody Map<String,String> map,@RequestParam(value="role",required =false,defaultValue ="")String role) {
        if (user.equals(role)) {
            if (map.get(userIdParam) != null) {
                String userId = map.get(userIdParam);
                return manageMapper.find("SELECT *", "user", "id='" + userId + "'");
            } else {
                return null;
            }
        }
        if (supplier.equals(role)){
            if (map.get(userIdParam) != null) {
                String supplierId = map.get(supplierIdParam);
                return manageMapper.find("SELECT *", "supplier", "id='" + supplierId + "'");
            } else {
                return null;
            }
        }
        return null;
    }

    @GetMapping("/history/user/")
    @ResponseBody
    public List<Map<String,String>>get(@RequestBody Map<String,String> map){
        return manageMapper.get(Integer.parseInt(map.get("id")));
    }

    @PostMapping("/history/user/")
    @ResponseBody
    public int addHistory(@RequestBody Map<String,String>map){
        return manageMapper.addHistory(Integer.parseInt(map.get("id")),Integer.parseInt(map.get("plan_id")),
                Integer.parseInt(map.get("plan_order")),map.get("cabin_class"),Integer.parseInt(map.get("user_id")),
                Integer.parseInt(map.get("flight_id")),map.get("flight_type"),Integer.parseInt(map.get("supplier_id")),
                Integer.parseInt(map.get("buy_date")));
    }

    @DeleteMapping("/history/user/")
    @ResponseBody
    public int deleteHistory(@RequestBody Map<String,String> map){
        return manageMapper.deleteHistory(Integer.parseInt(map.get("id")));
    }

    @GetMapping("/history/flight/")
    @ResponseBody
    public List<Map<String,String>>getFlight(@RequestBody Map<String,String> map){
        return manageMapper.getFlight(Integer.parseInt(map.get("id")));
    }

    @PostMapping("/history/flight/")
    @ResponseBody
    public int addFlight(@RequestBody Map<String,String>map){
        return manageMapper.addFlight(Integer.parseInt("id"),map.get("flight_no"),map.get("aircraft_model"),
                                        Integer.parseInt("start_time"),Integer.parseInt("end_time"),
                                        Integer.parseInt("source_airport"), Integer.parseInt(map.get("destination_airport")),
                                        Double.parseDouble(map.get("price")),Integer.parseInt(map.get("stops")));
    }

    @DeleteMapping("/history/flight/")
    @ResponseBody
    public int deleteFlight(@RequestBody Map<String,String> map){
        return manageMapper.deleteFlight(Integer.parseInt(map.get("id")));
    }



}
