package com.lj.scmusteritemdemo.controller;

import com.alibaba.fastjson.JSONObject;
import com.lj.commonshttp.response.Code;
import com.lj.commonshttp.response.ResponseMsg;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@RestController
@RequestMapping("/redisDemo")
public class RedisDemoController {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource
    private RedisTemplate redisTemplate;

    private static final String REDIS_DEMO_CALL = "REDIS_DEMO_CALL";

    /**
     * 根据条件查询订单详细信息列表（分页）
     * @param data
     * @return
     */
    @PostMapping("/getRedisCall")
    public ResponseMsg getRedisCall(@RequestBody JSONObject data){
        try {
            String nowDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));//当前时间

            // 获取上次调用时间
            Object callDateObj = redisTemplate.opsForValue().get(REDIS_DEMO_CALL);
            String lastDateObj = callDateObj == null ? "未调用" : callDateObj.toString();

            // 更新时间
            redisTemplate.opsForValue().set(REDIS_DEMO_CALL, nowDate);

            return new ResponseMsg(Code.SUCCESS, lastDateObj,"查询上次调用时间成功！");
        } catch (Exception e) {
            logger.error("查询上次调用时间失败！", e);
            return new ResponseMsg<>(Code.FAIL, null, "查询上次调用时间失败==》" + e.getMessage());
        }
    }

}
