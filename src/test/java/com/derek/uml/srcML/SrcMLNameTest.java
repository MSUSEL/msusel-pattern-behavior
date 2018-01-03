package com.derek.uml.srcML;

import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Element;

import static org.junit.Assert.*;

public class SrcMLNameTest extends SrcMLTest{
    private SrcMLName srcMLName1;
    private SrcMLName srcMLName2;
    private SrcMLName srcMLName3;
    private SrcMLName srcMLName4;


    public SrcMLNameTest(){
        xmlFileLocation = "src/test/resources/SrcMLNameExample.xml";
    }

    @Before
    public void build(){
        super.build();
        Element nameEle1 = (Element) doc.getElementsByTagName("name").item(0);
        Element nameEle2 = (Element) doc.getElementsByTagName("name").item(1);
        Element nameEle3 = (Element) doc.getElementsByTagName("name").item(4);
        Element nameEle4 = (Element) doc.getElementsByTagName("name").item(9);
        srcMLName1 = new SrcMLName(nameEle1);
        srcMLName2 = new SrcMLName(nameEle2);
        srcMLName3 = new SrcMLName(nameEle3);
        srcMLName4 = new SrcMLName(nameEle4);
    }


    @Test
    public void verifyName(){
        assertEquals(srcMLName1.getName().size(), 1);
        assertEquals(srcMLName1.getName().get(0), "First");
        assertEquals(srcMLName2.getName().size(), 1);
        assertEquals(srcMLName2.getName().get(0), "Optional");
        assertEquals(srcMLName3.getName().size(), 1);
        assertEquals(srcMLName3.getName().get(0), "ExpectedCondition");
        assertEquals(srcMLName4.getName().size(), 2);
        assertEquals(srcMLName4.getName().get(0), "element");
        assertEquals(srcMLName4.getName().get(1), "isEnabled");
    }

    @Test
    public void verifyArgumentList(){
        assertEquals(srcMLName1.getArgumentList().size(), 0);
        assertEquals(srcMLName2.getArgumentList().size(), 1);
        assertEquals(srcMLName3.getArgumentList().size(), 1);
        assertEquals(srcMLName4.getArgumentList().size(), 0);
    }

    @Test
    public void verifyParameterList(){
        assertEquals(srcMLName1.getParameterList().size(), 0);
        assertEquals(srcMLName2.getParameterList().size(), 0);
        assertEquals(srcMLName3.getParameterList().size(), 0);
        assertEquals(srcMLName4.getParameterList().size(), 0);
    }

    @Test
    public void verifyOperators(){
        assertEquals(srcMLName1.getOperators().size(), 0);
        assertEquals(srcMLName2.getOperators().size(), 0);
        assertEquals(srcMLName3.getOperators().size(), 0);
        assertEquals(srcMLName4.getOperators().size(), 1);
        assertEquals(srcMLName4.getOperators().get(0), ".");
    }

}