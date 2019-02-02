package com.unacceptable.unacceptablelibrary.Models;

import com.google.gson.annotations.Expose;

public class Response {
    @Expose
    boolean success;
    @Expose
    String message;

    public Response(boolean success, String message) {
        this.success = success;
        this.message = message;
    }
}
