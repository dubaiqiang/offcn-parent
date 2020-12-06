package com.offcn.dycommon.enums;

//响应状态码枚举类
public enum ResponseCodeEnume {
    SUCCESS(0, "操作成功"),
    FAIL(1, "服务器 异常"),
    NOT_FOUND(404, "资源未找到"),
    NOT_AUTHED(403, "无权限，访问拒绝"),
    PARAM_INVAILD(400, "提交参数非法");


    private ResponseCodeEnume(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    //注意：  枚举类中的set get方法不使用Lombok
    private Integer code;
    private String msg;

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
}
