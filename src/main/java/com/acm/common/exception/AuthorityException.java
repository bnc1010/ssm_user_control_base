package com.acm.common.exception;

public class AuthorityException extends RuntimeException {

    private String msg;
    public AuthorityException(String msg) {
        super();
        this.msg = msg;
    }

    public String getMessage() {
        return msg;
    }

    public void setMessage(String msg) {
        this.msg = msg;
    }
}
