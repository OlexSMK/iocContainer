package com.study.iocContainer.exception;

public class WrongBeansXMLConfig extends RuntimeException {
    public WrongBeansXMLConfig(String message){
        super(message);
    }
}
