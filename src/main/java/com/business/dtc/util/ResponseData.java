package com.business.dtc.util;

/**
 * @创建人: xudy
 * @创建日期: 2018/01/24 18:44
 * @类描述:
 */
public class ResponseData {
    public ResponseData(){

    }

    public ResponseData(int code, String msg, Object data){
        this.code = code;
        this.msg = msg;
        this.data = data;
    }
    public ResponseData(int code, Object data){
        this.code = code;
        this.data = data;
    }
    public ResponseData(int code, String msg){
        this.code = code;
        this.msg = msg;
    }

    public static ResponseData buildResponseData(int code, String msg, Object data){
        return new ResponseData(code,msg,data);
    }

    public static ResponseData buildResponseData(int code,Object data){
        return new ResponseData(code,data);
    }

    private int code;

    private String msg;

    private Object data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}