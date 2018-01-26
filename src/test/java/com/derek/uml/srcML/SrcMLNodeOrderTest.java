package com.derek.uml.srcML;

import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Element;

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
        List<String> nodeOrder = new ArrayList<>();
        List<String> expectedNodeOrder = node1.getNodeOrder();
        nodeOrder.add("type");
        nodeOrder.add("name");
        nodeOrder.add("init");
        //assertEquals(node1.nodeOrder.size(), nodeOrder.size());
        assertEquals(node1.nodeOrder.get(0), nodeOrder.get(0));
        assertEquals(node1.nodeOrder.get(1), nodeOrder.get(1));
        assertEquals(node1.nodeOrder.get(2), nodeOrder.get(2));
    }

}
