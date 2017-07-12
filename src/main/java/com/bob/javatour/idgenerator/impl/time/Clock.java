package com.bob.javatour.idgenerator.impl.time;

/**
 * Created by wangxiang on 17/4/26.
 */
public abstract class Clock {

    /**
     * 创建系统时钟.
     *
     * @return 系统时钟
     */
    public static Clock systemClock() {
        return new SystemClock();
    }

    /**
     * 返回从纪元开始的毫秒数.
     *
     * @return 从纪元开始的毫秒数
     */
    public abstract long millis();

    private static final class SystemClock extends Clock {
        @Override
        public long millis() {
            return System.currentTimeMillis();
        }
    }
}