package com.study.iocContainer.entity;

import java.util.Map;

public class BeanDefinition {
    private String id;
    private String className;
    private Map<String,String> valueDependecies;
    private Map<String,String> refDependecies;

    public BeanDefinition(String id, String className, Map<String, String> valueDependecies, Map<String, String> refDependecies) {
        this.id = id;
        this.className = className;
        this.valueDependecies = valueDependecies;
        this.refDependecies = refDependecies;
    }

    public String getId() {
        return id;
    }

    public String getClassName() {
        return className;
    }

    public Map<String, String> getValueDependecies() {
        return valueDependecies;
    }

    public Map<String, String> getRefDependecies() {
        return refDependecies;
    }

    @Override
    public String toString() {
        return "BeanDefinition{" +
                "id='" + id + '\'' +
                ", className='" + className + '\'' +
                ", valueDependecies=" + valueDependecies.toString() +
                ", refDependecies=" + refDependecies.toString() +
                '}';
    }
}
