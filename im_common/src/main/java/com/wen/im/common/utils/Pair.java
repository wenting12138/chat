package com.wen.im.common.utils;

import com.wen.im.common.model.dto.msg.UrlInfo;

public class Pair<T1, T2> {
    private T1 object1;
    private T2 object2;

    public Pair(T1 object1, T2 object2) {
        this.object1 = object1;
        this.object2 = object2;
    }

    public static <T1, T2> Pair<T1, T2> of(T1 match, T2 urlInfo) {
        return new Pair<>(match, urlInfo);
    }

    public T1 getObject1() {
        return object1;
    }

    public void setObject1(T1 object1) {
        this.object1 = object1;
    }

    public T2 getObject2() {
        return object2;
    }

    public void setObject2(T2 object2) {
        this.object2 = object2;
    }
}
