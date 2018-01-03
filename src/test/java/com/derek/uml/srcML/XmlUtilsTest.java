package com.derek.uml.srcML;

import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import java.util.List;

import static org.junit.Assert.*;

public class XmlUtilsTest extends SrcMLTest{


    public XmlUtilsTest(){
        xmlFileLocation = "src/test/resources/XmlUtilExampleFunction.xml";
    }


    @Test
    public void getImmediateChildren() {
        //should return only 'getDataModel'

        NodeList nodeLists = doc.getElementsByTagName("function");
        Node firstFunction = nodeLists.item(0);

        List<Node> n = XmlUtils.getImmediateChildren(firstFunction, "name");
        assertEquals(n.size(), 1);

        Node firstName = n.get(0);
        assertEquals(firstName.getTextContent(), "getDataModel");

    }
}