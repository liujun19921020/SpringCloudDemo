package com.lj.scelasticsearchdemo.common;

import com.lj.scelasticsearchdemo.common.vo.PageMap;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * 将Page对象转为自己想要的样子
 */
public class EsPageTool<T>  {
    public static <T> PageMap getPageMap(Page<T> resutlPage, Pageable pageable){
        PageMap pageMap = new PageMap();
        pageMap.setPageSize(pageable.getPageSize());//每页显示条数
        pageMap.setCurrentNumber(resutlPage.getNumberOfElements());//当页显示条数
        pageMap.setCurrentPage(pageable.getPageNumber()+1);//当前页
        pageMap.setTotalNumber(resutlPage.getTotalElements());//总条数
        pageMap.setObjectList(resutlPage.getContent());//数据对象
        return pageMap;
    }
}
