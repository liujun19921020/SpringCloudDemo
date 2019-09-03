package com.lj.scxxljobdemo.jobhandler;

import com.lj.scxxljobdemo.service.cs1db.ICs1DbService;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.CountDownLatch;

/**
 * cs1db库 表数据备份
 */
@JobHandler(value = "cs1DbJobHandler")
@Component
public class Cs1DbJobHandler extends IJobHandler {

    @Autowired
    ICs1DbService cs1DbService;

    @Override
    public ReturnT<String> execute(String s){
        try {
            //要同步的表名
            String[] tableNameList = new String[]{"order_amazon","order_amazon_detail"};

            CountDownLatch latch = new CountDownLatch(tableNameList.length);//设置与表相同的线程计数器，同时备份表
            for(String tableName : tableNameList){
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            cs1DbService.tableOperation(tableName);
                        } catch (Exception e) {
                            e.printStackTrace();
                        } finally {
                            latch.countDown();
                        }
                    }
                }).start();
            }
            latch.await();
            return SUCCESS;
        } catch (InterruptedException e) {
            e.printStackTrace();
            return FAIL;
        }

    }
}
