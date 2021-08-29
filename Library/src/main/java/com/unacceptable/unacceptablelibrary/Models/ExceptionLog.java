package com.unacceptable.unacceptablelibrary.Models;

import com.google.gson.annotations.Expose;
import com.unacceptable.unacceptablelibrary.Models.ListableObject;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.time.OffsetDateTime;
import java.util.Arrays;

public class ExceptionLog extends ListableObject {

    public ExceptionLog() {

    }

    public ExceptionLog(Throwable e, String sSource, OffsetDateTime t) {
        try {
            Message = e.getMessage();
            //StackTrace = Arrays.toString(e.getStackTrace());
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            StackTrace = sw.toString(); // stack trace as a string

        } catch (Exception e2) {
            Message = "Exception Saving Exception... Inception... If you're seeing this, shit is fucked up...";
            StackTrace = "";
        }

        Source = sSource;
        //Time = t;
    }

    //@Expose
    //public OffsetDateTime Time;
    @Expose
    public String Message;
    @Expose
    public String StackTrace;
    @Expose
    public String Source;
}
