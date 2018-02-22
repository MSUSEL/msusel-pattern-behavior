package com.derek.uml.srcML;

import com.derek.uml.CallTreeNode;
import com.google.common.graph.MutableGraph;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.w3c.dom.Element;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static junit.framework.TestCase.assertEquals;

public class SrcMLCallTreeLogicTest extends SrcMLTest {
    private SrcMLBlock block;
    private SrcMLBlock block2;
    private SrcMLBlock block3;
    //testing console output
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();

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
        System.setOut(new PrintStream(outContent));
    }

    @Test
    public void verifyExpr(){
        assertEquals(block.getExpr_stmts().size(), 1);
        assertEquals(block.getExpr_stmts().get(0).getExpressions().size(), 1);
        SrcMLExpression parentExpression = block.getExpr_stmts().get(0).getExpressions().get(0);
        CallTreeNode<SrcMLNode> callTree = block.getExpr_stmts().get(0).getExpressionPaths().get(0);
        SrcMLCall command = parentExpression.getCalls().get(0);
        SrcMLCall getExtraEnv = command.getArgumentList().getArguments().get(0).getExpression().getCalls().get(0);
        SrcMLCall get = command.getArgumentList().getArguments().get(0).getExpression().getCalls().get(1);
        SrcMLCall commandLine = get.getArgumentList().getArguments().get(0).getExpression().getCalls().get(0);

        callTree.printSrcMLTree();
        assertEquals("", outContent.toString());

//        assertEquals(callTree.getChildren().size(), 1);
//        CallTreeNode<SrcMLNode> firstChild = callTree.getChildren().get(0);
//        assertEquals(((SrcMLCall)firstChild.getName()).getName(), "command.updateDynamicLibraryPath");
//        assertEquals(firstChild.getChildren().size(), 2);
//        CallTreeNode<SrcMLNode> grandChild1 = firstChild.getChildren().get(0);
//        CallTreeNode<SrcMLNode> grandChild2 = firstChild.getChildren().get(0);
//        assertEquals(((SrcMLCall)grandChild1.getName()).getName(), "getExtraEnv");
//        assertEquals(grandChild1.getChildren().size(), 0);
//        assertEquals(((SrcMLCall)grandChild2.getName()).getName(), "get");
//        assertEquals(grandChild2.getChildren().size(), 1);
//        CallTreeNode<SrcMLNode> greatGrandChild1 = grandChild2.getChildren().get(0);
//        assertEquals(((SrcMLCall)greatGrandChild1.getName()).getName(), "Command.getLibraryPathPropertyName");
//        assertEquals(greatGrandChild1.getChildren().size(), 0);

    }

    @Ignore
    @Test
    public void verifyDecl(){
        assertEquals(block2.getDeclStmts().size(), 1);
        assertEquals(block2.getDeclStmts().get(0).getDecls().size(), 1);
        assertEquals(block2.getDeclStmts().get(0).getDecls().get(0).getInit().getExpressions().size(), 1);
        SrcMLExpression parentExpression = block2.getDeclStmts().get(0).getDecls().get(0).getInit().getExpressions().get(0);
        CallTreeNode<SrcMLNode> callTree = block2.getDeclStmts().get(0).getDecls().get(0).getInit().getExpressionPaths().get(0);
        //assertEquals(callTree.toString(), "");
        SrcMLCall firefoxBinary = parentExpression.getCalls().get(0);
        SrcMLCall getName = parentExpression.getCalls().get(1);
        SrcMLCall replace = parentExpression.getCalls().get(2);
//        SrcMLCall systemBinary = replace.getArgumentList().getArguments().get(1).getExpression().getCalls().get(0);
//        assertEquals(callTree.nodes().size(), 4);
//        assertEquals(callTree.hasEdgeConnecting(firefoxBinary, getName), true);
//        assertEquals(callTree.hasEdgeConnecting(replace, systemBinary), true);
    }


    @After
    public void after(){
        System.setOut(System.out);
    }
}
