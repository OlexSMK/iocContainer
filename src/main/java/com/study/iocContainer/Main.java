package com.study.iocContainer;

import com.study.iocContainer.context.ApplicationContext;
import com.study.iocContainer.context.ClassPathApplicationContext;
import com.study.iocContainer.service.BeanDefinitionReader;
import com.study.iocContainer.service.xml.XMLBeanDefinitionReader;

import java.lang.reflect.Parameter;

public class Main {

    public static void main(String[] args) {

        /*String myXML = "<beans>" +
                "<bean id=\"first\" class=\"MyClass\">" +
                "<property name=\"val1\" value=\"123\"/>" +
                "<property name=\"xxxx\" value=\"something\"/>" +
                "<bean/>"+
                "<bean id=\"second\" class=\"MyClass\">" +
                "<property name=\"ddd\" value=\"456\"/>" +
                "<property name=\"xxxx\" ref=\"first\"/>" +
                "<beans/>";
        BeanDefinitionReader beanDefinitionReader = new XMLBeanDefinitionReader("\\123.xml");
        System.out.println(beanDefinitionReader.readBeanDefinition());
*/
        ApplicationContext applicationContext = new ClassPathApplicationContext("123.xml");

    }
}
