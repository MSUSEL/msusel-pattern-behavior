package com.derek.uml.srcML;

import com.derek.uml.UMLClass;
import com.derek.uml.UMLGenerationUtils;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Element;

import static org.junit.Assert.assertEquals;

public class SrcMLEnumUMLGenerationTest extends SrcMLTest {

    private SrcMLEnum srcMLEnum;

    public SrcMLEnumUMLGenerationTest(){
        xmlFileLocation = "src/test/resources/SrcMLEnumExample.xml";
    }

    @Before
    public void build(){
        super.build();
        Element enumEle = (Element)doc.getElementsByTagName("enum").item(0);
        srcMLEnum = new SrcMLEnum(enumEle);
    }
    @Test
    public void verifyEnum(){
        UMLClass umlEnum = UMLGenerationUtils.getUMLEnum(srcMLEnum);
        assertEquals(umlEnum.isAbstract(), false);
        assertEquals(umlEnum.getAttributes().size(), 7);
        assertEquals(umlEnum.getConstructors().size(), 1);
        assertEquals(umlEnum.getOperations().size(), 5);
    }
}
