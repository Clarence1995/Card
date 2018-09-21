package com.tecsun.card.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Result {
    private int stateCode;
    private String msg;
    private Object data;


    public Result(){}
    public Result(int stateCode, String msg) {
        this.stateCode = stateCode;
        this.msg = msg;
    }
    public Result(int stateCode, String msg, Object data) {
        this.stateCode = stateCode;
        this.msg = msg;
        this.data = data;
    }

    public void setData(Object data) {
        this.data = data;
    }
    public void success() {
        this.stateCode = 200;
        this.msg = "处理成功";
    }
    public void success(String msg) {
        this.stateCode = 200;
        this.msg = msg;
    }
    public void success(Object obj) {
        this.stateCode = 200;
        this.msg = "处理成功";
        this.data = obj;
    }

    public void fail() {
        this.stateCode = 400;
        this.msg = "处理失败";
    }
    public void fail(String msg) {
        this.stateCode = 400;
        this.msg = msg;
    }
    public void fail(Object obj) {
        this.stateCode = 400;
        this.msg = "处理失败";
        this.data = obj;
    }
}
