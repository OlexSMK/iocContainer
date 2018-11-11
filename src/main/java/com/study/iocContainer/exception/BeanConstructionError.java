package com.study.iocContainer.exception;

public class BeanConstructionError extends RuntimeException{
    public BeanConstructionError(String message){
        super(message);
    }
}
