package com.lj.commonsutils.excel;

import com.lj.commonsutils.helper.StringHelper;

import java.util.ArrayList;
import java.util.List;

public class ExcelLogs {
    private Boolean hasError;
    private List<ExcelLog> logList;

    public ExcelLogs() {
        super();
        hasError = false;
    }

    public Boolean getHasError() {
        return hasError;
    }

    public void setHasError(Boolean hasError) {
        this.hasError = hasError;
    }

    public List<ExcelLog> getLogList() {
        return logList;
    }

    public List<ExcelLog> getErrorLogList() {
        List<ExcelLog> errList = new ArrayList<>();
        for (ExcelLog log : this.logList) {
            if (log != null && StringHelper.isNotBlank(log.getLog())) {
                errList.add(log);
            }
        }
        return errList;
    }

    public void setLogList(List<ExcelLog> logList) {
        this.logList = logList;
    }

}
