package com.highestpeakscu.child_bigdata_smallpractice.dao.mybatis_mapper;

import com.highestpeakscu.child_bigdata_smallpractice.domain.DO.AnalysisDO;
import com.highestpeakscu.child_bigdata_smallpractice.domain.DO.MostTwoSelectDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

/**
 * @author highestpeak
 */
@Mapper
@Repository
public interface UserAnalysisMapper {
    @Select({"SELECT analy_key,analy_value from dws_simple_analysis"})
    List<AnalysisDO> basicAnalysis();

    /**
     * idea search pattern "(selectTableWherePattern.*?[a-zA-Z])","
     * replace pattern "$1 col1","
     * */
    @Select({"SELECT ${selectCol} from ${table} where ${whereCase}"})
    List<MostTwoSelectDO> selectTableWherePattern(String selectCol, String table, String whereCase);

//    /*
//    * active date
//    * */
//
//    @Select({"SELECT user_id from dws_user_active_day where dt=#{date}"})
//    List<String> userActiveByDate(String date);
//
//    @Select({"SELECT dt from dws_user_active_day where user_id=#{id}"})
//    List<String> userActiveById(int id);
//
//    /*
//     * continue3wk
//     * */
//
//    @Select({"SELECT wk from dws_user_wk_active_continue3 where user_id=#{id}"})
//    List<String> userContinue3wkById(int id);
//
//    @Select({"SELECT user_id from dws_user_wk_active_continue3 where wk=#{wk}"})
//    List<String> userContinue3wkByDate(int wk);
//
//    /*
//     * 7day3continue
//     * */
//
//    @Select({"SELECT dt from dws_user_active_7day3continue where user_id=#{id}"})
//    List<String> user7day3continueById(int id);
//
//    @Select({"SELECT user_id from dws_user_active_7day3continue where dt=#{dt}"})
//    List<String> user7day3continueByDt(String dt);
//
//    /**
//     *
//     */
//
//    @Select({"SELECT cnt from dws_user_active_count where type=#{type} and certain_dt=#{date}"})
//    Integer userActiveCountByDate(int type,String date);
//
//    @Select({"SELECT cnt,certain_dt from dws_user_active_count where type=#{type}"})
//    List<Object> userActiveCountByType(int type);
//
//    /**
//     *
//     */
//    @Select({"SELECT cnt from dws_user_new_count where type=#{type} and certain_dt=#{date}"})
//    Integer userNewCountByDate(int type,String date);
//
//    @Select({"SELECT cnt,certain_dt from dws_user_new_count where type=#{type}"})
//    List<Object> userNewCountByType(int type);
//
//    /**
//     *
//     */
//    @Select({"SELECT item_id,rate_value from dws_item_visit_cart_buy_rate ${diffWhereCol}"})
//    Object userVisitToByType(String diffWhereCol);
//
//    @Select({"SELECT rate_type,rate_value from dws_user_new_count where item_id=#{itemId}"})
//    Object userVisitToById(int itemId);
//
//    /**
//     *
//     */
//    @Select({"SELECT ${diffSelect} from dws_slience_loss_count where dt=#{date}"})
//    Object userSlienceLossCountByDate(String diffSelect,String date);
//
//    @Select({"SELECT ${diffSelect} from dws_slience_loss_count"})
//    Object userSlienceLossCount(String diffSelect);
//
//    /**
//     *
//     */
//    @Select({"SELECT rate from dws_user_keep_rate where dt=#{date} and before_gap=#{beforeGap}"})
//    Object userKeepRate(String date,String beforeGap);
//
//    @Select({"SELECT ${selectCol} from dws_user_keep_rate where ${whereCol}=#{whereVal}"})
//    Object userKeepRate(String selectCol,String whereCol,Object whereVal);
}
