package com.derek.uml.srcML;

import com.derek.uml.UMLOperation;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Element;

import static org.junit.Assert.*;

public class SrcMLFunctionUMLGenerationTest extends SrcMLTest{

    private SrcMLFunction srcMLFunction;
    private SrcMLFunction srcMLFunction2;
    private SrcMLFunction srcMLFunction3;


    public SrcMLFunctionUMLGenerationTest(){
        xmlFileLocation = "src/test/resources/SrcMLFunctionExample.xml";
    }

    @Before
    public void build(){
        super.build();
        Element functionEle = (Element)doc.getElementsByTagName("function").item(0);
        Element functionEle2 = (Element)doc.getElementsByTagName("function").item(1);
        Element functionEle3 = (Element)doc.getElementsByTagName("function").item(2);
        srcMLFunction = new SrcMLFunction(functionEle);
        srcMLFunction2 = new SrcMLFunction(functionEle2);
        srcMLFunction3 = new SrcMLFunction(functionEle3);
    }


    @Test
    public void verifyUMLGeneration(){
        UMLOperation op = srcMLFunction.getOperation();
        assertEquals(op.getName(), "getDataModel");
        assertEquals(op.getReturnDataType(), "int");
        assertEquals(op.getParameters().size(), 0); //empty params
    }

    @Test
    public void verifyUMLGeneration2(){
        UMLOperation op = srcMLFunction2.getOperation();
        assertEquals(op.getName(), "or");
        assertEquals(op.getReturnDataType(), "ExpectedCondition<Boolean>");
        assertEquals(op.getParameters().size(), 1); //empty params
        assertEquals(op.getParameters().get(0).getKey(), "ExpectedCondition<?>");
        assertEquals(op.getParameters().get(0).getValue(), "conditions");
    }

    @Test
    public void verifyUMLGeneration3(){
        UMLOperation op = srcMLFunction3.getOperation();
        assertEquals(op.getName(), "apply");
        assertEquals(op.getReturnDataType(), "Boolean");
        assertEquals(op.getParameters().size(), 1); //empty params
        assertEquals(op.getParameters().get(0).getKey(), "WebDriver");
        assertEquals(op.getParameters().get(0).getValue(), "driver");
    }


}