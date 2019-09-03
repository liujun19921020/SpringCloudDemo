package com.lj.commonsutils.helper;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * String 帮助API
 */
public class StringHelper {
    /**
     * 判断字符串是否有内容（无内容返回true）
     * @param str
     * @return
     */
    public static boolean isBlank(String str){
        if(str == null){
            return true;
        }
        return str.length() == 0;
    }

    /**
     *判断字符串是否有内容（有内容返回true）
     * @param str
     * @return
     */
    public static boolean isNotBlank(String str){
        return !isBlank(str);
    }

    /**
     * 检查是否为数字
     * @param str
     * @return
     */
    public static boolean isNumeric(String str){
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNum = pattern.matcher(str);
        if( !isNum.matches() ){
            return false;
        }
        return true;
    }

}
