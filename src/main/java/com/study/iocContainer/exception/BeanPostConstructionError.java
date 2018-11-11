package com.study.iocContainer.exception;

public class BeanPostConstructionError extends RuntimeException {
    public BeanPostConstructionError(String message){
        super(message);
    }
}
