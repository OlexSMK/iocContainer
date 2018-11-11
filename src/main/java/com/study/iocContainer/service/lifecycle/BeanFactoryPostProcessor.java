package com.study.iocContainer.service.lifecycle;

import com.study.iocContainer.entity.BeanDefinition;

import java.util.List;

public interface BeanFactoryPostProcessor {
    void postProcessBeanFactory(List<BeanDefinition> definitions);
}