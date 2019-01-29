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

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.w3c.dom.Element;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class SrcMLConstructorTest extends SrcMLTest{
    private SrcMLConstructor srcMLConstructor;

    public SrcMLConstructorTest(){
        xmlFileLocation = "src/test/resources/SrcMLConstructorExample.xml";
    }

    @BeforeAll
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