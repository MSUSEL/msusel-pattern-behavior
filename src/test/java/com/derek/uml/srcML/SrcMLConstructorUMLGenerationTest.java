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


public class SrcMLConstructorUMLGenerationTest extends SrcMLTest{
    private SrcMLConstructor srcMLConstructor;

    public SrcMLConstructorUMLGenerationTest(){
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
        UMLOperation op = UMLGenerationUtils.getUMLConstructor(srcMLConstructor);
        assertEquals(op.getName(), "UrlChecker");
        assertEquals(op.getStringParameters().size(), 1);
        assertEquals(op.getStringParameters().get(0).getKey(), "TimeLimiter");
        assertEquals(op.getStringParameters().get(0).getValue(), "timeLimiter");
    }


}