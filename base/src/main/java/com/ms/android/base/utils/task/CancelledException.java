package com.ms.android.base.utils.task;

public class CancelledException extends RuntimeException {
    public CancelledException(String detailMessage) {
        super(detailMessage);
    }
}