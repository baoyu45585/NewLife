package com.ms.android.base.utils.task;

/**
 * 任务工具类
 *
 * @author wenxc 2016-01-09
 */
public class TaskHelper {

    private TaskHelper() {
        throw new AssertionError();
    }

    private static TaskController taskController;

    public static void setTaskController(TaskController taskController) {
        if (TaskHelper.taskController == null) {
            TaskHelper.taskController = taskController;
        }
    }

    static {
        TaskControllerImpl.registerInstance();
    }

    /**
     * 在UI线程执行runnable.
     * 如果已在UI线程, 则直接执行.
     *
     * @param runnable
     */
    public static void autoPost(Runnable runnable) {
        taskController.autoPost(runnable);
    }

    /**
     * 在UI线程执行runnable.
     * post到msg queue.
     *
     * @param runnable
     */
    public static void post(Runnable runnable) {
        taskController.post(runnable);
    }

    /**
     * 在UI线程执行runnable.
     *
     * @param runnable
     * @param delayMillis 延迟时间(单位毫秒)
     */
    public static void postDelayed(Runnable runnable, long delayMillis) {
        taskController.postDelayed(runnable, delayMillis);
    }

    /**
     * 在后台线程执行runnable
     *
     * @param runnable
     */
    public static void run(Runnable runnable) {
        taskController.run(runnable);
    }

    /**
     * 移除未执行的runnable
     *
     * @param runnable
     */
    public static void removeCallbacks(Runnable runnable) {
        taskController.removeCallbacks(runnable);
    }

    /**
     * 开始一个异步任务
     *
     * @param task
     * @param <T>
     * @return
     */
    public static <T> AbsTask<T> start(AbsTask<T> task) {
        return taskController.start(task);
    }

    /**
     * 同步执行一个任务
     *
     * @param task
     * @param <T>
     * @return
     * @throws Throwable
     */
    public static <T> T startSync(AbsTask<T> task) throws Throwable {
        return taskController.startSync(task);
    }
    
    /**
     * 批量执行异步任务
     *
     * @param groupCallback
     * @param tasks
     * @param <T>
     * @return
     */
    public static <T extends AbsTask<?>> Cancelable startTasks(GroupCallback<T> groupCallback, T... tasks) {
        return taskController.startTasks(groupCallback, tasks);
    }

}
