package com.lj.commonsutils.helper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * List 帮助API
 */
public class ListHelper {

    /**
     * 数组转换list
     * @param array
     * @param <E>
     * @return
     */
    public static <E> List<E> toList(E[] array){
        List<E> transferedList = new ArrayList<>();
        Arrays.stream(array).forEach(arr -> transferedList.add(arr));
        return transferedList;
    }
}
