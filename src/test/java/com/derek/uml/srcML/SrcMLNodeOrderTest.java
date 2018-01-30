package com.derek.uml.srcML;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class SrcMLNodeOrderTest extends SrcMLTest {
    private SrcMLNode node1;

    public SrcMLNodeOrderTest(){
        xmlFileLocation = "src/test/resources/SrcMLNodeExample.xml";
    }

    @Before
    public void build(){
        super.build();
        Element nameEle1 = (Element) doc.getElementsByTagName("decl").item(0);
        node1 = new SrcMLDecl(nameEle1);
    }
    @Test
    public void verifyNodeOrder(){
        assertEquals(node1.getChildNodeOrder().size(), 3);
        assertEquals(node1.getChildNodeOrder().get(0).getElement().getTagName(), "type");
        assertEquals(node1.getChildNodeOrder().get(1).getElement().getTagName(), "name");
        assertEquals(node1.getChildNodeOrder().get(2).getElement().getTagName(), "init");
    }


}
