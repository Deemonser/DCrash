package com.deemons.lib;

/**
 * 创建者      chenghaohao
 * 创建时间     2017/9/15 10:13
 * 包名       com.deemons.lib
 * 描述       此异常专用于退出 DCrash
 */

public class QuitCrash extends RuntimeException {
    public QuitCrash(String message) {
        super(message);
    }
}
