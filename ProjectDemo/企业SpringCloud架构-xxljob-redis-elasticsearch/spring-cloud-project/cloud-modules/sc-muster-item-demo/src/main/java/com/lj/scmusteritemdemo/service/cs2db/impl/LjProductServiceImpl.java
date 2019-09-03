package com.lj.scmusteritemdemo.service.cs2db.impl;

import com.lj.scmusteritemdemo.mapper.cs2db.LjProductMapper;
import com.lj.scmusteritemdemo.service.cs2db.ILjProductService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class LjProductServiceImpl implements ILjProductService {

    @Resource
    private LjProductMapper ljProductMapper;

    /**
     * 根据商品sku获取商品名称
     * @param sku
     * @return
     */
    @Override
    public String getProductName(String sku) {
        return ljProductMapper.getProductName(sku);
    }
}
