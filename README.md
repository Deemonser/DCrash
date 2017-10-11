![](https://img.shields.io/badge/build-passing-green.svg)&nbsp; ![](https://img.shields.io/badge/JCenter-1.0.0-brightgreen.svg)&nbsp;![](https://img.shields.io/badge/licenes-Apache2-brightgreen.svg)

<br>

DCarsh 是一个防止APP Crash 的库。

我们知道，APP 一旦崩溃，就会弹出一个停止运行的弹窗，这对用户很不友好。

我们希望，即使APP crash 了，也不会出现这个弹窗，而是直接退出。



### 原理

DCrash 的核心有两个，一个是替换系统默认的 全局未捕获异常处理器。

```java
  //将全局未捕获的异常处理器替换掉，变为自定义的处理异常。此处理器只能捕获子线程异常，对主线程异常无效。
        sUncaughtExceptionHandler = Thread.getDefaultUncaughtExceptionHandler();//取出默认处理器
        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread t, Throwable e) {
                if (sExceptionHandler != null) {
                    sExceptionHandler.handlerException(t, e);
                }
            }
        });
```

这个异常处理器只能出来非UI线程的异常，而一旦UI线程出现异常，是不会进入这个回调的。

这个时候就有人想出了利用主线程 Handler 机制，来捕获主线程的异常，这也是DCrash 的第二个核心。

```java
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
```

利用 Handler机制，向 UI 线程发送了一个死循环的 Runnable ，这样就相当于接过了系统的默认 Handler 机制调用，这时候就可以 try -catch 捕获主线程的异常了。



### 使用

1. 在项目的 build.gradle 中添加依赖

   ```groovy
   compile 'com.deemons.dcrash:dcrash:x.x.x'
   ```

2. 在需要的地方使用

   ```java
   DCrash.start();

   //你也可以自定义全局未捕获的异常处理器，注意，异常处理器有可能会在子线程运行
   DCrash.start(new ExceptionHandler() {
               @Override
               public void handlerException(Thread thread, Throwable throwable) {
                   //do something...
               }
           });
   ```

3. 也可以随时停止

   ```java
   DCrash.stop();
   ```

