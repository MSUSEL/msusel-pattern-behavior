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

import jdk.nashorn.internal.ir.annotations.Ignore;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.w3c.dom.Element;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SrcMLIfMessageUMLGenerationTest extends SrcMLTest {

    private SrcMLBlock block;
    private SrcMLBlock block2;
    private SrcMLBlock block3;

    public SrcMLIfMessageUMLGenerationTest(){
        xmlFileLocation = "src/test/resources/SrcMLIfCallExample.xml";
    }

    @BeforeAll
    public void build(){
        super.build();
        Element blockEle = (Element)doc.getElementsByTagName("block").item(0);
        Element blockEle2 = (Element)doc.getElementsByTagName("block").item(2);

        block = new SrcMLBlock(blockEle);
        block2 = new SrcMLBlock(blockEle2);
    }

    @Ignore
    @Test
    public void verifyCall1(){
//        List<UMLMessage> messages = UMLMessageGenerationUtils.getUMLMessages(block);
//        String verifier1 = "element.isDisplayed";
//        MutableGraph<String> callForest = messages.get(0).getCallForest();
//        assertEquals(callForest.nodes().size(), 1);
//        assertEquals(callForest.degree(verifier1), 0);
    }
    @Ignore
    @Test
    public void verifyCall2(){
//        List<UMLMessage> messages = UMLMessageGenerationUtils.getUMLMessages(block2);
//        String forest1verifier1 = "Platform.getCurrent";
//        String forest1verifier2 = "Platform.getCurrent.is";
//        MutableGraph<String> callForest1 = messages.get(0).getCallForest();
//        assertEquals(callForest1.nodes().size(), 2);
//        assertEquals(callForest1.degree(forest1verifier1), 1);
//        assertEquals(callForest1.degree(forest1verifier2), 1);
//        assertEquals(callForest1.hasEdgeConnecting(forest1verifier1, forest1verifier2), true);
//
//        //second call tree
//        MutableGraph<String> callForest2 = messages.get(1).getCallForest();
//        String forest2verifier1 = "binaryLocation.getParent";
//        String forest2verifier2 = "binaryLocation.getParent.getParent";
//        String forest2verifier3 = "binaryLocation.getParent.getParent.resolve";
//        String forest2verifier4 = "binaryLocation.getParent.getParent.resolve.resolve";
//        assertEquals(callForest2.nodes().size(), 4);
//        assertEquals(callForest2.degree(forest2verifier1), 1);
//        assertEquals(callForest2.degree(forest2verifier2), 2);
//        assertEquals(callForest2.degree(forest2verifier3), 2);
//        assertEquals(callForest2.degree(forest2verifier4), 1);
//        assertEquals(callForest2.hasEdgeConnecting(forest2verifier1, forest2verifier2), true);
//        assertEquals(callForest2.hasEdgeConnecting(forest2verifier2, forest2verifier3), true);
//        assertEquals(callForest2.hasEdgeConnecting(forest2verifier3, forest2verifier4), true);
//        assertEquals(callForest2.hasEdgeConnecting(forest2verifier1, forest2verifier3), false);
//        assertEquals(callForest2.hasEdgeConnecting(forest2verifier1, forest2verifier4), false);
//        assertEquals(callForest2.hasEdgeConnecting(forest2verifier2, forest2verifier4), false);
//
//        //third call tree
//        MutableGraph<String> callForest3 = messages.get(2).getCallForest();
//        String forest3verifier1 = "binaryLocation.getParent";
//        String forest3verifier2 = "binaryLocation.getParent.resolve";
//        assertEquals(callForest3.nodes().size(), 2);
//        assertEquals(callForest3.degree(forest3verifier1), 1);
//        assertEquals(callForest3.degree(forest3verifier2), 1);
//        assertEquals(callForest3.hasEdgeConnecting(forest3verifier1, forest3verifier2), true);
    }


}
