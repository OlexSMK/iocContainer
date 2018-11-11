package com.study.iocContainer.exception;

public class BeanConstrunctionError extends RuntimeException{
    public BeanConstrunctionError(String message){
        super(message);
    }
}
