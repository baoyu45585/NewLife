package com.ms.android.base.utils.task;

public interface GroupCallback<ItemType> {
    void onSuccess(ItemType var1);

    void onError(ItemType var1, Throwable var2, boolean var3);

    void onCancelled(ItemType var1, CancelledException var2);

    void onFinished(ItemType var1);

    void onAllFinished();
}