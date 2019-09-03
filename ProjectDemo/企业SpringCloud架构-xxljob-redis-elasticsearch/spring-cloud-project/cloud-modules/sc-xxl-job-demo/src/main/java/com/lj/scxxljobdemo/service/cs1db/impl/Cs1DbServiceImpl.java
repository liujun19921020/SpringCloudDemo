package com.lj.scxxljobdemo.service.cs1db.impl;

import com.lj.scxxljobdemo.Util.DateStringUtil;
import com.lj.scxxljobdemo.mapper.cs1db.Cs1DbMapper;
import com.lj.scxxljobdemo.service.cs1db.ICs1DbService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * cs1db库 表数据备份 Service 实现
 */
@Service
public class Cs1DbServiceImpl implements ICs1DbService {

    @Resource
    private Cs1DbMapper cs1DbMapper;

    @Transactional
    @Override
    public void tableOperation(String tableName ) {
        cs1DbMapper.createTableName( tableName, DateStringUtil.getDateString());//创建当天的备份表
        cs1DbMapper.addTableData( tableName, DateStringUtil.getDateString());//向备份表里备份数据
        cs1DbMapper.deleteTableName( tableName,DateStringUtil.getDelDateString());//删除2天前的备份表
    }
}
