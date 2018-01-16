package com.derek.uml.srcML;

import com.derek.uml.UMLGenerationUtils;
import com.derek.uml.UMLOperation;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Element;

import static org.junit.Assert.assertEquals;

public class SrcMLConstructorUMLGenerationTest extends SrcMLTest{
    private SrcMLConstructor srcMLConstructor;

    public SrcMLConstructorUMLGenerationTest(){
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
        UMLOperation op = UMLGenerationUtils.getUMLConstructor(srcMLConstructor);
        assertEquals(op.getName(), "UrlChecker");
        assertEquals(op.getParameters().size(), 1);
        assertEquals(op.getParameters().get(0).getKey(), "TimeLimiter");
        assertEquals(op.getParameters().get(0).getValue(), "timeLimiter");
    }


}