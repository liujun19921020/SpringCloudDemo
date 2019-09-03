package com.lj.scmusteritemdemo.common;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

/**
 *  多数据源的选择
 */
public class DynamicDataSource extends AbstractRoutingDataSource {
    /**
     * 取得当前使用哪个数据源
     * @return
     */
    @Override
    protected Object determineCurrentLookupKey() {
        return DbContextHolder.getDbType();
    }
}
