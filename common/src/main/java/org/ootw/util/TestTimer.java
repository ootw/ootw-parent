package org.ootw.util;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class TestTimer {


    private static final ScheduledExecutorService service = Executors.newScheduledThreadPool(5);

    public TestTimer() {
        createTask();
    }

    public void createTask() {
        service.submit(() -> task());
    }

    public void task() {
        final AtomicInteger i = new AtomicInteger(1);
        ScheduledFuture future = service.scheduleAtFixedRate(
                () -> {
                    System.out.println("正在以200ms的固定频率执行任务, 正在执行第" + (i.getAndIncrement()) + "次");
                },
                1000, 200, TimeUnit.MILLISECONDS);
        System.out.println("================");
        service.schedule(() -> {
            System.out.println("注销小周期任务");
            future.cancel(true);
        }, 10, TimeUnit.SECONDS);
        System.out.println(">>>>>>>>>>>>>>>>>>");
        synchronized (this) {
            try {
                this.wait(10 * 1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println("<<<<<<<<<<<<<<<<<<<");
        task();
    }

    public static void main(String[] args) {
        TestTimer timer = new TestTimer();
    }
}
