package com.derek.uml.srcML;

import com.derek.uml.UMLAttribute;
import com.derek.uml.UMLGenerationUtils;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Element;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class SrcMLDeclStmtUMLGenerationTest extends SrcMLTest{

    private SrcMLDeclStmt srcMLDeclStmt;
    private SrcMLDeclStmt srcMLDeclStmt2;
    private SrcMLDeclStmt srcMLDeclStmt3;

    public SrcMLDeclStmtUMLGenerationTest(){
        xmlFileLocation = "src/test/resources/SrcMLDeclStmtExample.xml";
    }

    @Before
    public void build(){
        super.build();
        Element declStmtEle = (Element)doc.getElementsByTagName("decl_stmt").item(0);
        //Element declStmtEle2 = (Element)doc.getElementsByTagName("decl_stmt").item(1);
        //Element declStmtEle3 = (Element)doc.getElementsByTagName("decl_stmt").item(2);
        srcMLDeclStmt = new SrcMLDeclStmt(declStmtEle);
        //srcMLDeclStmt2 = new SrcMLDeclStmt(declStmtEle2);
        //srcMLDeclStmt3 = new SrcMLDeclStmt(declStmtEle3);
    }

    @Test
    public void verfityDeclStmt1(){
        List<SrcMLDeclStmt> declStmts = new ArrayList<>();
        declStmts.add(srcMLDeclStmt);
        List<UMLAttribute> atts = UMLGenerationUtils.getUMLAttributes(declStmts);
        assertEquals(atts.size(), 1);
        assertEquals(atts.get(0).getDataType(), "String");
        assertEquals(atts.get(0).getName(), "HOST");
        //test atts
    }

}