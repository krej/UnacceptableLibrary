package com.unacceptable.unacceptablelibrary.Repositories;

import java.util.Calendar;
import java.util.Date;

public interface ITimeSource {
    long currentTimeMillis();
    long elapsedRealtime();
    Date getTodaysDate();
    Calendar getCalendarInstance();
}
