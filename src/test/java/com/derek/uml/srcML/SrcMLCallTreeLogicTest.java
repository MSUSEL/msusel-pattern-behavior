package com.derek.uml.srcML;

import com.google.common.graph.MutableGraph;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Element;

import static junit.framework.TestCase.assertEquals;

public class SrcMLCallTreeLogicTest extends SrcMLTest {
    private SrcMLBlock block;
    private SrcMLBlock block2;
    private SrcMLBlock block3;

    public SrcMLCallTreeLogicTest(){
        xmlFileLocation = "src/test/resources/SrcMLCallExample.xml";
    }

    @Before
    public void build(){
        super.build();
        Element blockEle = (Element)doc.getElementsByTagName("block").item(0);
        Element blockEle2 = (Element)doc.getElementsByTagName("block").item(1);

        block = new SrcMLBlock(blockEle);
        block2 = new SrcMLBlock(blockEle2);
    }

    @Test
    public void verifyExpr(){
        assertEquals(block.getExpr_stmts().size(), 1);
        assertEquals(block.getExpr_stmts().get(0).getExpressions().size(), 1);
        SrcMLExpression parentExpression = block.getExpr_stmts().get(0).getExpressions().get(0);
        MutableGraph<SrcMLNode> callTree = block.getExpr_stmts().get(0).getExpressionPaths().get(0);
        SrcMLCall command = parentExpression.getCalls().get(0);
        SrcMLCall getExtraEnv = command.getArgumentList().getArguments().get(0).getExpression().getCalls().get(0);
        SrcMLCall get = command.getArgumentList().getArguments().get(0).getExpression().getCalls().get(1);
        SrcMLCall commandLine = get.getArgumentList().getArguments().get(0).getExpression().getCalls().get(0);
        assertEquals(callTree.nodes().size(), 4);
        assertEquals(callTree.hasEdgeConnecting(command, getExtraEnv), true);
        assertEquals(callTree.hasEdgeConnecting(command, get), true);
        assertEquals(callTree.hasEdgeConnecting(get, commandLine), true);
    }

    @Test
    public void verifyDecl(){
        assertEquals(block2.getDeclStmts().size(), 1);
        assertEquals(block2.getDeclStmts().get(0).getDecls().size(), 1);
        assertEquals(block2.getDeclStmts().get(0).getDecls().get(0).getInit().getExpressions().size(), 1);
        SrcMLExpression parentExpression = block2.getDeclStmts().get(0).getDecls().get(0).getInit().getExpressions().get(0);
        MutableGraph<SrcMLNode> callTree = block2.getDeclStmts().get(0).getDecls().get(0).getInit().getExpressionPaths().get(0);
        //assertEquals(callTree.toString(), "");
        SrcMLCall firefoxBinary = parentExpression.getCalls().get(0);
        SrcMLCall getName = parentExpression.getCalls().get(1);
        SrcMLCall replace = parentExpression.getCalls().get(2);
        SrcMLCall systemBinary = replace.getArgumentList().getArguments().get(1).getExpression().getCalls().get(0);
        assertEquals(callTree.nodes().size(), 4);
        assertEquals(callTree.hasEdgeConnecting(firefoxBinary, getName), true);
        assertEquals(callTree.hasEdgeConnecting(replace, systemBinary), true);
    }
}
