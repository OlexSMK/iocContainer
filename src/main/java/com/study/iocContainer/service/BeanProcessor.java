package com.study.iocContainer.service;

import com.study.iocContainer.entity.Bean;
import com.study.iocContainer.entity.BeanDefinition;
import com.study.iocContainer.exception.BeanConstructionError;
import com.study.iocContainer.exception.BeanPostConstructionError;
import com.study.iocContainer.service.lifecycle.BeanFactoryPostProcessor;
import com.study.iocContainer.service.lifecycle.BeanPostProcessor;

import javax.annotation.PostConstruct;
import java.lang.reflect.Method;
import java.util.*;

public class BeanProcessor {
    public Map<String,Bean> construct(List<BeanDefinition> beanDefinitions){
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
                throw new BeanConstructionError("Duplicate for bean's id=" + id);
            }
            try {
                Class clazz = Class.forName(className);
                Object beanValue = clazz.newInstance();
                bean  = new Bean(id,beanValue);
            } catch (Exception e) {
                e.printStackTrace();
                throw new BeanConstructionError("Error on creation bean id=" + id + " class=" + className);
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
                throw new BeanConstructionError("Error on value injection for id=" +beanDefinition.getId());
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
                    throw new BeanConstructionError("Not found referenced bean id=" + ref.getValue());
                }
                refBeans.put(ref.getKey(),refBean);
            }
            try{
                BeanInjector.injectRef(beanValue,refBeans);
            }
            catch (Exception e){
                throw new BeanConstructionError("Error on value injection for id=" +beanDefinition.getId());
            }

        }
    }

    public Map<String,Bean> getBeanFactoryPostProcessors(List<BeanDefinition> beanDefinitions){
        List<BeanDefinition> systemBeanDefinitions = new ArrayList<>();
        Iterator<BeanDefinition> iterator = beanDefinitions.iterator();
        while(iterator.hasNext()){
            BeanDefinition beanDefinition = iterator.next();
            if(isBeanFactoryPostProcessors(beanDefinition.getClassName())){
                systemBeanDefinitions.add(beanDefinition);
                iterator.remove();
            }
        }
        return construct(systemBeanDefinitions);
    }

    public void runBeanFactoryPostProcessors(List<BeanDefinition> beanDefinitions, Map<String,Bean> factories){
        for(Bean bean: factories.values()){
            if(bean.getValue().getClass().isAssignableFrom(BeanFactoryPostProcessor.class)){
                try {
                    Method postConstruct = bean.getValue().getClass().getMethod("postProcessBeanFactory");
                    postConstruct.invoke(bean.getValue(),beanDefinitions);
                } catch (Exception e) {
                    e.printStackTrace();
                    throw new BeanPostConstructionError("Fail to execute postProcessBeanFactory for bean id=" + bean.getId());
                }
            }
            else{
                throw new BeanPostConstructionError("Not implementor of BeanFactoryPostProcessor bean id=" + bean.getId());
            }
        }
    }

    public Map<String,Bean> getBeanPostProcessors(Map<String,Bean> beans){
        Map<String,Bean> systemBeans = new HashMap<>();
        Iterator<Bean>  iterator = beans.values().iterator();
        while(iterator.hasNext()){
            Bean currentBean = iterator.next();
            if(currentBean.getClass().isAssignableFrom(BeanPostProcessor.class)){
                systemBeans.put(currentBean.getId(),currentBean);
                iterator.remove();
            }
        }
        return systemBeans;
    }

    public void runBeanPostProcessors(Map<String,Bean> beans,Map<String,Bean> beanPostProcessors){
        for(Bean beanPostProcessor: beanPostProcessors.values()){
            for(Bean bean:beans.values()){
                bean.setValue(doPostProcessingInitialization(bean,beanPostProcessor, InitStep.BEFOR));
            }
        }
        for(Bean bean:beans.values()){
            doPostConstruct(bean);
        }

        for(Bean beanPostProcessor: beanPostProcessors.values()){
            for(Bean bean:beans.values()){
                bean.setValue(doPostProcessingInitialization(bean,beanPostProcessor,InitStep.AFTER));
            }
        }
    }

    private boolean isBeanFactoryPostProcessors(String ClassName){
        try{
            return Class.forName(ClassName).isAssignableFrom(BeanFactoryPostProcessor.class);
        }
//        catch (Exception e){
//            throw new BeanPostConstructionError("Error on lookup for class=" + ClassName);}
         catch (ClassNotFoundException e) {
            return false;
        }
    }

    private Object doPostProcessingInitialization(Bean bean, Bean beanPostProcesssor, InitStep step){
        Object postProcessor = beanPostProcesssor.getValue();
        Object result;
        if(postProcessor.getClass().isAssignableFrom(BeanPostProcessor.class)){
            try {
                Method method = postProcessor.getClass().getMethod(step.getMethodName());
                result = method.invoke(postProcessor,bean,bean.getId());
                return result;
            }catch(Exception e){
                e.printStackTrace();
                throw new BeanPostConstructionError("Error on invocation of " + step.getMethodName() + " id=" + beanPostProcesssor.getId()
                        + " for bean id=" + bean.getId());
            }
        }
        else{
            throw new BeanPostConstructionError("Not implementor of BeanPostProcessor bean id=" + bean.getId());
        }
    }

    private void doPostConstruct(Bean target){
        for(Method method:target.getValue().getClass().getDeclaredMethods()){
            if(method.isAnnotationPresent(PostConstruct.class)){
                try {
                    method.invoke(target);
                } catch (Exception e) {
                    e.printStackTrace();
                    throw new BeanPostConstructionError("Error on execution PostConstruct method " + method.getName() + " id=" + target.getId());
                }
            }
        }
    }
}
