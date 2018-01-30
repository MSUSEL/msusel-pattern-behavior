package com.derek.uml.srcML;

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

        block = new SrcMLBlock(blockEle);
    }

    @Test
    public void verifyExpr(){
        assertEquals(block.getExpr_stmts().size(), 1);
    }
}
