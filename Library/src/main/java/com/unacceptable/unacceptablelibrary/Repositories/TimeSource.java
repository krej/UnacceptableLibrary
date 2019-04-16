package com.unacceptable.unacceptablelibrary.Repositories;

import android.os.SystemClock;

public class TimeSource implements ITimeSource {
    @Override
    public long currentTimeMillis() {
        return System.currentTimeMillis();
    }

    @Override
    public long elapsedRealtime() {
        return SystemClock.elapsedRealtime();
    }
}
