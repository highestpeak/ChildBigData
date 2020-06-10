package com.highestpeakscu.child_bigdata_smallpractice.controller;

import com.highestpeakscu.child_bigdata_smallpractice.dao.mybatis_mapper.UserAnalysisMapper;
import com.highestpeakscu.child_bigdata_smallpractice.domain.DO.AnalysisDO;
import com.highestpeakscu.child_bigdata_smallpractice.service.UserAnalysisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @author highestpeak
 */
@Controller
@RequestMapping("/api/user/")
public class UserController {
    @Autowired
    private UserAnalysisService userAnalysisService;

    @Autowired
    private UserAnalysisMapper userAnalysisMapper;

    @GetMapping("basicAnalysis")
    @ResponseBody
    public List<AnalysisDO> basicAnalysis() {
        return userAnalysisMapper.basicAnalysis();
    }

    private static final String INVALID_QUERY_MSG="invalid query";

    private boolean isDefaultVal(Object value){
        if (value==null){
            return true;
        }
        switch (value.getClass().getSimpleName()){
            case "Integer":
                return (int)value==-1;
            case "String":
                return false;
            default:
                break;
        }
        return true;
    }

    /**
     * one and two param must send one but not both
     * todo: 更改可以替换策略的invalid值检测
     * @param one first param
     * @param two second param
     * @return whether valid or not
     */
    private boolean isInvalidQuery(
            Object one,Object two
    ){
        boolean queryByOne = isDefaultVal(one);
        boolean queryByTwo = isDefaultVal(two);
        return (!queryByOne && !queryByTwo);
    }

    @GetMapping("active")
    @ResponseBody
    public Object activeUser(
            @RequestParam(value = "user_id",required = false,defaultValue = "-1") int id,
            @RequestParam (value = "dt",required = false) String date
    ) {
        if (isInvalidQuery(id,date)){
            return new ArrayList<String>(){{
                add(INVALID_QUERY_MSG);
            }};
        }

        UserAnalysisService.ActiveUser activeUser= userAnalysisService.getActiveUserInstance();
        return activeUser.query(()->id!=-1,id,()->date!=null,date);
    }

    @GetMapping("active/continue3wk")
    @ResponseBody
    public Object activeContinue3wk(
            @RequestParam(value = "user_id",required = false,defaultValue = "-1") int id,
            @RequestParam(value = "wk",required = false,defaultValue = "-1") int wk
    ) {
        if (isInvalidQuery(id,wk)){
            return new ArrayList<String>(){{
                add(INVALID_QUERY_MSG);
            }};
        }

        UserAnalysisService.ActiveContinue3wk activeContinue3wk= userAnalysisService.getActiveContinue3wkInstance();
        return activeContinue3wk.query(()->id!=-1,id,()->wk!=-1,wk);
    }

    @GetMapping("active/7day3continue")
    @ResponseBody
    public Object active7day3continue(
            @RequestParam(value = "user_id",required = false,defaultValue = "-1") int id,
            @RequestParam(value = "dt",required = false) String date
    ) {
        if (isInvalidQuery(id,date)){
            return new ArrayList<String>(){{
                add(INVALID_QUERY_MSG);
            }};
        }

        UserAnalysisService.Active7day3continue active7day3continue= userAnalysisService.getActive7day3continueInstance();
        return active7day3continue.query(()->id!=-1,id,()->date!=null,date);
    }


    @GetMapping("count/active")
    @ResponseBody
    public Object activeCount(
            @RequestParam(value = "type") int type,
            @RequestParam(value = "certain",required = false) String certain
    ) {
        return isDefaultVal(certain)?
                userAnalysisMapper.selectTableWherePattern("cnt col1,certain_dt col2","dws_user_active_count","type="+type):
                userAnalysisMapper.selectTableWherePattern("cnt col1","dws_user_active_count","type="+type+" and certain_dt='"+certain+"'");
    }

    @GetMapping("count/new")
    @ResponseBody
    public Object newCount(
            @RequestParam(value = "type") int type,
            @RequestParam(value = "certain",required = false) String certain
    ) {
        return isDefaultVal(certain)?
                userAnalysisMapper.selectTableWherePattern("cnt col1,certain_dt col2","dws_user_new_count","type="+type):
                userAnalysisMapper.selectTableWherePattern("cnt col1","dws_user_new_count","type="+type+" and certain_dt='"+certain+"'");
    }

    private static final int SLIENCE_TYPE = 0;
    private static final int LOSS_TYPE = 1;
    private static final int SLIENCE_LOSS_TYPE = 2;
    @GetMapping("count/loss_slience")
    @ResponseBody
    public Object lossCount(
            @RequestParam(value = "type") int type,
            @RequestParam(value = "dt",required = false) String date
            ) {
        StringBuilder diffSelect=new StringBuilder();

        switch (type){
            case SLIENCE_TYPE:
                diffSelect.append("slience_cnt col1");
                break;
            case LOSS_TYPE:
                diffSelect.append("loss_cnt col1");
                break;
            case SLIENCE_LOSS_TYPE:
                diffSelect.append("slience_cnt col1,loss_cnt col2");
                break;
            default:
                break;
        }

        if (date==null){
            diffSelect.append(",dt");
            return userAnalysisMapper.selectTableWherePattern(diffSelect.toString(),"dws_slience_loss_count","dt='"+date+"'");
        }
        return userAnalysisMapper.selectTableWherePattern(diffSelect.toString(),"dws_slience_loss_count","true");

    }

    private static final int CART_TYPE = 0;
    private static final int BUY_TYPE = 1;
    private static final int CART_BUY_TYPE = 2;
    @GetMapping("rate/visitTo")
    @ResponseBody
    public Object visitToCartBuyRate(
            @RequestParam(value = "type") int type,
            @RequestParam(value = "item_id",required = false,defaultValue = "-1") int id
    ) {
        String diffWhereCol;

        switch (type){
            case CART_TYPE:
                diffWhereCol="rate_type=0";
                break;
            case BUY_TYPE:
                diffWhereCol="rate_type=1";
                break;
            case CART_BUY_TYPE:
                diffWhereCol="rate_type=0 or rate_type=1";
                break;
            default:
                diffWhereCol="true";
                break;
        }

        if (isDefaultVal(id)){
            return userAnalysisMapper.selectTableWherePattern("item_id col1,rate_value col2","dws_item_visit_cart_buy_rate",diffWhereCol);
        }
        return userAnalysisMapper.selectTableWherePattern("rate_type col1,rate_value col2","dws_item_visit_cart_buy_rate","item_id="+id);
    }

    @GetMapping("rate/keep")
    @ResponseBody
    public Object keepRate(
            @RequestParam(value = "before_gap",required = false,defaultValue = "-1") int beforeGap,
            @RequestParam(value = "dt",required = false) String date
    ) {
//        if (isInvalidQuery(beforeGap,date)){
//            return INVALID_QUERY_MSG;
//        }

        UserAnalysisService.UserKeepRate userKeepRate= userAnalysisService.getUserKeepRateInstance();
        return userKeepRate.query(()->beforeGap!=-1,beforeGap,()->date!=null,date);
    }



}
