package com.study.iocContainer.service;

public enum InitStep {
    BEFOR("postProcessBeforeInitialization"),
    AFTER("postProcessAfterInitialization");

    private String methodName;

    InitStep(String methodName){
        this.methodName = methodName;
    }

    public String getMethodName(){
        return methodName;
    }
}
