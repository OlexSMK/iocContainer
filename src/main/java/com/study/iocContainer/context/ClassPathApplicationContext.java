package com.study.iocContainer.context;

import com.study.iocContainer.entity.Bean;
import com.study.iocContainer.entity.BeanDefinition;
import com.study.iocContainer.exception.TooManyBeansFound;
import com.study.iocContainer.service.BeanProcessor;
import com.study.iocContainer.service.BeanDefinitionReader;
import com.study.iocContainer.service.xml.XMLBeanDefinitionReader;

import java.util.List;
import java.util.Map;

public class ClassPathApplicationContext implements ApplicationContext {
    private Map<String,Bean> beans;
    private Map<String,Bean> systemBeansFactory;
    private Map<String,Bean> systemBeansPostProccesors;


    public ClassPathApplicationContext(String path){

        BeanDefinitionReader beanDefinitionReader = new XMLBeanDefinitionReader(path);
        List<BeanDefinition> beanDefinitions= beanDefinitionReader.readBeanDefinition();
        BeanProcessor beanProcessor = new BeanProcessor();
        systemBeansFactory = beanProcessor.getBeanFactoryPostProcessors(beanDefinitions);
        beanProcessor.runBeanFactoryPostProcessors(beanDefinitions,systemBeansFactory);
        beans = beanProcessor.construct(beanDefinitions);
        systemBeansPostProccesors = beanProcessor.getBeanPostProcessors(beans);
        beanProcessor.runBeanPostProcessors(beans,systemBeansPostProccesors);
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
