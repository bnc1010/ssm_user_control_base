package com.acm.common.exception;

public class AuthorityException extends RuntimeException {

    private String msg;
    public AuthorityException(String msg) {
        super();
        this.msg = msg;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
