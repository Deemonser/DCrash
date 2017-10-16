package com.deemons.lib;

import android.os.Handler;
import android.os.Looper;


/**
 * 创建者     chenghaohao
 * 创建时间   2017/9/15 10:26
 * 包名       com.deemons.lib
 * 描述	      捕获全局APP异常，防止Crash
 * 更新者     chenghaohao
 * 更新时间   2017/9/15 10:26
 * 更新描述   捕获全局APP异常，防止Crash
 */
public final class DCrash {


    private DCrash() {
    }

    private static ExceptionHandler sExceptionHandler;
    private static Thread.UncaughtExceptionHandler sUncaughtExceptionHandler;
    private static boolean sInstalled = false;//标记位，避免重复安装卸载

    /**
     * 开始捕获全局异常，使用默认的异常处理器 {@link DefaultExceptionHandler}
     */
    public static void start() {
        start(new DefaultExceptionHandler());
    }

    /**
     * 开始捕获全局异常
     * @param exceptionHandler 处理异常 {@link ExceptionHandler}
     */
    public static synchronized void start(ExceptionHandler exceptionHandler) {
        if (sInstalled) {
            return;
        }
        sInstalled = true;
        sExceptionHandler = exceptionHandler;

        //利用 Handler 机制，捕获主线程异常。
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {

                while (true) {
                    try {
                        Looper.loop();
                    } catch (Throwable e) {
                        //停止DCrash时抛的异常，让死循环退出
                        if (e instanceof QuitCrash) {
                            return;
                        }
                        //处理异常
                        if (sExceptionHandler != null) {
                            sExceptionHandler.handlerException(Looper.getMainLooper().getThread(), e);
                        }
                    }
                }
            }
        });

        //将全局未捕获的异常处理器替换掉，变为自定义的处理异常。此处理器只能捕获子线程异常，对主线程异常无效。
        sUncaughtExceptionHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread t, Throwable e) {
                if (sExceptionHandler != null) {
                    sExceptionHandler.handlerException(t, e);
                }
            }
        });

    }

    /**
     * 停止 捕获异常
     */
    public static synchronized void stop() {
        if (!sInstalled) {
            return;
        }
        sInstalled = false;
        sExceptionHandler = null;
        //卸载后恢复默认的异常处理逻辑，否则主线程再次抛出异常后将导致ANR，并且无法捕获到异常位置
        Thread.setDefaultUncaughtExceptionHandler(sUncaughtExceptionHandler);
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                throw new QuitCrash("Quit DCrash!");//主线程抛出异常，迫使死循环结束
            }
        });

    }
}
