package com.lj.scmusteritemdemo.common;

/**
 *  数据源枚举类型
 */
public enum DBTypeEnum {
    cs1db("cs1-db"), cs2db("cs2-db");
    private String value;

    DBTypeEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
