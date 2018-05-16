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
        Element blockEle3 = (Element)doc.getElementsByTagName("block").item(2);

        block = new SrcMLBlock(blockEle);
        block2 = new SrcMLBlock(blockEle2);
        block3 = new SrcMLBlock(blockEle3);
        System.setOut(new PrintStream(outContent));
    }

    @Test
    public void verifyExpr(){
        assertEquals(block.getExpr_stmts().size(), 1);
        assertEquals(block.getExpr_stmts().get(0).getExpressions().size(), 1);
        CallTreeNode<SrcMLNode> mockCallTree= new CallTreeNode<>(block.getExpr_stmts().get(0), "mockRoot");
        block.fillCallTree(mockCallTree);

        mockCallTree.printTree();
        //visualizes tree
        //assertEquals("", outContent.toString());

        //this code checks the same thing as the graph above, but it just much more verbose.
        assertEquals(mockCallTree.getChildren().size(), 1);
        CallTreeNode<SrcMLNode> firstChild = mockCallTree.getChildren().get(0);
        assertEquals(((SrcMLCall)firstChild.getName()).getName(), "command.updateDynamicLibraryPath");
        assertEquals(firstChild.getChildren().size(), 2);
        CallTreeNode<SrcMLNode> grandChild1 = firstChild.getChildren().get(0);
        CallTreeNode<SrcMLNode> grandChild2 = firstChild.getChildren().get(1);
        assertEquals(((SrcMLCall)grandChild1.getName()).getName(), "getExtraEnv");
        assertEquals(grandChild1.getChildren().size(), 0);
        assertEquals(((SrcMLCall)grandChild2.getName()).getName(), "get");
        assertEquals(grandChild2.getChildren().size(), 1);
        CallTreeNode<SrcMLNode> greatGrandChild1 = grandChild2.getChildren().get(0);
        assertEquals(((SrcMLCall)greatGrandChild1.getName()).getName(), "CommandLine.getLibraryPathPropertyName");
        assertEquals(greatGrandChild1.getChildren().size(), 0);

    }

    @Test
    public void verifyDecl(){
        assertEquals(block2.getDeclStmts().size(), 1);
        assertEquals(block2.getDeclStmts().get(0).getDecls().size(), 1);
        assertEquals(block2.getDeclStmts().get(0).getDecls().get(0).getInit().getExpressions().size(), 1);
        //modified this and removed init so many tests here will likely fail.
        CallTreeNode<SrcMLNode> callTree = block2.getDeclStmts().get(0).getDecls().get(0).getCallTree();
        callTree.printTree();
        //visualize tree
        //assertEquals("", outContent.toString());

        assertEquals(callTree.getChildren().size(), 3);
        CallTreeNode<SrcMLNode> firstChild = callTree.getChildren().get(0);
        assertEquals(((SrcMLCall)firstChild.getName()).getName(), "FirefoxBinary.class.getPackage");
        assertEquals(firstChild.getChildren().size(), 0);
        CallTreeNode<SrcMLNode> secondChild = callTree.getChildren().get(1);
        assertEquals(((SrcMLCall)secondChild.getName()).getName(), "getName");
        assertEquals(secondChild.getChildren().size(), 0);
        CallTreeNode<SrcMLNode> thirdChild = callTree.getChildren().get(2);
        assertEquals(((SrcMLCall)thirdChild.getName()).getName(), "replace");
        assertEquals(thirdChild.getChildren().size(), 1);
        CallTreeNode<SrcMLNode> grandChild1 = thirdChild.getChildren().get(0);
        assertEquals(((SrcMLCall)grandChild1.getName()).getName(), "systemBinary.getChannel");
        assertEquals(grandChild1.getChildren().size(), 0);
    }

    @Ignore
    @Test
    public void verifyFor(){
        CallTreeNode<SrcMLNode> callTree = block3.getFors().get(0).getCallTree();
        callTree.printTree();
        assertEquals("", outContent.toString());
    }


    @After
    public void after(){
        System.setOut(System.out);
    }
}
