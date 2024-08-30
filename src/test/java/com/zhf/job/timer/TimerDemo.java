package com.zhf.job.timer;

import java.util.Timer;

public class TimerDemo {
    public static void main(String[] args) {
        Timer timer = new Timer();
        TimerTaskDemo task = new TimerTaskDemo();
        timer.schedule(task, 3000L, 1000L);
    }
}
