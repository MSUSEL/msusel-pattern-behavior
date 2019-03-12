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

public class SrcMLNameTest extends SrcMLTest{
    private SrcMLName srcMLName1;
    private SrcMLName srcMLName2;
    private SrcMLName srcMLName3;
    private SrcMLName srcMLName4;
    private SrcMLName srcMLName5;


    public SrcMLNameTest(){
        xmlFileLocation = "src/test/resources/SrcMLNameExample.xml";
    }

    @BeforeAll
    public void build(){
        super.build();
        Element nameEle1 = (Element) doc.getElementsByTagName("name").item(0);
        Element nameEle2 = (Element) doc.getElementsByTagName("name").item(1);
        Element nameEle3 = (Element) doc.getElementsByTagName("name").item(4);
        Element nameEle4 = (Element) doc.getElementsByTagName("name").item(9);
        Element nameEle5 = (Element) doc.getElementsByTagName("name").item(12);
        srcMLName1 = new SrcMLName(nameEle1);
        srcMLName2 = new SrcMLName(nameEle2);
        srcMLName3 = new SrcMLName(nameEle3);
        srcMLName4 = new SrcMLName(nameEle4);
        srcMLName5 = new SrcMLName(nameEle5);
    }


    @Test
    public void verifyName(){
        assertEquals(srcMLName1.getNames().size(), 1);
        assertEquals(srcMLName1.getNames().get(0), "First");
        assertEquals(srcMLName1.getName(), "First");
    }
    @Test
    public void verifyName2(){
        assertEquals(srcMLName2.getNames().size(), 1);
        assertEquals(srcMLName2.getName(), "Optional<Result>");
    }
    @Test
    public void verifyName3(){
        assertEquals(srcMLName3.getNames().size(), 1);
        assertEquals(srcMLName3.getName(), "ExpectedCondition<List<WebElement>>");
    }
    @Test
    public void verifyName4(){
        assertEquals(srcMLName4.getNames().size(), 2);
        assertEquals(srcMLName4.getNames().get(0), "element");
        assertEquals(srcMLName4.getNames().get(1), "isEnabled");
        assertEquals(srcMLName4.getName(), "element.isEnabled");
    }

    @Test
    public void verifyOperators(){
        assertEquals(srcMLName1.getOperators().size(), 0);
        assertEquals(srcMLName2.getOperators().size(), 0);
        assertEquals(srcMLName3.getOperators().size(), 0);
        assertEquals(srcMLName4.getOperators().size(), 1);
        assertEquals(srcMLName4.getOperators().get(0), ".");
    }
    @Test
    public void verifyName5(){
        assertEquals(srcMLName5.getNames().size(), 1);
        assertEquals(srcMLName5.getName(), "ExpectedCondition<List<WebElement<tester>>>");
    }

}