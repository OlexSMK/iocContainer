package com.study.iocContainer.entity;

public class Bean {
    private String id;
    private Object value;

    public Bean(String id, Object value) {
        this.id = id;
        this.value = value;
    }

    public String getId() {
        return id;
    }

    public Object getValue() {
        return value;
    }
}
