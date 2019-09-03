package com.lj.scelasticsearchdemo.service;

import com.lj.scelasticsearchdemo.common.vo.PageMap;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

public interface EsDemoService {

    /**
     * 添加es案例信息 （数据来源为多库时 可以通到java代码实现数据插入；数据来源为同一机器时，建议通过sql+conf的形式导入数据）
     * @author Liujun
     * @param pushMapList
     * @return
     */
    Integer saveEsDemoInfo(List<Map<String, Object>> pushMapList);

    /**
     * 查询es案例信息 （假设条件为类似sql:select order_id from sku=xx and site=xx and (order_status=1 or order_status = 2)）
     * @param pageable
     * @param sku
     * @param site
     * @return
     */
    PageMap selectEsDemoInfo(Pageable pageable, String sku, String site);
}
