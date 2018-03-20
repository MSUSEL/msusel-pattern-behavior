package com.derek;

import com.derek.uml.PackageTree;
import com.derek.uml.UMLClass;
import com.derek.uml.UMLClassifier;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class PackageTreeLogicTest {

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();

    private UMLClassifier _1;
    private UMLClassifier _2;
    private UMLClassifier _3;
    private UMLClassifier _4;

    public PackageTreeLogicTest(){

    }

    @Before
    public void initialize(){
        List<String> _1Package = new ArrayList<>();
        _1Package.add("org");
        _1Package.add("openqa");
        _1Package.add("selenium");
        _1Package.add("firefox");
        List<String> _2Package = new ArrayList<>();
        _2Package.add("org");
        _2Package.add("openqa");
        _2Package.add("selenium");
        _2Package.add("firefox");
        List<String> _3Package = new ArrayList<>();
        _3Package.add("org");
        _3Package.add("openqa");
        _3Package.add("selenium");
        _3Package.add("tester");
        List<String> _4Package = new ArrayList<>();
        _4Package.add("org");
        _4Package.add("openqa");
        _4Package.add("selenium");

        _1 = new UMLClass("_1", _1Package, null, null, null, null, false, null, null, "test1");
        _2 = new UMLClass("_2", _2Package, null, null, null, null, false, null, null, "test2");
        _3 = new UMLClass("_3", _3Package, null, null, null, null, false, null, null, "test3");
        _4 = new UMLClass("_4", _4Package, null, null, null, null, false, null, null, "test4");

        System.setOut(new PrintStream(outContent));
    }

    @Test
    public void verifyPackageLogic(){
        PackageTree packageTree = new PackageTree();
        packageTree.addEntirePackage(_1);
        packageTree.addEntirePackage(_2);
        packageTree.addEntirePackage(_3);
        packageTree.addEntirePackage(_4);


        packageTree.printInOrder(packageTree.getRoot());
        //assertEquals("", outContent.toString());

        assertEquals("org", packageTree.getRoot().getName());
        assertEquals(1, packageTree.getRoot().getChildren().size());
        PackageTree.PackageNode firstChild = packageTree.getRoot().getChildren().get(0);
        assertEquals("openqa", firstChild.getName());
        assertEquals(1, firstChild.getChildren().size());
        PackageTree.PackageNode secondChild = firstChild.getChildren().get(0);
        assertEquals("selenium", secondChild.getName());
        assertEquals(2, secondChild.getChildren().size());
        PackageTree.PackageNode thirdChild = secondChild.getChildren().get(0);
        assertEquals("firefox", thirdChild.getName());
        assertEquals(0, thirdChild.getChildren().size());
        assertEquals(2, thirdChild.getClassifiers().size());
        PackageTree.PackageNode fourthChild = secondChild.getChildren().get(1);
        assertEquals("tester", fourthChild.getName());
        assertEquals(0, fourthChild.getChildren().size());
        assertEquals(1, fourthChild.getClassifiers().size());


    }

    @After
    public void after(){
        System.setOut(System.out);
    }

}
