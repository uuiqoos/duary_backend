package com.ivis.duary.config.response;

import lombok.Data;

/**
 * Response Data
 * @param <T>
 */
@Data
public class Response<T> {
    private final static String SUCCESS_MSG = "OK";

    private String code;
    private int status;
    private T data;
    private boolean error;
    private String message;

    public Response() {
        this.status = 200;
        this.error = false;
        this.message = SUCCESS_MSG;
    }

    public Response(T data) {
        this.code = SUCCESS_MSG;
        this.status = 200;
        this.error = false;
        this.data = data;
        this.message = SUCCESS_MSG;
    }

    public Response(T data, String message) {
        this.status = 200;
        this.error = false;
        this.data = data;
        this.message = message;
    }

    public Response(int status, boolean error, String message) {
        this.error = error;
        this.status = status;
        this.message = message;
    }
    /**
     * 성공 Response
     */
    public static Response<?> SUCCESS = new Response<>();

}

