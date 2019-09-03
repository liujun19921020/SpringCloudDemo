package com.lj.commonshttp.response;

/**
 * 接口返回对象
 */
public class ResponseMsg<T> {

    private int code = Code.SUCCESS;

    private T data;

    private String msg;

    public ResponseMsg() {
        this.code = Code.SUCCESS;
        this.msg = Code.SUCCESS_MSG;
    }

    public ResponseMsg(T data) {
        this.data = data;
        this.code = Code.SUCCESS;
        this.msg = Code.SUCCESS_MSG;
    }

    public ResponseMsg(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public ResponseMsg(int code, T data, String msg) {
        this.code = code;
        this.data = data;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

}

