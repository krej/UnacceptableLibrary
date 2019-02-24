package com.unacceptable.unacceptablelibrary.Models;

import com.google.gson.annotations.Expose;

public class Response {
    @Expose
    public boolean Success;
    @Expose
    public String Message;

    public Response(boolean success, String message) {
        this.Success = success;
        this.Message = message;
    }
}
