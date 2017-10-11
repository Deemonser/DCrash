package com.deemons.dcrash;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.deemons.lib.DCrash;
import com.deemons.lib.ExceptionHandler;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    public void button0(View view) {
        DCrash.start(new ExceptionHandler() {
            @Override
            public void handlerException(Thread thread, Throwable throwable) {

            }
        });
    }

    public void button1(View view) {
        throw new NullPointerException("Test NullPointerException");
    }

    public void button2(View view) {
        throw new IllegalStateException("Test IllegalStateException");
    }

    public void button3(View view) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                throw new NullPointerException("Thread:" + Thread.currentThread().getName() + "\nTest NullPointerException");
            }
        }).start();

    }

    public void button4(View view) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                throw new IllegalStateException("Thread:" + Thread.currentThread().getName() + "\nTest IllegalStateException");
            }
        }).start();
    }

    public void button5(View view) {
        DCrash.stop();
    }

}
