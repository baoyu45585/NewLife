//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.ms.android.base.utils.task;

import android.os.Looper;
import android.util.Log;

import java.util.concurrent.atomic.AtomicInteger;


public final class TaskControllerImpl implements TaskController {
    private static final String TAG = TaskControllerImpl.class.getSimpleName();
    private static TaskController instance;

    private TaskControllerImpl() {
    }

    public static void registerInstance() {
        if (instance == null) {
            Class var0 = TaskController.class;
            synchronized (TaskController.class) {
                if (instance == null) {
                    instance = new TaskControllerImpl();
                }
            }
        }

        TaskHelper.setTaskController(instance);
    }

    public <T> AbsTask<T> start(AbsTask<T> task) {
        TaskProxy proxy = null;
        if (task instanceof TaskProxy) {
            proxy = (TaskProxy) task;
        } else {
            proxy = new TaskProxy(task);
        }

        try {
            proxy.doBackground();
        } catch (Throwable var4) {
            Log.e(TAG, var4.getMessage());
        }

        return proxy;
    }

    public <T> T startSync(AbsTask<T> task) throws Throwable {
        Object result = null;

        try {
            task.onWaiting();
            task.onStarted();
            result = task.doBackground();
            task.onSuccess((T) result);
        } catch (CancelledException var8) {
            task.onCancelled(var8);
        } catch (Throwable var9) {
            task.onError(var9, false);
            throw var9;
        } finally {
            task.onFinished();
        }

        return (T) result;
    }

    public <T extends AbsTask<?>> Cancelable startTasks(final GroupCallback<T> groupCallback, final T... tasks) {
        if (tasks == null) {
            throw new IllegalArgumentException("task must not be null");
        } else {
            final Runnable callIfOnAllFinished = new Runnable() {
                private final int total = tasks.length;
                private final AtomicInteger count = new AtomicInteger(0);

                public void run() {
                    if (this.count.incrementAndGet() == this.total && groupCallback != null) {
                        groupCallback.onAllFinished();
                    }

                }
            };
            AbsTask[] var7 = tasks;
            int var6 = tasks.length;

            for (int var5 = 0; var5 < var6; ++var5) {
                final AbsTask task = var7[var5];
                this.start(new TaskProxy(task) {
                    protected void onSuccess(Object result) {
                        super.onSuccess(result);
                        TaskControllerImpl.this.post(new Runnable() {
                            public void run() {
                                if (groupCallback != null) {
                                    groupCallback.onSuccess((T) task);
                                }

                            }
                        });
                    }

                    protected void onCancelled(final CancelledException cex) {
                        super.onCancelled(cex);
                        TaskControllerImpl.this.post(new Runnable() {
                            public void run() {
                                if (groupCallback != null) {
                                    groupCallback.onCancelled((T) task, cex);
                                }

                            }
                        });
                    }

                    protected void onError(final Throwable ex, final boolean isCallbackError) {
                        super.onError(ex, isCallbackError);
                        TaskControllerImpl.this.post(new Runnable() {
                            public void run() {
                                if (groupCallback != null) {
                                    groupCallback.onError((T) task, ex, isCallbackError);
                                }

                            }
                        });
                    }

                    protected void onFinished() {
                        super.onFinished();
                        TaskControllerImpl.this.post(new Runnable() {
                            public void run() {
                                if (groupCallback != null) {
                                    groupCallback.onFinished((T) task);
                                }

                                callIfOnAllFinished.run();
                            }
                        });
                    }
                });
            }

            return new Cancelable() {
                public void cancel() {
                    AbsTask[] var4 = tasks;
                    int var3 = tasks.length;

                    for (int var2 = 0; var2 < var3; ++var2) {
                        AbsTask task = var4[var2];
                        task.cancel();
                    }

                }

                public boolean isCancelled() {
                    boolean isCancelled = true;
                    AbsTask[] var5 = tasks;
                    int var4 = tasks.length;

                    for (int var3 = 0; var3 < var4; ++var3) {
                        AbsTask task = var5[var3];
                        if (!task.isCancelled()) {
                            isCancelled = false;
                        }
                    }

                    return isCancelled;
                }
            };
        }
    }

    public void autoPost(Runnable runnable) {
        if (runnable != null) {
            if (Thread.currentThread() == Looper.getMainLooper().getThread()) {
                runnable.run();
            } else {
                TaskProxy.sHandler.post(runnable);
            }

        }
    }

    public void post(Runnable runnable) {
        if (runnable != null) {
            TaskProxy.sHandler.post(runnable);
        }
    }

    public void postDelayed(Runnable runnable, long delayMillis) {
        if (runnable != null) {
            TaskProxy.sHandler.postDelayed(runnable, delayMillis);
        }
    }

    public void run(Runnable runnable) {
        this.run(runnable, true);
    }

    public void run(Runnable runnable, boolean immediated) {
        if (TaskProxy.sDefaultExecutor.isBusy() && immediated) {
            (new Thread(runnable)).start();
        } else {
            TaskProxy.sDefaultExecutor.execute(runnable);
        }

    }

    public void removeCallbacks(Runnable runnable) {
        TaskProxy.sHandler.removeCallbacks(runnable);
    }
}
