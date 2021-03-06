package com.study.iocContainer.context;

public interface ApplicationContext {
    Object getBean(String id);
    <T> T getBean(Class<T> clazz);
    <T> T getBean(String id, Class<T> clazz);
}
