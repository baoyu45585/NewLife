
package com.ms.android.base.utils;

import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 提供打印log信息的API。实际项目中必须使用这些API打印log，不再直接使用Android提供的{@link Log}，
 * 正式发布版本将屏蔽不必要的log输出。当然，代码中临时性的log语句在不使用时必须删除。
 *
 * @author kaiyuan.zhang
 * @since 2015-8-11
 */
//TODO: 在该类中,未使用的语句和成员变量是不可避免的
@SuppressWarnings("unused")
public class LogUtil {

    private static final String DEFAULT_TAG = LogUtil.class.getSimpleName();
    public static final int LOG_LEVEL_VERBOSE = 0;
    public static final int LOG_LEVEL_DEBUG = 1;
    public static final int LOG_LEVEL_INFO = 2;
    public static final int LOG_LEVEL_WARN = 3;
    public static final int LOG_LEVEL_ERROR = 4;

    /**
     * 该等级的log信息会写入到指定的文件中
     *
     * @see #wtf(String, String)
     * @see #wtf(String, String, Throwable)
     */
    public static final int LOG_LEVEL_ASSERT = 5;

    /**
     * 指定为该等级则禁用所有的log
     */
    public static final int LOG_LEVEL_NONE = Integer.MAX_VALUE;

    /**
     * 指定输出log的等级，小于该等级的log类别将被屏蔽
     */
    private static int LOG_LEVEL = LOG_LEVEL_DEBUG;
    private static SimpleDateFormat sFileFormat;
    //private static int LOG_LEVEL = BuildConfig.DEBUG ? LOG_LEVEL_DEBUG : LOG_LEVEL_ASSERT;

    /**
     * 设置log输出等级，低于level的将被屏蔽
     * <p>
     * <p>可在Application中调用设置log输出等级，代码使用示例如下：<br>{@code LogUtil.setLogLevel(BuildConfig.DEBUG ? LogUtil.LOG_LEVEL_DEBUG : LogUtil.LOG_LEVEL_NONE);}</p>
     *
     * @param level
     */
    public static void setLogLevel(int level) {
        LOG_LEVEL = level;
    }

    public static void v(String msg) {
        v(DEFAULT_TAG, msg);
    }

    /**
     * Send a VERBOSE log message.
     *
     * @param tag Used to identify the source of a log message. It usually
     *            identifies the class or activity where the log call occurs.
     * @param msg The message you would like logged.
     */
    public static void v(String tag, String msg) {
        if (LOG_LEVEL_VERBOSE < LOG_LEVEL) {
            return;
        }
        Log.v(tag, msg);
    }

    /**
     * Send a VERBOSE log message and log the exception.
     *
     * @param tag Used to identify the source of a log message. It usually
     *            identifies the class or activity where the log call occurs.
     * @param msg The message you would like logged.
     * @param tr  An exception to log
     */
    public static void v(String tag, String msg, Throwable tr) {
        if (LOG_LEVEL_VERBOSE < LOG_LEVEL) {
            return;
        }
        Log.v(tag, msg, tr);
    }

    public static void d(String msg) {
        d(DEFAULT_TAG, msg);
    }

    /**
     * Send a DEBUG log message.
     *
     * @param tag Used to identify the source of a log message. It usually
     *            identifies the class or activity where the log call occurs.
     * @param msg The message you would like logged.
     */
    public static void d(String tag, String msg) {
        if (LOG_LEVEL_DEBUG < LOG_LEVEL) {
            return;
        }
        Log.d(tag, msg);
    }

    /**
     * Send a DEBUG log message and log the exception.
     *
     * @param tag Used to identify the source of a log message. It usually
     *            identifies the class or activity where the log call occurs.
     * @param msg The message you would like logged.
     * @param tr  An exception to log
     */
    public static void d(String tag, String msg, Throwable tr) {
        if (LOG_LEVEL_DEBUG < LOG_LEVEL) {
            return;
        }
        Log.d(tag, msg, tr);
    }

    public static void i(String msg) {
        i(DEFAULT_TAG, msg);
    }

    /**
     * Send a INFO log message.
     *
     * @param tag Used to identify the source of a log message. It usually
     *            identifies the class or activity where the log call occurs.
     * @param msg The message you would like logged.
     */
    public static void i(String tag, String msg) {
        if (LOG_LEVEL_INFO < LOG_LEVEL) {
            return;
        }
        Log.i(tag, msg);
    }

    /**
     * Send a INFO log message and log the exception.
     *
     * @param tag Used to identify the source of a log message. It usually
     *            identifies the class or activity where the log call occurs.
     * @param msg The message you would like logged.
     * @param tr  An exception to log
     */
    public static void i(String tag, String msg, Throwable tr) {
        if (LOG_LEVEL_INFO < LOG_LEVEL) {
            return;
        }
        Log.i(tag, msg, tr);
    }

    public static void w(String msg) {
        w(DEFAULT_TAG, msg);
    }

