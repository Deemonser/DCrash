package com.deemons.lib;

import android.util.Log;

/**
 * 创建者      chenghaohao
 * 创建时间     2017/9/15 10:18
 * 包名       com.deemons.lib
 * 描述       全局异常处理器的默认实现。
 */

public class DefaultExceptionHandler implements ExceptionHandler {


    @Override
    public void handlerException(Thread thread, Throwable throwable) {
        Log.e("DefaultExceptionHandler", "Thread:" + thread.getName(),throwable);
    }
}
