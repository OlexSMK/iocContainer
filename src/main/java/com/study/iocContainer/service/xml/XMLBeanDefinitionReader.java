package com.study.iocContainer.service.xml;

import com.google.common.annotations.VisibleForTesting;
import com.study.iocContainer.entity.BeanDefinition;
import com.study.iocContainer.exception.WrongBeansXMLConfig;
import com.study.iocContainer.service.BeanDefinitionReader;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.*;
import java.util.List;

public class XMLBeanDefinitionReader implements BeanDefinitionReader {
    private String xmlFileName;

    public XMLBeanDefinitionReader(String xmlFileName) {
        this.xmlFileName = xmlFileName;

    }

    @VisibleForTesting
    InputStream getXMLContent(String file){
        InputStream xmlContent;
        File xmlFile = new File(file);
        if(xmlFile.exists() && xmlFile.isFile()){
            try {
                xmlContent = new FileInputStream(file);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                throw new WrongBeansXMLConfig("XML content file not found " + file);
            }
        }
        else{
            xmlContent = this.getClass().getClassLoader().getResourceAsStream(file);
            if(xmlContent == null){
                throw new WrongBeansXMLConfig("XML content file not found " + file);
            }
        }
        return xmlContent;
    }

    @Override
    public List<BeanDefinition> readBeanDefinition() {
        return getBeanDefinitions(getXMLContent(xmlFileName));
    }

    @VisibleForTesting
    List<BeanDefinition> getBeanDefinitions(InputStream xmlBeans){
        List<BeanDefinition> listOfBeansDefinitions;
        SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
        try {
            SAXParser saxParser = saxParserFactory.newSAXParser();
            XMLBeansHandler XMLBeansHandler = new XMLBeansHandler();
            saxParser.parse(xmlBeans, XMLBeansHandler);
            listOfBeansDefinitions = XMLBeansHandler.getBeansDefinions();

        } catch (ParserConfigurationException|SAXException | IOException  e) {
            e.printStackTrace();
            throw new RuntimeException("Error on parsing XML ",e);
        }
        return listOfBeansDefinitions;
    }
}