    /**
     * Send a WARN log message.
     *
     * @param tag Used to identify the source of a log message. It usually
     *            identifies the class or activity where the log call occurs.
     * @param msg The message you would like logged.
     */
    public static void w(String tag, String msg) {
        if (LOG_LEVEL_WARN < LOG_LEVEL) {
            return;
        }
        Log.w(tag, msg);
    }

    /**
     * Send a WARN log message and log the exception.
     *
     * @param tag Used to identify the source of a log message. It usually
     *            identifies the class or activity where the log call occurs.
     * @param msg The message you would like logged.
     * @param tr  An exception to log
     */
    public static void w(String tag, String msg, Throwable tr) {
        if (LOG_LEVEL_WARN < LOG_LEVEL) {
            return;
        }
        Log.w(tag, msg, tr);
    }

    public static void e(String msg) {
        e(DEFAULT_TAG, msg);
    }

    /**
     * Send a ERROR log message.
     *
     * @param tag Used to identify the source of a log message. It usually
     *            identifies the class or activity where the log call occurs.
     * @param msg The message you would like logged.
     */
    public static void e(String tag, String msg) {
        if (LOG_LEVEL_ERROR < LOG_LEVEL) {
            return;
        }
        Log.e(tag, msg);
    }

    /**
     * Send a ERROR log message and log the exception.
     *
     * @param tag Used to identify the source of a log message. It usually
     *            identifies the class or activity where the log call occurs.
     * @param msg The message you would like logged.
     * @param tr  An exception to log
     */
    public static void e(String tag, String msg, Throwable tr) {
        if (LOG_LEVEL_ERROR < LOG_LEVEL) {
            return;
        }
        Log.e(tag, msg, tr);
    }

    /**
     * <pre>
     * 写入到文件的log格式：
     *    Time\tPid\tTag\tMessage\n
     * </pre>
     */
    private static final String LOG_FORMAT = "%1$s\t%2$s\t%3$s\t%4$s\n";

    private static final String TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    private static final String FILE_FORMAT = "yyyy-MM-dd_HHmmss";

    private static final String LOG_FILE_NAME = "traces.log";

    private static String sLogFileDir = "cache";

    private static SimpleDateFormat sDateFormat;

    private static FileOutputStream sLogOutput;

    public static void setWtfFileDir(String dir) {
        synchronized (LOG_FORMAT) {
            if (!sLogFileDir.equals(dir)) {
                sLogFileDir = dir;
                sLogOutput = null;
            }
        }
    }

    /**
     * <pre>
     * 将log信息写入到设定的文件中。格式为：Time \t Pid \t Tag \t Message
     * 注意：严重的异常，或无法及时得到log时才使用该方法
     * </pre>
     *
     * @param tag 用来标识log信息
     * @param msg 要记录的信息
     */
    public static void wtf(String tag, String msg) {
        if (LOG_LEVEL_ASSERT < LOG_LEVEL) {
            return;
        }
        wtf(tag, msg, null);
    }

    /**
     * <pre>
     * 将log信息写入到设定的文件中。格式为：Time \t Pid \t Tag \t Message
     * 注意：严重的异常，或无法及时得到log时才使用该方法
     * </pre>
     *
     * @param tag 用来标识log信息
     * @param msg 要记录的信息
     * @param tr  要记录的异常实例
     */
    public static void wtf(String tag, String msg, Throwable tr) {
        if (LOG_LEVEL_ASSERT < LOG_LEVEL) {
            return;
        }
        if (tag == null || msg == null) {
            return;
        }
        synchronized (LOG_FORMAT) {
            if (sDateFormat == null) {
                sDateFormat = new SimpleDateFormat(TIME_FORMAT);
            }
            String timeStr = sDateFormat.format(new Date(System.currentTimeMillis()));
            if (sFileFormat == null) {
                sFileFormat = new SimpleDateFormat(FILE_FORMAT);
            }
            String fileStr = sFileFormat.format(new Date(System.currentTimeMillis()));
            String pidStr = String.valueOf(android.os.Process.myPid());
            String logStr = String.format(LOG_FORMAT, timeStr, pidStr, tag, msg);
            if (tr != null) {
                logStr += Log.getStackTraceString(tr);
            }
            FileOutputStream logOutput = sLogOutput;
            try {
                if (logOutput == null) {
                    File file = new File(sLogFileDir, fileStr + "_" + LOG_FILE_NAME);
                    File parent = file.getParentFile();
                    if (parent != null && !parent.exists()) {
                        boolean isOk = parent.mkdirs();
                        if (!isOk) {
                            e(LogUtil.class.getSimpleName(), "Can not make dirs: " + parent);
                            return;
                        }
                    }
                    logOutput = sLogOutput = new FileOutputStream(file, true);
                }
                logOutput.write(logStr.getBytes());
                logOutput.flush();
            } catch (IOException ioe) {
                if (logOutput != null) {
                    try {
                        logOutput.close();
                    } catch (IOException e1) {
                    }
                    sLogOutput = null;
                }
                e(LogUtil.class.getSimpleName(), "Writing log file failed!", ioe);
            }
        }
    }
}
