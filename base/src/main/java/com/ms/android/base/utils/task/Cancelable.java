package com.ms.android.base.utils.task;

public interface Cancelable {
    void cancel();

    boolean isCancelled();
}