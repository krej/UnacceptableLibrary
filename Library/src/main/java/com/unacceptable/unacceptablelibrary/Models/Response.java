package com.unacceptable.unacceptablelibrary.Models;

import com.google.gson.annotations.Expose;

public class Response {
    @Expose
    boolean Success;
    @Expose
    String Message;

    public Response(boolean success, String message) {
        this.Success = success;
        this.Message = message;
    }
}
