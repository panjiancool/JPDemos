package com.jpan.jpdemos.ui;

import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.jpan.jpdemos.BaseActivity;
import com.jpan.jpdemos.R;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

import butterknife.InjectView;

public class ThreadControlDemo extends BaseActivity {

    @InjectView(R.id.normal_start)
    Button mNormalStart;
    @InjectView(R.id.join_start)
    Button mJoinStart;
    @InjectView(R.id.wait_start)
    Button mWaitStart;
    @InjectView(R.id.countDownLatch_start)
    Button mCountDownLatchStart;
    @InjectView(R.id.all_ready_start)
    Button mAllReadyStart;
    @InjectView(R.id.do_task_start)
    Button mDoTaskStart;
    @InjectView(R.id.executorService_start)
    Button mExecutorStart;
    @InjectView(R.id.test_result)
    TextView mTextResult;

    private static Handler mHandler;

    private StringBuilder mResult = new StringBuilder();

    private static class NewHandler extends Handler {
        WeakReference<ThreadControlDemo> mActivityWeakReference;

        private NewHandler(ThreadControlDemo activity) {
            mActivityWeakReference = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    ThreadControlDemo activity = mActivityWeakReference.get();
                    if (activity != null) {
                        activity.setResultContent((String) msg.obj);
                    }
                    break;
                case 1:
                    demo3_B_thread();
                    break;
                default:
                    break;
            }
        }
    }

    public void setResultContent(String content) {
        mResult.append(content);
        mResult.append("\r\n");
        mTextResult.setText(mResult.toString());
    }

    @Override
    public int getLayoutResourceId() {
        return R.layout.demo_thread_control;
    }

    @Override
    public void initView() {
        mNormalStart.setOnClickListener(this);
        mJoinStart.setOnClickListener(this);
        mWaitStart.setOnClickListener(this);
        mCountDownLatchStart.setOnClickListener(this);
        mAllReadyStart.setOnClickListener(this);
        mDoTaskStart.setOnClickListener(this);
        mExecutorStart.setOnClickListener(this);
        mHandler = new NewHandler(this);
    }

    @Override
    public void onClick(View v) {
        mResult.setLength(0);
        switch (v.getId()) {
            case R.id.normal_start:
                demo1();
                break;
            case R.id.join_start:
                demo2();
                break;
            case R.id.wait_start:
                demo3();
                break;
            case R.id.countDownLatch_start:
                runDAfterABC();
                break;
            case R.id.all_ready_start:
                runABCWhenAllReady();
                break;
            case R.id.do_task_start:
                doTaskWithResultInWorker();
                break;
            case R.id.executorService_start:
                try {
                    exec();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
                break;
            default:
                break;
        }
    }

    /**
     * 打印函数
     *
     * @param threadName 打印线程名称
     */
    private static void printNumber(String threadName) {
        int i = 0;
        while (i++ < 3) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            printMsg(threadName + " print: " + i);
        }
        printMsg(threadName + " print end");
    }

    private static void printMsg(String msg) {
        mHandler.sendMessage(Message.obtain(mHandler, 0, msg));
    }

    /**
     * 正常启动两个线程
     */
    private static void demo1() {
        Thread A = new Thread(new Runnable() {
            @Override
            public void run() {
                printNumber("A");
            }
        });
        Thread B = new Thread(new Runnable() {
            @Override
            public void run() {
                printNumber("B");
            }
        });
        A.start();
        B.start();
    }

    /**
     * 调用join控制线程执行顺序
     */
    private static void demo2() {
        final Thread A = new Thread(new Runnable() {
            @Override
            public void run() {
                printNumber("A");
            }
        });
        Thread B = new Thread(new Runnable() {
            @Override
            public void run() {
                printMsg("B开始等待A");
                try {
                    A.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                printNumber("B");
            }
        });
        B.start();
        A.start();
    }

    /**
     * 通过相同的类锁以及wait和notify实现线程控制，注意wait会放弃锁的监听
     */
    private void demo3() {
        Thread A = new Thread(new Runnable() {
            @Override
            public void run() {
                synchronized (ThreadControlDemo.class) {
                    printMsg("A 1");
                    printMsg("A waiting...");
                    mHandler.sendMessage(Message.obtain(mHandler, 1, null));
                    try {
                        ThreadControlDemo.class.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    printMsg("A 2");
                    printMsg("A 3");
                }
            }
        });
        A.start();
    }

    /**
     * 配合上述demo3调用类的notify唤醒A
     */
    private static void demo3_B_thread() {
        Thread B = new Thread(new Runnable() {
            @Override
            public void run() {
                synchronized (ThreadControlDemo.class) {
                    printMsg("B 1");
                    printMsg("B 2");
                    printMsg("B 3");
                    ThreadControlDemo.class.notify();
                }
            }
        });
        B.start();
    }

    /**
     * D在ABC执行完毕后才开始执行
     */
    private void runDAfterABC() {
        final CountDownLatch countDownLatch = new CountDownLatch(3);
        new Thread(new Runnable() {
            @Override
            public void run() {
                printMsg("D is waiting for other three threads");
                try {
                    countDownLatch.await();
                    printMsg("All done, D starts working");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
        for (char threadName = 'A'; threadName <= 'C'; threadName++) {
            final String tN = String.valueOf(threadName);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    printMsg(tN + " is working");
                    try {
                        Thread.sleep(100);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    printMsg(tN + " finished");
                    countDownLatch.countDown();
                }
            }).start();
        }
    }

    /**
     * ABC三个同时准备好数据后，同时开始执行内容
     */
    private void runABCWhenAllReady() {
        final CyclicBarrier cyclicBarrier = new CyclicBarrier(3);
        final Random random = new Random();
        for (char runnerName = 'A'; runnerName <= 'C'; runnerName++) {
            final String rN = String.valueOf(runnerName);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    long prepareTime = random.nextInt(10000) + 100;
                    printMsg(rN + " is preparing for time:" + prepareTime);
                    try {
                        Thread.sleep(prepareTime);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    try {
                        printMsg(rN + " is prepared, waiting for others");
                        cyclicBarrier.await(); // 当前运动员准备完毕，等待别人准备好
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (BrokenBarrierException e) {
                        e.printStackTrace();
                    }
                    printMsg(rN + " starts running"); // 所有运动员都准备好了，一起开始跑
                }
            }).start();
        }
    }

    /**
     * 主线程与子线程数据通信，注意此例子会阻塞主线程
     */
    private void doTaskWithResultInWorker() {
        Callable<Integer> callable = new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                printMsg("Task starts");
                Thread.sleep(1000);
                int result = 0;
                for (int i = 0; i <= 100; i++) {
                    result += i;
                }
                printMsg("Task finished and return result. thread:" + Thread.currentThread().getName());
                return result;
            }
        };
        FutureTask<Integer> futureTask = new FutureTask<>(callable);
        new Thread(futureTask).start();
        try {
            printMsg("Before futureTask.get()");
            printMsg("Result:" + futureTask.get() + " thread:" + Thread.currentThread().getName());
            printMsg("After futureTask.get()");
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    /**
     * 线程池提交异步任务，配合FutureTask实现主线程与子线程数据交互
     */
    private void exec() throws InterruptedException, ExecutionException {
        //进行异步任务列表
        List<FutureTask<Integer>> futureTasks = new ArrayList<>();
        //线程池 初始化十个线程 和JDBC连接池是一个意思 实现重用
        //Java通过Executors提供四种线程池，分别为：
        //newCachedThreadPool创建一个可缓存线程池，如果线程池长度超过处理需要，可灵活回收空闲线程，若无可回收，则新建线程。
        //newFixedThreadPool 创建一个定长线程池，可控制线程最大并发数，超出的线程会在队列中等待。
        //newScheduledThreadPool 创建一个定长线程池，支持定时及周期性任务执行。
        //newSingleThreadExecutor 创建一个单线程化的线程池，它只会用唯一的工作线程来执行任务，保证所有任务按照指定顺序(FIFO, LIFO, 优先级)执行
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        long start = System.currentTimeMillis();
        //类似与run方法的实现 Callable是一个接口，在call中手写逻辑代码
        Callable<Integer> callable = new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                Integer res = new Random().nextInt(100);
                Thread.sleep(1000);
                printMsg("任务执行:获取到结果 :" + res);
                return res;
            }
        };

        for (int i = 0; i < 10; i++) {
            //创建一个异步任务
            FutureTask<Integer> futureTask = new FutureTask<>(callable);
            futureTasks.add(futureTask);
            //提交异步任务到线程池，让线程池管理任务 特爽把。
            //由于是异步并行任务，所以这里并不会阻塞
            executorService.submit(futureTask);
        }

        int count = 0;
        for (FutureTask<Integer> futureTask : futureTasks) {
            //futureTask.get() 得到我们想要的结果
            //该方法有一个重载get(long timeout, TimeUnit unit) 第一个参数为最大等待时间，第二个为时间的单位
            count += futureTask.get();
        }
        long end = System.currentTimeMillis();
        printMsg("线程池的任务全部完成:结果为:" + count + "，main线程关闭，进行线程的清理");
        printMsg("使用时间：" + (end - start) + "ms");
        //清理线程池
        executorService.shutdown();
    }
}
