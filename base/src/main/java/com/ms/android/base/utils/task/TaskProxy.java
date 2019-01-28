//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.ms.android.base.utils.task;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import java.util.concurrent.Executor;

class TaskProxy<ResultType> extends AbsTask<ResultType> {
    static final InternalHandler sHandler = new InternalHandler();
    static final PriorityExecutor sDefaultExecutor = new PriorityExecutor(true);
    private final AbsTask<ResultType> task;
    private final Executor executor;
    private volatile boolean callOnCanceled = false;
    private volatile boolean callOnFinished = false;
    private static final int MSG_WHAT_BASE = 1000000000;
    private static final int MSG_WHAT_ON_WAITING = 1000000001;
    private static final int MSG_WHAT_ON_START = 1000000002;
    private static final int MSG_WHAT_ON_SUCCESS = 1000000003;
    private static final int MSG_WHAT_ON_ERROR = 1000000004;
    private static final int MSG_WHAT_ON_UPDATE = 1000000005;
    private static final int MSG_WHAT_ON_CANCEL = 1000000006;
    private static final int MSG_WHAT_ON_FINISHED = 1000000007;

    TaskProxy(AbsTask<ResultType> task) {
        super(task);
        this.task = task;
        this.task.setTaskProxy(this);
        this.setTaskProxy((TaskProxy) null);
        Object taskExecutor = task.getExecutor();
        if (taskExecutor == null) {
            taskExecutor = sDefaultExecutor;
        }

        this.executor = (Executor) taskExecutor;
    }

    protected final ResultType doBackground() throws Throwable {
        this.onWaiting();
        PriorityRunnable runnable = new PriorityRunnable(this.task.getPriority(), new Runnable() {
            public void run() {
                try {
                    if (TaskProxy.this.callOnCanceled || TaskProxy.this.isCancelled()) {
                        throw new CancelledException("");
                    }

                    TaskProxy.this.onStarted();
                    if (TaskProxy.this.isCancelled()) {
                        throw new CancelledException("");
                    }

                    TaskProxy.this.task.setResult(TaskProxy.this.task.doBackground());
                    TaskProxy.this.setResult(TaskProxy.this.task.getResult());
                    if (TaskProxy.this.isCancelled()) {
                        throw new CancelledException("");
                    }

                    TaskProxy.this.onSuccess(TaskProxy.this.task.getResult());
                } catch (CancelledException var6) {
                    TaskProxy.this.onCancelled(var6);
                } catch (Throwable var7) {
                    TaskProxy.this.onError(var7, false);
                } finally {
                    TaskProxy.this.onFinished();
                }

            }
        });
        this.executor.execute(runnable);
        return null;
    }

    protected void onWaiting() {
        this.setState(State.WAITING);
        sHandler.obtainMessage(MSG_WHAT_ON_WAITING, this).sendToTarget();
    }

    protected void onStarted() {
        this.setState(State.STARTED);
        sHandler.obtainMessage(MSG_WHAT_ON_START, this).sendToTarget();
    }

    protected void onSuccess(ResultType result) {
        this.setState(State.SUCCESS);
        sHandler.obtainMessage(MSG_WHAT_ON_SUCCESS, this).sendToTarget();
    }

    protected void onError(Throwable ex, boolean isCallbackError) {
        this.setState(State.ERROR);
        sHandler.obtainMessage(MSG_WHAT_ON_ERROR, new ArgsObj(this, new Object[]{ex})).sendToTarget();
    }

    protected void onUpdate(int flag, Object... args) {
        sHandler.obtainMessage(MSG_WHAT_ON_UPDATE, flag, flag, new ArgsObj(this, args)).sendToTarget();
    }

    protected void onCancelled(CancelledException cex) {
        this.setState(State.CANCELLED);
        sHandler.obtainMessage(MSG_WHAT_ON_CANCEL, new ArgsObj(this, new Object[]{cex})).sendToTarget();
    }

    protected void onFinished() {
        sHandler.obtainMessage(MSG_WHAT_ON_FINISHED, this).sendToTarget();
    }

    final void setState(State state) {
        super.setState(state);
        this.task.setState(state);
    }

    public final Priority getPriority() {
        return this.task.getPriority();
    }

    public final Executor getExecutor() {
        return this.executor;
    }

    private static class ArgsObj {
        final TaskProxy taskProxy;
        final Object[] args;

        public ArgsObj(TaskProxy taskProxy, Object... args) {
            this.taskProxy = taskProxy;
            this.args = args;
        }
    }

    static final class InternalHandler extends Handler {
        private String TAG = InternalHandler.class.getSimpleName();

        private InternalHandler() {
            super(Looper.getMainLooper());
        }

        public void handleMessage(Message msg) {
            if (msg.obj == null) {
                throw new IllegalArgumentException("msg must not be null");
            } else {
                TaskProxy taskProxy = null;
                Object[] args = null;
                if (msg.obj instanceof TaskProxy) {
                    taskProxy = (TaskProxy) msg.obj;
                } else if (msg.obj instanceof TaskProxy.ArgsObj) {
                    ArgsObj ex = (ArgsObj) msg.obj;
                    taskProxy = ex.taskProxy;
                    args = ex.args;
                }

                if (taskProxy == null) {
                    throw new RuntimeException("msg.obj not instanceof TaskProxy");
                } else {
                    try {
                        switch (msg.what) {
                            case MSG_WHAT_ON_WAITING:
                                taskProxy.task.onWaiting();
                                break;
                            case MSG_WHAT_ON_START:
                                taskProxy.task.onStarted();
                                break;
                            case MSG_WHAT_ON_SUCCESS:
                                taskProxy.task.onSuccess(taskProxy.getResult());
                                break;
                            case MSG_WHAT_ON_ERROR:
                                assert args != null;
                                Throwable ex1 = (Throwable) args[0];
                                Log.d(TAG, ex1.getMessage());
                                taskProxy.task.onError(ex1, false);
                                break;
                            case MSG_WHAT_ON_UPDATE:
                                taskProxy.task.onUpdate(msg.arg1, args);
                                break;
                            case MSG_WHAT_ON_CANCEL:
                                if (taskProxy.callOnCanceled) {
                                    return;
                                }

                                taskProxy.callOnCanceled = true;

                                assert args != null;

                                taskProxy.task.onCancelled((CancelledException) args[0]);
                                break;
                            case MSG_WHAT_ON_FINISHED:
                                if (taskProxy.callOnFinished) {
                                    return;
                                }

                                taskProxy.callOnFinished = true;
                                taskProxy.task.onFinished();
                        }
                    } catch (Throwable var5) {
                        taskProxy.setState(State.ERROR);
                        if (msg.what != MSG_WHAT_ON_ERROR) {
                            taskProxy.task.onError(var5, true);
                        }
                    }

                }
            }
        }
    }
}
