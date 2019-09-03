package com.lj.commonsutils.excel;

public class ExcelLog {
    private Integer rowNum;
    private Object object;
    private String log;

    public Integer getRowNum() {
        return rowNum;
    }

    public void setRowNum(Integer rowNum) {
        this.rowNum = rowNum;
    }

    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }

    public String getLog() {
        return log;
    }

    public ExcelLog(Object object, String log) {
        super();
        this.object = object;
        this.log = log;
    }

    public ExcelLog(Object object, String log, Integer rowNum) {
        super();
        this.rowNum = rowNum;
        this.object = object;
        this.log = log;
    }

    public void setLog(String log) {
        this.log = log;
    }

}
