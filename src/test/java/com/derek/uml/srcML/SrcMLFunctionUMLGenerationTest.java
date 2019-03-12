/**
 * MIT License
 *
 * Copyright (c) 2017 Montana State University, Gianforte School of Computing
 * Software Engineering Laboratory
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.derek.uml.srcML;

import com.derek.uml.UMLGenerationUtils;
import com.derek.uml.UMLOperation;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.w3c.dom.Element;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SrcMLFunctionUMLGenerationTest extends SrcMLTest{

    private SrcMLFunction srcMLFunction;
    private SrcMLFunction srcMLFunction2;
    private SrcMLFunction srcMLFunction3;


    public SrcMLFunctionUMLGenerationTest(){
        xmlFileLocation = "src/test/resources/SrcMLFunctionExample.xml";
    }

    @BeforeAll
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
        UMLOperation op = UMLGenerationUtils.getUMLOperation(srcMLFunction);
        assertEquals(op.getName(), "getDataModel");
        assertEquals(op.getStringReturnDataType(), "int");
        assertEquals(op.getStringParameters().size(), 0); //empty params
    }

    @Test
    public void verifyUMLGeneration2(){
        UMLOperation op = UMLGenerationUtils.getUMLOperation(srcMLFunction2);
        assertEquals(op.getName(), "or");
        assertEquals(op.getStringReturnDataType(), "ExpectedCondition<Boolean>");
        assertEquals(op.getStringParameters().size(), 1); //empty params
        assertEquals(op.getStringParameters().get(0).getKey(), "ExpectedCondition<?>");
        assertEquals(op.getStringParameters().get(0).getValue(), "conditions");
    }

    @Test
    public void verifyUMLGeneration3(){
        UMLOperation op = UMLGenerationUtils.getUMLOperation(srcMLFunction3);
        assertEquals(op.getName(), "apply");
        assertEquals(op.getStringReturnDataType(), "Boolean");
        assertEquals(op.getStringParameters().size(), 1); //empty params
        assertEquals(op.getStringParameters().get(0).getKey(), "WebDriver");
        assertEquals(op.getStringParameters().get(0).getValue(), "driver");
    }


}