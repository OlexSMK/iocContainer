package com.study.iocContainer.service.xml;

import com.study.iocContainer.entity.BeanDefinition;
import com.study.iocContainer.exception.WrongBeansXMLConfig;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.util.List;

public class TextXMLBeanDefinitionReader {
    @Test
    public void TestDefinitionParserNoProperties(){
        String XMLdocument =
                "<beans>" +
                "<bean id=\"first\" class=\"TestClass1\"/>" +
                "<bean id=\"second\" class=\"TestClass2\">" +
                "</bean>"+
                "<bean id=\"third\" class=\"TestClass1\"/>" +
                "</beans>";
        XMLBeanDefinitionReader xmlBeanDefinitionReader = new XMLBeanDefinitionReader("test");
        List<BeanDefinition> beanDefinitions = xmlBeanDefinitionReader.getBeanDefinitions(new ByteArrayInputStream(XMLdocument.getBytes()));
        Assert.assertEquals("Beans definitions",3,beanDefinitions.size());
        Assert.assertEquals("Bean definitions#1 id","first",beanDefinitions.get(0).getId());
        Assert.assertEquals("Beans definitions#1 class","TestClass1",beanDefinitions.get(0).getClassName());
        Assert.assertEquals("Bean definitions#2 id","second",beanDefinitions.get(1).getId());
        Assert.assertEquals("Beans definitions#2 class","TestClass2",beanDefinitions.get(1).getClassName());
        Assert.assertEquals("Bean definitions#1 id","third",beanDefinitions.get(2).getId());
        Assert.assertEquals("Beans definitions#1 class","TestClass1",beanDefinitions.get(2).getClassName());

    }
    @Test
    public void TestDefinitionParserCheckProperties(){
        String XMLdocument =
                "<beans>" +
               "<bean id=\"first\" class=\"TestClass1\">" +
               "<property name=\"p1\" value=\"123\"/>" +
               "<property name=\"p2\" value=\"abc\"/>" +
               "<property name=\"p3\" value=\"0.5\"/>" +
               "<property name=\"ref1\" ref=\"newOne\"/>"+
               "<property name=\"ref2\" ref=\"oldTwo\"/>"+
               "</bean>"+
               "</beans>";

        XMLBeanDefinitionReader xmlBeanDefinitionReader = new XMLBeanDefinitionReader("test");
        List<BeanDefinition> beanDefinitions = xmlBeanDefinitionReader.getBeanDefinitions(new ByteArrayInputStream(XMLdocument.getBytes()));
        Assert.assertEquals("Just one beans definition",1,beanDefinitions.size());

        Assert.assertEquals("Bean definitions id","first",beanDefinitions.get(0).getId());
        Assert.assertEquals("Beans definitions class","TestClass1",beanDefinitions.get(0).getClassName());
        Assert.assertEquals("Bean definitions values number",3,beanDefinitions.get(0).getValueDependecies().size());
        Assert.assertEquals("Bean definitions ref number",2,beanDefinitions.get(0).getRefDependecies().size());
        Assert.assertEquals("Bean definitions value for p1","123",beanDefinitions.get(0).getValueDependecies().get("p1"));
        Assert.assertEquals("Bean definitions value for p2","abc",beanDefinitions.get(0).getValueDependecies().get("p2"));
        Assert.assertEquals("Bean definitions value for p3","0.5",beanDefinitions.get(0).getValueDependecies().get("p3"));
        Assert.assertEquals("Bean definitions reference for ref1","newOne",beanDefinitions.get(0).getRefDependecies().get("ref1"));
        Assert.assertEquals("Bean definitions reference for ref2","oldTwo",beanDefinitions.get(0).getRefDependecies().get("ref2"));


    }

    @Test(expected = WrongBeansXMLConfig.class)
    public void TestDefinitionParserWrongConfig(){
        String XMLdocument =
                "<beans>" +
                        "<bean id=\"first\" class=\"TestClass1\">" +
                        "<property name=\"ref2\" ref=\"oldTwo\" value=\"123\">"+
                        "</bean>"+
                        "</beans>";

        XMLBeanDefinitionReader xmlBeanDefinitionReader = new XMLBeanDefinitionReader("test");
        List<BeanDefinition> beanDefinitions = xmlBeanDefinitionReader.getBeanDefinitions(new ByteArrayInputStream(XMLdocument.getBytes()));
    }
}
