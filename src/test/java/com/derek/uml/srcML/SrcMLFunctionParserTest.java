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