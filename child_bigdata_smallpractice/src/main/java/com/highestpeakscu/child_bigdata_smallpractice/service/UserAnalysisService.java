package com.highestpeakscu.child_bigdata_smallpractice.service;

import com.highestpeakscu.child_bigdata_smallpractice.dao.mybatis_mapper.UserAnalysisMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author highestpeak
 */
@Service
public class UserAnalysisService {
    @Autowired
    private UserAnalysisMapper userAnalysisMapper;

    /**
     * active
     */
    @FunctionalInterface
    public interface SituationJudge {
        boolean isFirst();
    }

    /**
     * 模板方法 设计模式
     * 根据传递参数的数目，确定选择的查询sql mapper
     */

    public abstract class AbstractDiffSituationActive{

        public AbstractDiffSituationActive() {
        }

        public Object query(
                SituationJudge situationOneJudge,Object one,
                SituationJudge situationTwoJudge, Object another
        ){
            if (situationOneJudge.isFirst()){
                if (situationTwoJudge.isFirst()){
                    return querySituationThird(one,another);
                }
                return querySituationFirst(one);
            }
            if (situationTwoJudge.isFirst()){
                return querySituationSecond(another);
            }
            return null;
        }

        abstract Object querySituationFirst(Object queryValue);

        abstract Object querySituationSecond(Object queryValue);
        
        abstract Object querySituationThird(Object queryValueOne,Object queryValueTwo);
    }

    public class ActiveUser extends AbstractDiffSituationActive{

        private static final String TABLE="dws_user_active_day";

        @Override
        Object querySituationFirst(Object queryValue) {
//            return userAnalysisMapper.userActiveById((Integer) queryValue);
            return userAnalysisMapper.selectTableWherePattern("dt col1",TABLE,"user_id="+queryValue);
        }

        @Override
        Object querySituationSecond(Object queryValue) {
//            return userAnalysisMapper.userActiveByDate((String)queryValue);
            return userAnalysisMapper.selectTableWherePattern("user_id col1",TABLE,"dt='"+queryValue+"'");
        }

        @Override
        Object querySituationThird(Object queryValueOne, Object queryValueTwo) {
            return null;
        }

    }
    private static ActiveUser activeUser;
    public ActiveUser getActiveUserInstance(){
        if (activeUser==null){
            activeUser=new ActiveUser();
        }
        return activeUser;
    }

    public class ActiveContinue3wk extends AbstractDiffSituationActive{

        private static final String TABLE="dws_user_wk_active_continue3";

        @Override
        Object querySituationFirst(Object queryValue) {
            return userAnalysisMapper.selectTableWherePattern("wk col1",TABLE,"user_id="+queryValue);
        }

        @Override
        Object querySituationSecond(Object queryValue) {
            return userAnalysisMapper.selectTableWherePattern("user_id col1",TABLE,"wk="+queryValue);
        }

        @Override
        Object querySituationThird(Object queryValueOne, Object queryValueTwo) {
            return null;
        }
    }
    private static ActiveContinue3wk activeContinue3wk;
    public ActiveContinue3wk getActiveContinue3wkInstance(){
        if (activeContinue3wk==null){
            activeContinue3wk=new ActiveContinue3wk();
        }
        return activeContinue3wk;
    }

    public class Active7day3continue extends AbstractDiffSituationActive{

        private static final String TABLE="dws_user_active_7day3continue";

        @Override
        Object querySituationFirst(Object queryValue) {
            return userAnalysisMapper.selectTableWherePattern("dt col1",TABLE,"user_id="+queryValue);
        }

        @Override
        Object querySituationSecond(Object queryValue) {
            return userAnalysisMapper.selectTableWherePattern("user_id col1",TABLE,"dt='"+queryValue+"'");
        }

        @Override
        Object querySituationThird(Object queryValueOne, Object queryValueTwo) {
            return null;
        }
    }
    private static Active7day3continue active7day3continue;
    public Active7day3continue getActive7day3continueInstance(){
        if (active7day3continue==null){
            active7day3continue=new Active7day3continue();
        }
        return active7day3continue;
    }

    public class UserKeepRate extends AbstractDiffSituationActive{

        private static final String TABLE="dws_user_keep_rate";

        @Override
        Object querySituationFirst(Object queryValue) {
            return userAnalysisMapper.selectTableWherePattern("dt col1",TABLE,"before_gap="+queryValue);
        }

        @Override
        Object querySituationSecond(Object queryValue) {
            return userAnalysisMapper.selectTableWherePattern("before_gap col1,rate col2",TABLE,"dt='"+queryValue+"'");
        }

        @Override
        Object querySituationThird(Object queryValueOne, Object queryValueTwo) {
            return userAnalysisMapper.selectTableWherePattern("rate col1","dws_user_keep_rate","dt='"+queryValueTwo+"' and before_gap="+queryValueOne);
        }
    }
    private static UserKeepRate userKeepRate;
    public UserKeepRate getUserKeepRateInstance(){
        if (userKeepRate==null){
            userKeepRate=new UserKeepRate();
        }
        return userKeepRate;
    }

}
