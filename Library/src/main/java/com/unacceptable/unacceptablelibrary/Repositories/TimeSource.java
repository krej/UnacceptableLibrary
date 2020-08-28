package com.unacceptable.unacceptablelibrary.Repositories;

import android.os.SystemClock;

import java.util.Calendar;
import java.util.Date;

public class TimeSource implements ITimeSource {
    @Override
    public long currentTimeMillis() {
        return System.currentTimeMillis();
    }

    @Override
    public long elapsedRealtime() {
        return SystemClock.elapsedRealtime();
    }

    @Override
    public Date getTodaysDate() {
        return new Date();
    }

    @Override
    public Calendar getCalendarInstance() {
        return Calendar.getInstance();
    }
}
