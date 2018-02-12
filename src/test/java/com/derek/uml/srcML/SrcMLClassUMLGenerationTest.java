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


import com.derek.uml.UMLClass;
import com.derek.uml.UMLGenerationUtils;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Element;

import static org.junit.Assert.*;
public class SrcMLClassUMLGenerationTest extends SrcMLTest{

    private SrcMLClass srcMLClass;
    private SrcMLClass srcMLClass2;
    private SrcMLClass srcMLClass3;

    public SrcMLClassUMLGenerationTest(){
        xmlFileLocation = "src/test/resources/SrcMLClassExample.xml";
    }

    @Before
    public void build(){
        super.build();
        Element classEle = (Element)doc.getElementsByTagName("class").item(0);
        Element classEle2 = (Element)doc.getElementsByTagName("class").item(1);
        Element classEle3 = (Element)doc.getElementsByTagName("class").item(3);
        srcMLClass = new SrcMLClass(classEle);
        srcMLClass2 = new SrcMLClass(classEle2);
        srcMLClass3 = new SrcMLClass(classEle3);
    }
    @Test
    public void verifyClass(){
        UMLClass umlClass = UMLGenerationUtils.getUMLClass(srcMLClass, null, null);
        assertEquals(srcMLClass.getSpecifiers().size(), 2);
        assertEquals(srcMLClass.getSpecifiers().get(0), "public");
        assertEquals(srcMLClass.getSpecifiers().get(1), "abstract");
        assertEquals(umlClass.isAbstract(), true);
        assertEquals(umlClass.getAttributes().size(), 0);
        assertEquals(umlClass.getOperations().size(), 2);
        assertEquals(umlClass.getOperations().get(0).getName(), "buildBy");
        assertEquals(umlClass.getOperations().get(0).getParameters().size(), 0);
        assertEquals(umlClass.getOperations().get(0).getStringReturnDataType(), "By");
    }
    @Test
    public void verifyClass2(){
        UMLClass umlClass = UMLGenerationUtils.getUMLClass(srcMLClass2,null, null);
        assertEquals(umlClass.isAbstract(), false);
        assertEquals(umlClass.getAttributes().size(), 21);
        assertEquals(umlClass.getConstructors().size(), 8);
        assertEquals(umlClass.getOperations().size(), 5);
    }
    @Test
    public void verifyClass3(){
        UMLClass umlClass = UMLGenerationUtils.getUMLClass(srcMLClass3,null, null);
        assertEquals(umlClass.getName(), "Anonymous Class");
        assertEquals(umlClass.getAttributes().size(), 1);
        assertEquals(umlClass.getOperations().size(), 2);

    }
}
