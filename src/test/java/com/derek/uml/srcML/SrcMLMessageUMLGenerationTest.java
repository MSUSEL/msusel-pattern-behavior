package com.derek.uml.srcML;

import com.derek.uml.UMLGenerationUtils;
import com.derek.uml.UMLMessage;
import com.derek.uml.UMLMessageGenerationUtils;
import com.google.common.graph.MutableGraph;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Element;

import java.util.List;

import static org.junit.Assert.*;

//I'm beginning to realize that this is going to be a tough class to test because I will need to treat it similar to integration tests..
//I basically need all files in a call chain to properly generate a sequence diagram.
//I'll leave this class here for now but likely I'll discard it.
public class SrcMLMessageUMLGenerationTest extends SrcMLTest {

    private SrcMLBlock block;
    private SrcMLBlock block2;
    private SrcMLBlock block3;

    public SrcMLMessageUMLGenerationTest(){
        xmlFileLocation = "src/test/resources/SrcMLCallExample.xml";
    }

    @Before
    public void build(){
        super.build();
        Element blockEle = (Element)doc.getElementsByTagName("block").item(0);
        Element blockEle2 = (Element)doc.getElementsByTagName("block").item(1);
        Element blockEle3 = (Element)doc.getElementsByTagName("block").item(2);

        block = new SrcMLBlock(blockEle);
        block2 = new SrcMLBlock(blockEle2);
        block3 = new SrcMLBlock(blockEle3);
    }

    @Test
    public void verifyCall1(){
        List<SrcMLExpression> expressions = block.getDeclStmts().get(0).getDecls().get(0).getInit().getExpressions();
        UMLMessage message = UMLMessageGenerationUtils.getUMLMessage(expressions);
        String verifier1 = "FirefoxBinary.class.getPackage";
        String verifier2 = "getName";
        String verifier3 = "replace";
        String verifier4 = "systemBinary.getChannel";
        MutableGraph<String> callForest = message.getCallForest();
        assertEquals(callForest.nodes().size(), 4);
        assertEquals(callForest.hasEdgeConnecting(verifier1,verifier2), true);
        assertEquals(callForest.hasEdgeConnecting(verifier1,verifier3), false);
        assertEquals(callForest.hasEdgeConnecting(verifier1,verifier4), false);
        assertEquals(callForest.hasEdgeConnecting(verifier2,verifier3), true);
        assertEquals(callForest.hasEdgeConnecting(verifier2,verifier4), false);
        assertEquals(callForest.hasEdgeConnecting(verifier3,verifier4), true);
    }

    @Test
    public void verifyCall2(){
        List<SrcMLExpression> expressions = block2.getExpr_stmts().get(0).getExpressions();
        UMLMessage message = UMLMessageGenerationUtils.getUMLMessage(expressions);
        MutableGraph<String> callForest = message.getCallForest();
        assertEquals(callForest.nodes().size(), 1);
        assertEquals(callForest.degree("Executable"), 0);
    }

    //TODO
    @Test
    public void verifyCall3(){
        List<SrcMLExpression> expressions = block3.getExpr_stmts().get(0).getExpressions();
        UMLMessage message = UMLMessageGenerationUtils.getUMLMessage(expressions);
        String verifier1 = "locateFirefoxBinaryFromSystemProperty";
        MutableGraph<String> callForest = message.getCallForest();
        assertEquals(callForest.nodes().size(), 2);
        assertEquals(callForest.degree("Executable"), 0);
    }

}
