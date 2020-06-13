package com.highestpeakscu.child_bigdata_smallpractice.service;

import java.util.Date;
import java.util.Map;

public abstract class TimeUserAnalysis {
    public static int countActiveUsers(){
        return 0;
    }
    public static int countStores(){
        return 0;
    }
    public static int countBrand(){
        return 0;
    }
    public static int countDeals(){
        return 0;
    }
    public static int countManBuy(){
        return 0;
    }
    public static int countWomanBuy(){
        return 0;
    }

    interface Top10{
        Map<String,Integer> topInfo();
    }
    public class CatTop10 implements  Top10{

        @Override
        public Map<String, Integer> topInfo() {
            return null;
        }
    }

    public static int ProvinceSpendingPower(){
        return 0;
    }

    interface CountUserBuyOnCertainDay{
        int count();
    }
    public class countUserBuyOn_1111 implements  CountUserBuyOnCertainDay{
        @Override
        public int count() {
            return 0;
        }
    }

    public abstract int countActiveUser();
    public abstract int countNewUser();

    interface RetentionRate{
        int rate(Date date);
    }
    public class OnedayBeforeRetentionRate implements  RetentionRate{
        @Override
        public int rate(Date date) {
            return 0;
        }
    }

    public static int countSilenceUsers(Date date){
        return 0;
    }

    public static int countLossUsers(int month){
        return 0;
    }

    public static int countActiveUsersDuringLastThreeWk(int wk){
        return 0;
    }

    public static int countActiveUsersInContinueThreeDayDuringLastWk(int wk){
        return 0;
    }

    interface FillerRatio{
        int rate(Date date);
    }

    public class VisitToBuyRatio implements  FillerRatio{

        @Override
        public int rate(Date date) {
            return 0;
        }
    }
}
