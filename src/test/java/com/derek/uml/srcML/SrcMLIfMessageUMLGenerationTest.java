package com.derek.uml.srcML;

import com.derek.uml.UMLMessage;
import com.derek.uml.UMLMessageGenerationUtils;
import com.google.common.graph.MutableGraph;
import org.jboss.shrinkwrap.descriptor.api.Mutable;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Element;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class SrcMLIfMessageUMLGenerationTest extends SrcMLTest {

    private SrcMLBlock block;
    private SrcMLBlock block2;
    private SrcMLBlock block3;

    public SrcMLIfMessageUMLGenerationTest(){
        xmlFileLocation = "src/test/resources/SrcMLIfCallExample.xml";
    }

    @Before
    public void build(){
        super.build();
        Element blockEle = (Element)doc.getElementsByTagName("block").item(0);
        Element blockEle2 = (Element)doc.getElementsByTagName("block").item(2);

        block = new SrcMLBlock(blockEle);
        block2 = new SrcMLBlock(blockEle2);
    }

    @Test
    public void verifyCall1(){
        List<UMLMessage> messages = UMLMessageGenerationUtils.getUMLMessages(block);
        String verifier1 = "element.isDisplayed";
        MutableGraph<String> callForest = messages.get(0).getCallForest();
        assertEquals(callForest.nodes().size(), 1);
        assertEquals(callForest.degree(verifier1), 0);
    }

    @Test
    public void verifyCall2(){
        List<UMLMessage> messages = UMLMessageGenerationUtils.getUMLMessages(block2);
        String forest1verifier1 = "Platform.getCurrent";
        String forest1verifier2 = "is";
        MutableGraph<String> callForest1 = messages.get(0).getCallForest();
        assertEquals(callForest1.nodes().size(), 2);
        assertEquals(callForest1.degree(forest1verifier1), 1);
        assertEquals(callForest1.degree(forest1verifier2), 1);
        assertEquals(callForest1.hasEdgeConnecting(forest1verifier1, forest1verifier2), true);

        //second call tree
        MutableGraph<String> callForest2 = messages.get(1).getCallForest();
        String forest2verifier1 = "binaryLocation.getParent";
        String forest2verifier2 = "getParent";
        String forest2verifier3 = "getParent";
        String forest2verifier4 = "resolve";
        String forest2verifier5 = "resolve";
        assertEquals(callForest2.nodes().size(), 5);
        assertEquals(callForest2.degree(forest2verifier1), 1);
        assertEquals(callForest2.degree(forest2verifier2), 1);
        assertEquals(callForest2.degree(forest2verifier3), 1);
        assertEquals(callForest2.degree(forest2verifier4), 1);
        assertEquals(callForest2.degree(forest2verifier5), 1);
        assertEquals(callForest2.hasEdgeConnecting(forest2verifier1, forest2verifier2), true);
        assertEquals(callForest2.hasEdgeConnecting(forest2verifier2, forest2verifier3), true);
        assertEquals(callForest2.hasEdgeConnecting(forest2verifier3, forest2verifier4), true);
        assertEquals(callForest2.hasEdgeConnecting(forest2verifier4, forest2verifier5), true);
        assertEquals(callForest2.hasEdgeConnecting(forest2verifier1, forest2verifier3), false);
        assertEquals(callForest2.hasEdgeConnecting(forest2verifier1, forest2verifier4), false);
        assertEquals(callForest2.hasEdgeConnecting(forest2verifier1, forest2verifier5), false);
        assertEquals(callForest2.hasEdgeConnecting(forest2verifier2, forest2verifier4), false);
        assertEquals(callForest2.hasEdgeConnecting(forest2verifier2, forest2verifier5), false);
        assertEquals(callForest2.hasEdgeConnecting(forest2verifier3, forest2verifier5), false);
    }


}
