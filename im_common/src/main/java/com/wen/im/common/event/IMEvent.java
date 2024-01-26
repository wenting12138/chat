package com.wen.im.common.event;

/**
 * @author wenting
 */
public interface IMEvent {
    void process(Event event);
    int getEventType();
}
