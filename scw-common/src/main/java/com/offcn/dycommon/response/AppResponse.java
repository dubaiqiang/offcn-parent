package com.offcn.dycommon.response;

import com.offcn.dycommon.enums.ResponseCodeEnume;

//返回响应的封装类
public class AppResponse<T> {
    private Integer code;
    private String msg;
    private T data;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    /**
     * 返回操作成功的响应方法
     * @param data
     * @param <T>
     * @return
     */
    public static<T> AppResponse<T> ok(T data){
        AppResponse<T> resp = new AppResponse<T>();
        resp.setCode(ResponseCodeEnume.SUCCESS.getCode());
        resp.setMsg(ResponseCodeEnume.SUCCESS.getMsg());
        resp.setData(data);
        return resp;
    }

    /**
     * 返回操作失败的响应方法
     * @param data
     * @param <T>
     * @return
     */
    public static<T> AppResponse<T> fail(T data){
        AppResponse<T> response = new AppResponse<T>();
        response.setCode(ResponseCodeEnume.FAIL.getCode());
        response.setMsg(ResponseCodeEnume.FAIL.getMsg());
        response.setData(data);
        return response;
    }
}
