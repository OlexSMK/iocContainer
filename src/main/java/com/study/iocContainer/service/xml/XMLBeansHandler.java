package com.study.iocContainer.service.xml;

import com.study.iocContainer.entity.BeanDefinition;
import com.study.iocContainer.exception.WrongBeansXMLConfig;
import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class XMLBeansHandler extends DefaultHandler {
    private int beansDescriptionStarted = -1;
    private boolean beanOpened = false;
    private List<BeanDefinition> listOfBeanDefinitions;
    private BeanDefinition currentBeanDefinition;
    private Map<String,String> valMap;
    private Map<String,String> refMap;


    private String getAttribute(String name, Attributes attributes){
        String value = attributes.getValue(name);
        if(value ==null || value.isEmpty()){
            throw new WrongBeansXMLConfig("Attribute \"" + name + "\"" + (value == null ? "\" not found" : " is empty"));
        }
        return value;
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) {
        if(qName.equalsIgnoreCase("beans")){
            if(beansDescriptionStarted == -1){
                beansDescriptionStarted = 1;
            }
            else {
                throw new WrongBeansXMLConfig("Found more than one <beans>");
            }

        }
        else if(qName.equalsIgnoreCase("bean")){
            if(beansDescriptionStarted == 1){
                if(beanOpened){
                    throw new WrongBeansXMLConfig("Nested <bean> not supported");
                }
                valMap = new HashMap<>();
                refMap = new HashMap<>();
                String clazz = getAttribute("class",attributes);
                String id = getAttribute("id",attributes);
                currentBeanDefinition = new BeanDefinition(id,clazz,valMap,refMap);
                beanOpened = true;
            }
            else{
                throw new WrongBeansXMLConfig("Found <bean> out of <beans>");
            }
        }
        else if(qName.equalsIgnoreCase("property")){
            if(beanOpened){
                String name = getAttribute("name",attributes);
                String value = attributes.getValue("value");
                String ref = attributes.getValue("ref");
                if(value != null && ref == null){
                    valMap.put(name,value);
                }
                else if(value == null && ref != null){
                    refMap.put(name,ref);
                } else if(value != null && ref != null){
                    throw new WrongBeansXMLConfig("Found both \"ref\" and \"value\" and <property>");
                }
                else {
                    throw new WrongBeansXMLConfig("Neither \"ref\" nor \"value\" found <property>");
                }
            }
            else{
                throw new WrongBeansXMLConfig("Found <property> out of <bean>");
            }

        }
        else{
            throw new WrongBeansXMLConfig("Found unsupported tag " + qName);
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) {
        if(qName.equalsIgnoreCase("beans")){
            beansDescriptionStarted = 0;
        }
        else if(qName.equalsIgnoreCase("bean")){
            if(listOfBeanDefinitions == null){
                listOfBeanDefinitions = new ArrayList<>();
            }
            listOfBeanDefinitions.add(currentBeanDefinition);
            beanOpened = false;
        }
    }

    public List<BeanDefinition> getBeansDefinions() {
        return listOfBeanDefinitions;
    }
}
