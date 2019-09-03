package com.lj.scxxljobdemo.Util;

import java.util.Calendar;

public class DateStringUtil {

    /**
     * 获取当前添加编号
     * @return
     */
    public static String getDateString(){
        Calendar cal=Calendar.getInstance();
        int date = cal.get(Calendar.DATE);
        if(date<10){
            return "0"+date;
        }
        return String.valueOf(date);
    }


    /**
     * 获取当前删除编号
     * @return
     */
    public static String getDelDateString(){
        Calendar cal=Calendar.getInstance();
        int date = cal.get(Calendar.DATE);
        if(date <= 2){
            cal.set(Calendar.DAY_OF_MONTH, 1);
            cal.add(Calendar.DAY_OF_MONTH, -1);
            if(date == 1){
                return String.valueOf(cal.get(Calendar.DATE) - 1);
            }
            return String.valueOf(cal.get(Calendar.DAY_OF_MONTH));
        }
        if(date <= 11){
            return "0"+(cal.get(Calendar.DATE) -2);
        }
        return String.valueOf(cal.get(Calendar.DATE) -2);

    }

    public static void main(String[] args) {
        System.out.println("添加编号："+getDateString()+";删除编号"+getDelDateString());
    }

}
