package com.lj.scitemdemo.service.cs2db.impl;

import com.lj.scitemdemo.common.DBTypeEnum;
import com.lj.scitemdemo.common.DataSourceSwitch;
import com.lj.scitemdemo.mapper.cs2db.LjProductMapper;
import com.lj.scitemdemo.service.cs2db.ILjProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LjProductServiceImpl implements ILjProductService {

    @Autowired
    private LjProductMapper ljProductMapper;

    /**
     * 根据商品sku获取商品名称
     * @param sku
     * @return
     */
    @Override
    @DataSourceSwitch(DBTypeEnum.cs2db)
    public String getProductName(String sku) {
        return ljProductMapper.getProductName(sku);
    }
}
