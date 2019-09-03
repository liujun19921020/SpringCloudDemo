package com.lj.scelasticsearchdemo.controller;

import com.alibaba.fastjson.JSONObject;
import com.lj.commonshttp.response.Code;
import com.lj.commonshttp.response.ResponseMsg;
import com.lj.scelasticsearchdemo.common.vo.PageMap;
import com.lj.scelasticsearchdemo.service.EsDemoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * ES 案例
 */
@RestController
@RequestMapping("/esDemo")
public class EsDemoController {

    private static Logger logger = LoggerFactory.getLogger(EsDemoController.class);

    @Autowired
    private EsDemoService esDemoService;

    /**
     * 查询es案例信息 （假设条件为类似sql:select order_id from sku=xx and site=xx and (order_status=1 or order_status = 2)）
     * @param jsonObject
     * @author Liujun
     * @return
     */
    @PostMapping("/selectEsDemoInfo")
    public ResponseMsg selectEsDemoInfo(@RequestBody JSONObject jsonObject) {
        try {
            Integer page = jsonObject.getInteger("page");
            Integer size = jsonObject.getInteger("size");
            String sku = jsonObject.getString("sku");
            String site = jsonObject.getString("site");

            Pageable pageable = PageRequest.of(page == null ? 0 : page-1, size == null ? 50 : size);//分页机制
            PageMap pageMap = esDemoService.selectEsDemoInfo(pageable, sku, site);
            return new ResponseMsg(Code.SUCCESS, pageMap, "根据条件查询ES案例信息成功！");
        } catch (Exception e) {
            logger.error("EsDemoController.selectEsDemoInfo 异常", e);
            return new ResponseMsg(Code.FAIL, null, "根据条件查询ES案例信息失败！"+e.getMessage());
        }
    }

    /**
     * 添加es案例信息 （数据来源为多库时 可以通到java代码实现数据插入；数据来源为同一机器时，建议通过sql+conf的形式导入数据）
     * @param pushMapList
     * @author Liujun
     * @return
     */
    @PostMapping("/saveEsDemoInfo")
    public ResponseMsg saveEsDemoInfo(@RequestBody List<Map<String, Object>> pushMapList){
        try{
            Integer saveNum = esDemoService.saveEsDemoInfo(pushMapList);
            return new ResponseMsg(Code.SUCCESS, saveNum,"添加ES案例信息成功！");
        } catch (Exception e){
            logger.error("EsDemoController.saveEsDemoInfo 异常", e);
            return new ResponseMsg(Code.FAIL,null, "添加ES案例信息失败！"+e.getMessage());
        }
    }
}
