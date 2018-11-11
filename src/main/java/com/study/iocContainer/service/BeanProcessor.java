package com.study.iocContainer.service;

import com.study.iocContainer.entity.Bean;
import com.study.iocContainer.entity.BeanDefinition;
import com.study.iocContainer.exception.BeanConstrunctionError;

import java.util.*;

public class BeanProcessor {
    public Map<String,Bean> contruct(List<BeanDefinition> beanDefinitions){
        Map<String,Bean> beans = constructBeans(beanDefinitions);
        injectValueDependencies(beans,beanDefinitions);
        injectRefDependencies(beans,beanDefinitions);
        return beans;
    }


    private Map<String,Bean> constructBeans(List<BeanDefinition> beanDefinitions){
        Map<String,Bean> beanSet = new HashMap<>();
        for (BeanDefinition beanDefinition : beanDefinitions){
            String id  = beanDefinition.getId();
            String className = beanDefinition.getClassName();
            Bean bean;
            if (beanSet.containsKey(id)) {
                throw new BeanConstrunctionError("Duplicate for bean's id=" + id);
            }
            try {
                Class clazz = Class.forName(className);
                Object beanValue = clazz.newInstance();
                bean  = new Bean(id,beanValue);
            } catch (Exception e) {
                e.printStackTrace();
                throw new BeanConstrunctionError("Error on creation bean id=" + id + " class=" + className);
            }
            beanSet.put(id,bean);
        }
        return beanSet;
    }

    private void injectValueDependencies(Map<String,Bean> beans, List<BeanDefinition> beanDefinitions){
        for(BeanDefinition beanDefinition : beanDefinitions){
            Object beanValue = beans.get(beanDefinition.getId()).getValue();
            try{
                BeanInjector.injectValues(beanValue,beanDefinition.getValueDependecies());
            }
            catch (Exception e){
                throw new BeanConstrunctionError("Error on value injection for id=" +beanDefinition.getId());
            }
        }
    }

    private void injectRefDependencies(Map<String,Bean> beans, List<BeanDefinition> beanDefinitions){
        for(BeanDefinition beanDefinition : beanDefinitions){
            Object beanValue = beans.get(beanDefinition.getId()).getValue();
            Map<String, Object> refBeans = new HashMap<>();
            for(Map.Entry<String, String> ref : beanDefinition.getRefDependecies().entrySet()){
                Object refBean =  beans.get(ref.getValue());
                if(refBean == null){
                    throw new BeanConstrunctionError("Not found referenced bean id=" + ref.getValue());
                }
                refBeans.put(ref.getKey(),refBean);
            }
            try{
                BeanInjector.injectRef(beanValue,refBeans);
            }
            catch (Exception e){
                throw new BeanConstrunctionError("Error on value injection for id=" +beanDefinition.getId());
            }

        }
    }
}
