package com.deemons.lib;

/**
 * 创建者      chenghaohao
 * 创建时间     2017/9/15 10:19
 * 包名       com.deemons.lib
 * 描述       全局异常处理器，包括主线程与子线程异常，
 *           注意：handlerException 方法可能运行在子线程
 *           默认实现 {@link DefaultExceptionHandler}
 */

public interface ExceptionHandler {
    void handlerException(Thread thread, Throwable throwable);
}
