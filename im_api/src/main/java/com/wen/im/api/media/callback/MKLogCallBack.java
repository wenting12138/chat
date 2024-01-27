package com.wen.im.api.media.callback;

import com.sun.jna.CallbackThreadInitializer;
import com.sun.jna.Native;
import com.wen.im.api.media.sdk.callback.IMKLogCallBack;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 日志输出广播
 */
public class MKLogCallBack implements IMKLogCallBack {
    private static final Logger log = LoggerFactory.getLogger(MKLogCallBack.class);
    public MKLogCallBack() {
        //回调使用同一个线程
        Native.setCallbackThreadInitializer(this, new CallbackThreadInitializer(true, false, "MediaServerLogThread"));
    }

    /**
     * 日志输出广播
     *
     * @param level    日志级别
     * @param file     源文件名
     * @param line     源文件行
     * @param function 源文件函数名
     * @param message  日志内容
     */
    public void invoke(int level, String file, int line, String function, String message) {
        switch (level) {
            case 0:
                log.trace("【MediaServer】{}", message);
                break;
            case 1:
                log.debug("【MediaServer】{}", message);
                break;
            case 2:
                log.info("【MediaServer】{}", message);
                break;
            case 3:
                log.warn("【MediaServer】{}", message);
                break;
            case 4:
                log.error("【MediaServer】{}", message);
                break;
            default:
                log.info("【MediaServer】{}", message);
                break;
        }
    }
}