package com.derek.uml.srcML;

import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Element;

import static org.junit.Assert.*;

public class SrcMLFunctionParserTest extends SrcMLTest{

    private SrcMLFunctionParser srcMLFunctionParser;


    public SrcMLFunctionParserTest(){
        xmlFileLocation = "src/test/resources/SrcMLFunctionExample.xml";
    }

    @Before
    public void build(){
        super.build();
        Element functionEle = (Element)doc.getElementsByTagName("function").item(0);
        srcMLFunctionParser = new SrcMLFunctionParser(functionEle);
    }


    @Test
    public void verifyAnnotations(){
       assertEquals(srcMLFunctionParser.getAnnotations().size(), 2);
       assertEquals(srcMLFunctionParser.getAnnotations().get(0), "Override");
       assertEquals(srcMLFunctionParser.getAnnotations().get(1), "nonNull");
    }

    @Test
    public void verifySpecifiers(){
        assertEquals(srcMLFunctionParser.getSpecifiers().size(), 2);
        assertEquals(srcMLFunctionParser.getSpecifiers().get(0), "public");
        assertEquals(srcMLFunctionParser.getSpecifiers().get(1), "static");
    }



}