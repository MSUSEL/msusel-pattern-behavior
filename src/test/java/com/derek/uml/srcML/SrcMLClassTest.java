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

import com.derek.uml.*;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.w3c.dom.Element;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SrcMLClassTest extends SrcMLTest {
    //this test is a bit more of an integration test....
    // This will read in an entire file and ensure that everything (structural and behavioral) is being parsed and captured via data structures.

    private SrcMLBlock root;
    private UMLClass classifier;
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();

    public SrcMLClassTest(){
        xmlFileLocation = "src/test/resources/Executable.xml";
    }

    @BeforeAll
    public void build(){
        super.build();
        Element rootEle = (Element)doc.getElementsByTagName("unit").item(0);
        root = new SrcMLBlock(rootEle);
        List<String> residingPackage = UMLGenerationUtils.getResidingPackage(root);
        List<List<String>> imports = UMLGenerationUtils.getImports(root);
        SrcMLClass srcMLClass = root.getClasses().get(0);
        classifier = UMLGenerationUtils.getUMLClass(srcMLClass, residingPackage, imports);

        System.setOut(new PrintStream(outContent));
    }

    @Test
    public void verifyStructural(){
        assertEquals("Executable", classifier.getName());

    }

    @Test
    public void veriftyAttributes(){
        List<UMLAttribute> attributes = classifier.getAttributes();
        assertEquals("File", attributes.get(0).getStringDataType());
        assertEquals("binary", attributes.get(0).getName());
        assertEquals("String", attributes.get(1).getStringDataType());
        assertEquals("version", attributes.get(1).getName());
        assertEquals("FirefoxBinary.Channel", attributes.get(2).getStringDataType());
        assertEquals("channel", attributes.get(2).getName());
    }

    @Test
    public void verifyConstructorStructure(){
        List<UMLOperation> constructors = classifier.getConstructors();
        assertEquals(1, constructors.size());
        assertEquals("null", constructors.get(0).getStringReturnDataType());
        assertEquals(1, constructors.get(0).getStringParameters().size());
        assertEquals("File", constructors.get(0).getStringParameters().get(0).getKey());
        assertEquals("userSpecifiedBinaryPath", constructors.get(0).getStringParameters().get(0).getValue());
    }

    @Test
    public void verifyConstructorCalls(){
        List<UMLOperation> constructors = classifier.getConstructors();
        CallTreeNode<String> callTree = constructors.get(0).getCallTreeString();
        CallTreeNode<SrcMLNode> callTreeNode = root.getClasses().get(0).getBlock().getConstructors().get(0).getCallTree();
        callTreeNode.printTree();
        //assertEquals("", outContent.toString());

    }

    @AfterAll
    public void after(){
        System.setOut(System.out);
    }
}
