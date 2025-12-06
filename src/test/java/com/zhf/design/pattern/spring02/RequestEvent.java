package com.zhf.design.pattern.spring02;

import org.springframework.context.ApplicationEvent;

public class RequestEvent extends ApplicationEvent {
    /**
     * Create a new {@code ApplicationEvent}.
     *
     * @param source the object on which the event initially occurred or with
     *               which the event is associated (never {@code null})
     */
    public RequestEvent(Object source) {
        super(source);
    }
}
