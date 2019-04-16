package com.unacceptable.unacceptablelibrary.Repositories;

public interface ITimeSource {
    long currentTimeMillis();
    long elapsedRealtime();
}
