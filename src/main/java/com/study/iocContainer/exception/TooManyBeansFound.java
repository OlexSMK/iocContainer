package com.study.iocContainer.exception;

public class TooManyBeansFound extends RuntimeException {
    public TooManyBeansFound(String message){
        super(message);
    }
}
