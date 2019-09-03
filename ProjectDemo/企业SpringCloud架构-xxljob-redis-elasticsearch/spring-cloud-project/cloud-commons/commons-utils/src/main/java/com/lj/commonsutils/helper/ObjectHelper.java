package com.lj.commonsutils.helper;

import java.lang.reflect.Field;
import java.util.List;

/**
 * Object  帮助API
 */
public class ObjectHelper {


    /**
     * 验证list<object>是否有空值
     * 返回false 说明有空值
     * @param objectList
     * @return
     */
    public static boolean isAllListHasNullVlue(List<Object> objectList){
        boolean flag = true;
        if(objectList!=null && objectList.size()>0){
            for(Object o:objectList){
                flag = isAllFieldHasNullValue(o);
                if(!flag){
                    flag = false;
                    break;
                }
            }
        }
        return flag;
    }

    /***
     * 对象数据效验
     * 返回false 说明有空值
     * @param obj
     * @return
     */
    public static boolean isAllFieldHasNullValue(Object obj) {
        boolean flag = true;
        try {
            /**
             * 得到类对象
             */
            Class stuCla = (Class) obj.getClass();
            /**
             * 获取声明字段
             */
             Field[] fs = stuCla.getDeclaredFields();
            /**
             * 检测
             */
            for (Field f : fs) {
                /**
                 * 可访问
                 */
                f.setAccessible(true);
                Object val = f.get(obj);
                if (StringHelper.isBlank(val.toString())) {
                    flag = false;
                    break;
                }
            }
        } catch (Exception e) {
            return false;
        }
        return flag;
    }

}
