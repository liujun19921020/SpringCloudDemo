package com.lj.scelasticsearchdemo.common.vo;

import java.io.Serializable;
import java.util.List;

public class PageMap<T> implements Serializable {
    private Integer pageSize;//每页显示条数
    private Integer currentNumber;//当页显示条数
    private Integer currentPage;//当前页
    private Long totalPage;//总页数
    private Long totalNumber;//总条数
    private List<T> ObjectList;//数据对象

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Integer getCurrentNumber() {
        return currentNumber;
    }

    public void setCurrentNumber(Integer currentNumber) {
        this.currentNumber = currentNumber;
    }

    public Integer getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(Integer currentPage) {
        this.currentPage = currentPage;
    }

    /**
     * 计算获取总页数
     * @return
     */
    public Long getTotalPage() {
        if(null == totalPage && totalNumber > 0){
            if(totalNumber % pageSize == 0){
                this.totalPage = totalNumber / pageSize;
            }else {
                this.totalPage = totalNumber / pageSize + 1;
            }
        }
        return totalPage;
    }

    public void setTotalPage(Long totalPage) {
        this.totalPage = totalPage;
    }

    public Long getTotalNumber() {
        return totalNumber;
    }

    public void setTotalNumber(Long totalNumber) {
        this.totalNumber = totalNumber;
    }

    public List<T> getObjectList() {
        return ObjectList;
    }

    public void setObjectList(List<T> objectList) {
        ObjectList = objectList;
    }
}
