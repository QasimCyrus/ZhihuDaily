package com.cyrus.zhihudaily.manager;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 线程池管理
 * <p>
 * Created by Cyrus on 2016/4/23.
 */
public class ThreadManager {
    private static ThreadManager instance = new ThreadManager();
    private ThreadPoolProxy mThreadPoolLongProxy;//线程池数量多的线程代理
    private ThreadPoolProxy mThreadPoolShortProxy;//线程池数量少的线程代理

    private ThreadManager() {
    }

    //提供外部方法  是外部获取线程代理
    public static ThreadManager getInstance() {
        return instance;
    }

    public synchronized ThreadPoolProxy createLongPool() {
        if (mThreadPoolLongProxy == null) {
            mThreadPoolLongProxy = new ThreadPoolProxy(5, 5, 500L);
        }
        return mThreadPoolLongProxy;
    }

    public synchronized ThreadPoolProxy createShortPool() {
        if (mThreadPoolShortProxy == null) {
            mThreadPoolShortProxy = new ThreadPoolProxy(3, 3, 500L);
        }
        return mThreadPoolShortProxy;
    }


    //创建线程管理器的代理
    public class ThreadPoolProxy {
        //线程池的执行者
        private ThreadPoolExecutor mThreadPoolExecutor;

        private int mCorePoolSize;
        private int mMaximumPoolSize;
        private long mTime;

        public ThreadPoolProxy(int corePoolSize, int maximumPoolSize, long time) {
            mCorePoolSize = corePoolSize;
            mMaximumPoolSize = maximumPoolSize;
            mTime = time;
        }

        //线程的执行
        public void execute(Runnable runnable) {
            if (mThreadPoolExecutor == null) {
                mThreadPoolExecutor = new ThreadPoolExecutor(
                        mCorePoolSize,//核心线程池中线程的数量
                        mMaximumPoolSize,//最大线程池中线程的数量
                        mTime,//每个线程运行的时间
                        TimeUnit.MILLISECONDS,//线程运行时间的单位
                        //使用链接表管理被阻塞的线程，10代表允许最大的阻塞线程的数量
                        new LinkedBlockingQueue<Runnable>(10));
            }

            mThreadPoolExecutor.execute(runnable);
        }


        //线程的取消
        public void cancel(Runnable runnable) {
            //线程池执行者不为空，且没有终止时执行线程的取消
            if (mThreadPoolExecutor != null &&
                    !mThreadPoolExecutor.isShutdown() &&
                    !mThreadPoolExecutor.isTerminated()) {
                mThreadPoolExecutor.remove(runnable);//取消异步任务；
            }
        }
    }
}
