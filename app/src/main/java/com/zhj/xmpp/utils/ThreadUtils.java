package com.zhj.xmpp.utils;


import android.os.Handler;

/**
 * Created by hasee on 2016/8/27.
 */
public class ThreadUtils {
    /**创建主线程的handler*/
    private static Handler mHandler = new Handler();
    /**
     * 子线程执行任务
     */
    public static void runInThread(Runnable task){
        new Thread(task).start();
    }
    /**主线程执行任务*/

    public static void runInUIThread(Runnable task){
       mHandler.post(task);
    }

}
