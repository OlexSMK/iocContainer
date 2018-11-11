package com.study.iocContainer.service.lifecycle;

public interface BeanPostProcessor {
    Object postProcessBeforeInitialization(Object bean, String id);

    Object postProcessAfterInitialization(Object bean, String id);
}