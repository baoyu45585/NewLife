//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.ms.android.base.utils.task;

import java.util.concurrent.Executor;

public abstract class AbsTask<ResultType> implements Cancelable {
    private TaskProxy taskProxy;
    private final Cancelable cancelHandler;
    private volatile boolean isCancelled;
    private volatile State state;
    private ResultType result;

    public AbsTask() {
        this((Cancelable) null);
    }

    public AbsTask(Cancelable cancelHandler) {
        this.taskProxy = null;
        this.isCancelled = false;
        this.state = State.IDLE;
        this.cancelHandler = cancelHandler;
    }

    protected abstract ResultType doBackground() throws Throwable;

    protected abstract void onSuccess(ResultType var1);

    protected abstract void onError(Throwable var1, boolean var2);

    protected void onWaiting() {
    }

    protected void onStarted() {
    }

    protected void onUpdate(int flag, Object... args) {
    }

    protected void onCancelled(CancelledException cex) {
    }

    protected void onFinished() {
    }

    public Priority getPriority() {
        return null;
    }

    public Executor getExecutor() {
        return null;
    }

    protected final void update(int flag, Object... args) {
        if (this.taskProxy != null) {
            this.taskProxy.onUpdate(flag, args);
        }

    }

    protected void cancelWorks() {
    }

    protected boolean isCancelFast() {
        return false;
    }

    public final synchronized void cancel() {
        if (!this.isCancelled) {
            this.isCancelled = true;
            this.cancelWorks();
            if (this.cancelHandler != null && !this.cancelHandler.isCancelled()) {
                this.cancelHandler.cancel();
            }

            if (this.state == State.WAITING || this.state == State.STARTED && this.isCancelFast()) {
                if (this.taskProxy != null) {
                    this.taskProxy.onCancelled(new CancelledException("cancelled by user"));
                    this.taskProxy.onFinished();
                } else if (this instanceof TaskProxy) {
                    this.onCancelled(new CancelledException("cancelled by user"));
                    this.onFinished();
                }
            }
        }

    }

    public final boolean isCancelled() {
        return this.isCancelled || this.state == State.CANCELLED || this.cancelHandler != null && this.cancelHandler.isCancelled();
    }

    public final boolean isFinished() {
        return this.state.value() > State.STARTED.value();
    }

    public final State getState() {
        return this.state;
    }

    public final ResultType getResult() {
        return this.result;
    }

    void setState(State state) {
        this.state = state;
    }

    final void setTaskProxy(TaskProxy taskProxy) {
        this.taskProxy = taskProxy;
    }

    final void setResult(ResultType result) {
        this.result = result;
    }

    public static enum State {
        IDLE(0),
        WAITING(1),
        STARTED(2),
        SUCCESS(3),
        CANCELLED(4),
        ERROR(5);

        private final int value;

        private State(int value) {
            this.value = value;
        }

        public int value() {
            return this.value;
        }
    }
}
