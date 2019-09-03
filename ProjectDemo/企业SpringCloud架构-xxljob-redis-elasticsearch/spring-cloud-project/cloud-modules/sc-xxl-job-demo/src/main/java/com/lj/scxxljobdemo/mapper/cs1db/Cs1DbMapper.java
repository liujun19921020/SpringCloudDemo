package com.lj.scxxljobdemo.mapper.cs1db;

import org.apache.ibatis.annotations.Param;

/**
 *  cs1db库 表数据备份
 */
public interface Cs1DbMapper {

    /**
     * 创建表
     * @param tableName
     * @param dateName
     */
    void createTableName(@Param("tableName") String tableName, @Param("dateName") String dateName);

    /**
     * 同步数据
     * @param tableName
     * @param dateName
     */
    void addTableData(@Param("tableName") String tableName,@Param("dateName") String dateName);

    /**
     * 删除表
     * @param tableName
     * @param dateName
     */
    void deleteTableName(@Param("tableName") String tableName,@Param("dateName") String dateName);
}
