package com.lj.scitemdemo.mapper.cs2db;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lj.scitemdemo.entity.cs2db.LjProduct;
import org.apache.ibatis.annotations.Param;

/**
 * 商品表 Mapper 接口
 */
public interface LjProductMapper extends BaseMapper<LjProduct> {

    /**
     * 根据商品sku获取商品名称
     * @param sku
     * @return
     */
    String getProductName(@Param("sku") String sku);
}
