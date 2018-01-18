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

import com.derek.uml.UMLAttribute;
import com.derek.uml.UMLGenerationUtils;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Element;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class SrcMLDeclStmtUMLGenerationTest extends SrcMLTest{

    private SrcMLBlock srcMLBlock;
    private SrcMLDeclStmt srcMLDeclStmt2;
    private SrcMLDeclStmt srcMLDeclStmt3;

    public SrcMLDeclStmtUMLGenerationTest(){
        xmlFileLocation = "src/test/resources/SrcMLDeclStmtExample.xml";
    }

    @Before
    public void build(){
        super.build();
        Element blockEle = (Element)doc.getElementsByTagName("block").item(0);
        //Element declStmtEle2 = (Element)doc.getElementsByTagName("decl_stmt").item(1);
        //Element declStmtEle3 = (Element)doc.getElementsByTagName("decl_stmt").item(2);
        srcMLBlock = new SrcMLBlock(blockEle);
        //srcMLDeclStmt2 = new SrcMLDeclStmt(declStmtEle2);
        //srcMLDeclStmt3 = new SrcMLDeclStmt(declStmtEle3);
    }

    @Test
    public void verifyDeclStmt1(){
        List<UMLAttribute> atts = UMLGenerationUtils.getUMLAttributes(srcMLBlock);
        assertEquals(atts.size(), 1);
        assertEquals(atts.get(0).getDataType(), "String");
        assertEquals(atts.get(0).getName(), "HOST");
        //test atts
    }

}