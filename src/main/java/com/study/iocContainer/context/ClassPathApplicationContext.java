package com.study.iocContainer.context;

import com.study.iocContainer.entity.Bean;
import com.study.iocContainer.entity.BeanDefinition;
import com.study.iocContainer.exception.TooManyBeansFound;
import com.study.iocContainer.service.BeanProcessor;
import com.study.iocContainer.service.BeanDefinitionReader;
import com.study.iocContainer.service.xml.XMLBeanDefinitionReader;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClassPathApplicationContext implements ApplicationContext {
    Map<String,Bean> beans;
    Map<String,Bean> systemBeans;


    public ClassPathApplicationContext(String path){

        BeanDefinitionReader beanDefinitionReader = new XMLBeanDefinitionReader(path);
        List<BeanDefinition> beanDefinitions= beanDefinitionReader.readBeanDefinition();
        BeanProcessor beanProcessor = new BeanProcessor();
        beans = beanProcessor.contruct(beanDefinitions);


    }

    @Override
    public Object getBean(String id) {
        return beans.get(id);
    }

    @Override
    public <T> T getBean(Class<T> clazz) {
        Object finded = null;
        for (Bean bean: beans.values()) {
            if(bean.getValue().getClass().isAssignableFrom( clazz)){
                if(finded == null){
                    finded = bean.getValue();
                }
                else{
                    throw new TooManyBeansFound("More than one bean found for " + clazz.getName());
                }
            }
        }
        return clazz.cast(finded);
    }

    @Override
    public <T> T getBean(String id, Class<T> clazz) {
        for(Bean bean: beans.values()){
            if(bean.getId().equals(id) && bean.getValue().getClass().isAssignableFrom(clazz)){
                return clazz.cast(bean.getValue());
            }
        }
        return null;
    }

}
