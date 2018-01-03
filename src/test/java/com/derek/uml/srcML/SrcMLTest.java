package com.derek.uml.srcML;

import org.junit.Before;
import org.w3c.dom.Document;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public abstract class SrcMLTest {

    protected String xmlFileLocation;
    protected Document doc;


    @Before
    public void build(){
        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            doc = dBuilder.parse(xmlFileLocation);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
