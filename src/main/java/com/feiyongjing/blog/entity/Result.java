package com.feiyongjing.blog.entity;

public abstract class Result<T> {
    private String status;
    private String msg;
    private T data;

    protected Result(String status, String msg) {
        this.status = status;
        this.msg = msg;
    }

    protected Result(String status, String msg, T data) {
        this.status = status;
        this.msg = msg;
        this.data = data;
    }

    public String getStatus() {
        return status;
    }

    public String getMsg() {
        return msg;
    }

    public Object getData() {
        return data;
    }
}
