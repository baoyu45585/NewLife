//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.ms.android.base.utils.task;

import android.support.annotation.NonNull;

import java.util.Comparator;
import java.util.concurrent.Executor;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class PriorityExecutor implements Executor {
    private static final int CORE_POOL_SIZE = 5;
    private static final int MAXIMUM_POOL_SIZE = 256;
    private static final int KEEP_ALIVE = 1;
    private static final AtomicLong SEQ_SEED = new AtomicLong(0L);
    private static final ThreadFactory sThreadFactory = new ThreadFactory() {
        private final AtomicInteger mCount = new AtomicInteger(1);

        public Thread newThread(@NonNull Runnable runnable) {
            return new Thread(runnable, "xTID#" + this.mCount.getAndIncrement());
        }
    };
    private static final Comparator FIFO_CMP = new Comparator() {
        @Override
        public int compare(Object lhs, Object rhs) {
            if (lhs instanceof PriorityRunnable && rhs instanceof PriorityRunnable) {
                PriorityRunnable lpr = (PriorityRunnable) lhs;
                PriorityRunnable rpr = (PriorityRunnable) rhs;
                int result = lpr.priority.ordinal() - rpr.priority.ordinal();
                return result == 0 ? (int) (lpr.SEQ - rpr.SEQ) : result;
            } else {
                return 0;
            }
        }
    };
    private static final Comparator FILO_CMP = new Comparator() {
        @Override
        public int compare(Object lhs, Object rhs) {
            if (lhs instanceof PriorityRunnable && rhs instanceof PriorityRunnable) {
                PriorityRunnable lpr = (PriorityRunnable) lhs;
                PriorityRunnable rpr = (PriorityRunnable) rhs;
                int result = lpr.priority.ordinal() - rpr.priority.ordinal();
                return result == 0 ? (int) (rpr.SEQ - lpr.SEQ) : result;
            } else {
                return 0;
            }
        }
    };
    private final ThreadPoolExecutor mThreadPoolExecutor;

    public PriorityExecutor(boolean fifo) {
        this(5, fifo);
    }

    public PriorityExecutor(int poolSize, boolean fifo) {
        PriorityBlockingQueue mPoolWorkQueue = new PriorityBlockingQueue(256, fifo ? FIFO_CMP : FILO_CMP);
        this.mThreadPoolExecutor = new ThreadPoolExecutor(poolSize, 256, 1L, TimeUnit.SECONDS, mPoolWorkQueue, sThreadFactory);
    }

    public int getPoolSize() {
        return this.mThreadPoolExecutor.getCorePoolSize();
    }

    public void setPoolSize(int poolSize) {
        if (poolSize > 0) {
            this.mThreadPoolExecutor.setCorePoolSize(poolSize);
        }

    }

    public ThreadPoolExecutor getThreadPoolExecutor() {
        return this.mThreadPoolExecutor;
    }

    public boolean isBusy() {
        return this.mThreadPoolExecutor.getActiveCount() >= this.mThreadPoolExecutor.getCorePoolSize();
    }

    public void execute(@NonNull Runnable runnable) {
        if (runnable instanceof PriorityRunnable) {
            ((PriorityRunnable) runnable).SEQ = SEQ_SEED.getAndIncrement();
        }

        this.mThreadPoolExecutor.execute(runnable);
    }
}
