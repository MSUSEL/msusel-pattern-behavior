package com.derek.uml.srcML;

import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Element;

import static org.junit.Assert.*;

public class SrcMLFunctionParserTest extends SrcMLTest{

    private SrcMLFunction srcMLFunctionParser;


    public SrcMLFunctionParserTest(){
        xmlFileLocation = "src/test/resources/SrcMLFunctionExample.xml";
    }

    @Before
    public void build(){
        super.build();
        Element functionEle = (Element)doc.getElementsByTagName("function").item(0);
        srcMLFunctionParser = new SrcMLFunction(functionEle);
    }


    @Test
    public void verifyAnnotations(){
       assertEquals(srcMLFunctionParser.getAnnotations().size(), 2);
       assertEquals(srcMLFunctionParser.getAnnotations().get(0).getName(), "Override");
       assertEquals(srcMLFunctionParser.getAnnotations().get(1).getName(), "nonNull");
    }

    @Test
    public void verifySpecifiers(){
        assertEquals(srcMLFunctionParser.getSpecifiers().size(), 2);
        assertEquals(srcMLFunctionParser.getSpecifiers().get(0), "public");
        assertEquals(srcMLFunctionParser.getSpecifiers().get(1), "static");
    }



}