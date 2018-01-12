package com.derek.uml.srcML;

import org.junit.Test;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.*;

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

    @Test
    public void verifyNameList(){
        List<String> nameList = new ArrayList<>();
        nameList.add("foo");
        nameList.add("bar");
        nameList.add("bar2");
        assertEquals(XmlUtils.stringifyNames(nameList), "foo,bar,bar2");
    }
    @Test
    public void verifyNameStack(){
        List<String> nameList1 = new ArrayList<>();
        nameList1.add("1foo");
        nameList1.add("1bar");
        nameList1.add("1bar2");
        List<String> nameList2 = new ArrayList<>();
        nameList2.add("2foo");
        nameList2.add("2bar");
        List<String> nameList3 = new ArrayList<>();
        nameList3.add("3foo");
        LinkedList<List<String>> linkedList = new LinkedList<>();
        linkedList.add(nameList3);
        linkedList.add(nameList2);
        linkedList.add(nameList1);
        assertEquals(XmlUtils.stringifyNames(linkedList), "3foo<2foo,2bar<1foo,1bar,1bar2>>");
    }
}