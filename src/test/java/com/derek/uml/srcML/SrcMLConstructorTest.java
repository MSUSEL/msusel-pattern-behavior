package com.derek.uml.srcML;

import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Element;

import static org.junit.Assert.*;

public class SrcMLConstructorTest extends SrcMLTest{
    private SrcMLConstructor srcMLConstructor;

    public SrcMLConstructorTest(){
        xmlFileLocation = "src/test/resources/SrcMLConstructorExample.xml";
    }

    @Before
    public void build(){
        super.build();
        Element constructorEle = (Element)doc.getElementsByTagName("constructor").item(0);
        srcMLConstructor = new SrcMLConstructor(constructorEle);
    }
    @Test
    public void verifyConstructor(){
        assertEquals(srcMLConstructor.getName(), "UrlChecker");
        assertEquals(srcMLConstructor.getSpecifiers().size(), 0);
        assertEquals(srcMLConstructor.getParameterList().getParameters().size(), 1);
        assertEquals(srcMLConstructor.getParameterList().getParameters().get(0).getDecl().getType().getName(), "TimeLimiter");
        assertEquals(srcMLConstructor.getParameterList().getParameters().get(0).getDecl().getName(), "timeLimiter");
    }


}