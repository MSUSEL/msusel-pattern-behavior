package com.derek.uml.srcML;


import com.derek.uml.UMLClass;
import com.derek.uml.UMLGenerationUtils;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Element;

import static org.junit.Assert.*;
public class SrcMLClassUMLGenerationTest extends SrcMLTest{

    private SrcMLClass srcMLClass;
    private SrcMLClass srcMLClass2;
    private SrcMLClass srcMLClass3;

    public SrcMLClassUMLGenerationTest(){
        xmlFileLocation = "src/test/resources/SrcMLClassExample.xml";
    }

    @Before
    public void build(){
        super.build();
        Element classEle = (Element)doc.getElementsByTagName("class").item(0);
        Element classEle2 = (Element)doc.getElementsByTagName("class").item(1);
        Element classEle3 = (Element)doc.getElementsByTagName("class").item(3);
        srcMLClass = new SrcMLClass(classEle);
        srcMLClass2 = new SrcMLClass(classEle2);
        srcMLClass3 = new SrcMLClass(classEle3);
    }
    @Test
    public void verifyClass(){
        UMLClass umlClass = UMLGenerationUtils.getUMLClass(srcMLClass);
        assertEquals(umlClass.isAbstract(), true);
        assertEquals(umlClass.getAttributes().size(), 0);
        assertEquals(umlClass.getOperations().size(), 2);
        assertEquals(umlClass.getOperations().get(0).getName(), "buildBy");
        assertEquals(umlClass.getOperations().get(0).getParameters().size(), 0);
        assertEquals(umlClass.getOperations().get(0).getReturnDataType(), "By");
    }
    @Test
    public void verifyClass2(){
        UMLClass umlClass = UMLGenerationUtils.getUMLClass(srcMLClass2);
        assertEquals(umlClass.isAbstract(), false);
        assertEquals(umlClass.getAttributes().size(), 21);
        assertEquals(umlClass.getConstructors().size(), 8);
        assertEquals(umlClass.getOperations().size(), 5);
    }
    @Test
    public void verifyClass3(){
        UMLClass umlClass = UMLGenerationUtils.getUMLClass(srcMLClass3);
        assertEquals(umlClass.getName(), "Anonymous Class");
        assertEquals(umlClass.getAttributes().size(), 1);
        assertEquals(umlClass.getOperations().size(), 2);

    }
}
