package com.study.iocContainer.service;

import com.study.iocContainer.entity.BeanDefinition;

import java.util.List;

public interface BeanDefinitionReader {
    List<BeanDefinition> readBeanDefinition();
}
