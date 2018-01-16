package com.derek.uml.srcML;

import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Element;

import static org.junit.Assert.*;

public class SrcMLFunctionParserTest extends SrcMLTest{

    private SrcMLFunction srcMLFunctionParser;
    private SrcMLFunction srcMLFunctionParser2;


    public SrcMLFunctionParserTest(){
        xmlFileLocation = "src/test/resources/SrcMLFunctionExample.xml";
    }

    @Before
    public void build(){
        super.build();
        Element functionEle = (Element)doc.getElementsByTagName("function").item(0);
        Element functionEle2 = (Element)doc.getElementsByTagName("function").item(1);
        srcMLFunctionParser = new SrcMLFunction(functionEle);
        srcMLFunctionParser2 = new SrcMLFunction(functionEle2);
    }


    @Test
    public void verifyFunction(){
        assertEquals(srcMLFunctionParser.getAnnotations().size(), 2);
        assertEquals(srcMLFunctionParser.getAnnotations().get(0).getName(), "Override");
        assertEquals(srcMLFunctionParser.getAnnotations().get(1).getName(), "nonNull");
        assertEquals(srcMLFunctionParser.getSpecifiers().size(), 2);
        assertEquals(srcMLFunctionParser.getSpecifiers().get(0), "public");
        assertEquals(srcMLFunctionParser.getSpecifiers().get(1), "static");
        assertEquals(srcMLFunctionParser.getType().getName(), "int");
        assertEquals(srcMLFunctionParser.getName(), "getDataModel");
    }

    @Test
    public void verifyFunction2(){
        assertEquals(srcMLFunctionParser2.getAnnotations().size(), 0);
        assertEquals(srcMLFunctionParser2.getSpecifiers().size(), 2);
        assertEquals(srcMLFunctionParser2.getSpecifiers().get(0), "public");
        assertEquals(srcMLFunctionParser2.getSpecifiers().get(1), "static");
        assertEquals(srcMLFunctionParser2.getType().getName(), "ExpectedCondition<Boolean>");
        assertEquals(srcMLFunctionParser2.getName(), "or");
    }



}