package com.derek.uml.srcML;

import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Element;

public class SrcMLMessageUMLGenerationTest extends SrcMLTest {

    private SrcMLBlock block;

    public SrcMLMessageUMLGenerationTest(){
        xmlFileLocation = "src/test/resources/SrcMLCallExample.xml";
    }

    @Before
    public void build(){
        super.build();
        Element blockEle = (Element)doc.getElementsByTagName("block").item(0);
        Element functionEle2 = (Element)doc.getElementsByTagName("function").item(1);
        Element functionEle3 = (Element)doc.getElementsByTagName("function").item(2);
        block = new SrcMLBlock(blockEle);
    }

    @Test
    public void verifyCall1(){

    }

}
