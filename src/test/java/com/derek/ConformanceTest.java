/**
 * MIT License
 *
 * Copyright (c) 2017 Montana State University, Gianforte School of Computing
 * Software Engineering Laboratory
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.derek;

import com.derek.model.PatternType;
import com.derek.model.SoftwareVersion;
import com.derek.model.patterns.PatternInstance;
import com.derek.rbml.RBMLMapping;
import com.derek.rbml.SPS;
import com.derek.uml.*;
import jdk.nashorn.internal.ir.annotations.Ignore;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ConformanceTest {

    //jspec

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private Conformance strictConformance;
    private Conformance relaxedConformance;

    private SPS stateSPSRelaxed;
    private SPS stateSPSStrict;

    private StatePattern oneStateOneRequest;
    private StatePattern twoStateOneRequest;
    private StatePattern oneStateTwoRequest;
    private StatePattern twoStateTwoRequest;

    private StatePattern missingContext;
    private StatePattern missingState;
    private StatePattern missingStateVar;
    private StatePattern missingRequest;

    @BeforeAll
    public void buildTest(){
        System.setOut(new PrintStream(outContent));
        oneStateOneRequest = buildOneStateOneRequest();
        twoStateOneRequest = buildTwoStateOneRequest();
        oneStateTwoRequest = buildOneStateTwoRequest();
        twoStateTwoRequest = buildTwoStateTwoRequest();
        stateSPSRelaxed = new SPS("src/test/resources/sps/StateSPSRelaxed.txt");
    }

    public StatePattern buildOneStateOneRequest(){
        List<Pair<String, String>> patternRoles = new ArrayList<>();
        patternRoles.add(new ImmutablePair<>("Context", "CH.DrawApplet"));
        patternRoles.add(new ImmutablePair<>("State", "CH.Tool"));
        patternRoles.add(new ImmutablePair<>("state", "CH.DrawApplet::fTool:CH.Tool"));
        patternRoles.add(new ImmutablePair<>("Request()", "CH.DrawApplet::setTool(CH.Tool, java.lang.String):void"));
        PatternInstance pi = new PatternInstance(patternRoles, PatternType.STATE, new SoftwareVersion(1));

        UMLClassDiagram umlClassDiagram = new UMLClassDiagram();
        List<UMLAttribute> drawAppletAttributes = new ArrayList<>();
        drawAppletAttributes.add(new UMLAttribute("fTool", "Tool"));
        List<UMLOperation> drawAppletOps = new ArrayList<>();
        List<Pair<String, String>> params = new ArrayList<>();
        params.add(new ImmutablePair<>("Tool", "a"));
        params.add(new ImmutablePair<>("String", "b"));
        drawAppletOps.add(new UMLOperation("setTool", params, "void"));
        drawAppletOps.get(0).setParameters(new ArrayList<>());
        List<String> residingPackage = new ArrayList<>();
        residingPackage.add("CH");
        UMLClass drawApplet = new UMLClass("DrawApplet", new ArrayList<>(), new ArrayList<>(), drawAppletAttributes, drawAppletOps,
                new ArrayList<>(), false, new ArrayList<>(), new ArrayList<>(), "class");
        UMLClass tool = new UMLClass("Tool", new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>(),
                new ArrayList<>(), false, new ArrayList<>(), new ArrayList<>(), "class");
        umlClassDiagram.addClassToDiagram(drawApplet);
        umlClassDiagram.addClassToDiagram(tool);
        umlClassDiagram.addRelationshipToDiagram(drawApplet, tool, RelationshipType.ASSOCIATION);
        PackageTree packageTree = new PackageTree();
        PackageTree.PackageNode root = packageTree.new PackageNode("CH", drawApplet);
        root.addClassifierAtThisLevel(tool);
        packageTree.setRoot(root);
        umlClassDiagram.setPackageTree(packageTree);
        return new StatePattern(pi, umlClassDiagram);
    }

    public StatePattern buildTwoStateOneRequest(){
        List<Pair<String, String>> patternRoles = new ArrayList<>();
        patternRoles.add(new ImmutablePair<>("Context", "CH.LineConnection"));
        patternRoles.add(new ImmutablePair<>("State", "CH.Connector"));
        patternRoles.add(new ImmutablePair<>("state", "CH.LineConnection::fStart:CH.Connector"));
        patternRoles.add(new ImmutablePair<>("state", "CH.LineConnection::fEnd:CH.Connector"));
        patternRoles.add(new ImmutablePair<>("Request()", "CH.LineConnection::updateConnection():void"));
        PatternInstance pi = new PatternInstance(patternRoles, PatternType.STATE, new SoftwareVersion(1));

        UMLClassDiagram umlClassDiagram = new UMLClassDiagram();
        List<UMLAttribute> contextAttributes = new ArrayList<>();
        contextAttributes.add(new UMLAttribute("fStart", "Connector"));
        contextAttributes.add(new UMLAttribute("fEnd", "Connector"));
        List<UMLOperation> contextOps = new ArrayList<>();
        List<Pair<String, String>> params = new ArrayList<>();
        contextOps.add(new UMLOperation("updateConnection", params, "void"));
        List<String> residingPackage = new ArrayList<>();
        residingPackage.add("CH");
        UMLClass lineConnection = new UMLClass("LineConnection", residingPackage, new ArrayList<>(), contextAttributes, contextOps,
                new ArrayList<>(), false, new ArrayList<>(), new ArrayList<>(), "class");
        UMLClass connector = new UMLClass("Connector", residingPackage, new ArrayList<>(), new ArrayList<>(), new ArrayList<>(),
                new ArrayList<>(), false, new ArrayList<>(), new ArrayList<>(), "class");
        umlClassDiagram.addClassToDiagram(lineConnection);
        umlClassDiagram.addClassToDiagram(connector);
        umlClassDiagram.addRelationshipToDiagram(lineConnection, connector, RelationshipType.ASSOCIATION);
        umlClassDiagram.buildPackageTree();
        return new StatePattern(pi, umlClassDiagram);
    }


    public StatePattern buildOneStateTwoRequest(){
        List<Pair<String, String>> patternRoles = new ArrayList<>();
        patternRoles.add(new ImmutablePair<>("Context", "CH.ConnectionHandle"));
        patternRoles.add(new ImmutablePair<>("State", "CH.Figure"));
        patternRoles.add(new ImmutablePair<>("state", "CH.ConnectionHandle::fTarget:CH.Figure"));
        patternRoles.add(new ImmutablePair<>("Request()", "CH.ConnectionHandle::invokeEnd():void"));
        patternRoles.add(new ImmutablePair<>("Request()", "CH.ConnectionHandle::invokeStep():void"));
        PatternInstance pi = new PatternInstance(patternRoles, PatternType.STATE, new SoftwareVersion(1));

        UMLClassDiagram umlClassDiagram = new UMLClassDiagram();
        List<UMLAttribute> contextAttributes = new ArrayList<>();
        contextAttributes.add(new UMLAttribute("fTarget", "Figure"));
        List<UMLOperation> contextOps = new ArrayList<>();
        List<Pair<String, String>> params = new ArrayList<>();
        contextOps.add(new UMLOperation("invokeEnd", params, "void"));
        contextOps.add(new UMLOperation("invokeStep", params, "void"));
        List<String> residingPackage = new ArrayList<>();
        residingPackage.add("CH");
        UMLClass connectionHandle = new UMLClass("ConnectionHandle", residingPackage, new ArrayList<>(), contextAttributes, contextOps,
                new ArrayList<>(), false, new ArrayList<>(), new ArrayList<>(), "class");
        UMLClass figure = new UMLClass("Figure", residingPackage, new ArrayList<>(), new ArrayList<>(), new ArrayList<>(),
                new ArrayList<>(), false, new ArrayList<>(), new ArrayList<>(), "class");
        umlClassDiagram.addClassToDiagram(connectionHandle);
        umlClassDiagram.addClassToDiagram(figure);
        umlClassDiagram.addRelationshipToDiagram(connectionHandle, figure, RelationshipType.ASSOCIATION);
        umlClassDiagram.buildPackageTree();
        return new StatePattern(pi, umlClassDiagram);
    }

    public StatePattern buildTwoStateTwoRequest(){
        List<Pair<String, String>> patternRoles = new ArrayList<>();
        patternRoles.add(new ImmutablePair<>("Context", "CH.CreationTool"));
        patternRoles.add(new ImmutablePair<>("State", "CH.Figure"));
        patternRoles.add(new ImmutablePair<>("state", "CH.CreationTool::fPrototype:CH.Figure"));
        patternRoles.add(new ImmutablePair<>("state", "CH.CreationTool::fCreatedFigure:CH.Figure"));
        patternRoles.add(new ImmutablePair<>("Request()", "CH.CreationTool::createFigure():CH.Figure"));
        patternRoles.add(new ImmutablePair<>("Request()", "CH.CreationTool::mouseDown():void"));
        PatternInstance pi = new PatternInstance(patternRoles, PatternType.STATE, new SoftwareVersion(1));

        UMLClassDiagram umlClassDiagram = new UMLClassDiagram();
        List<UMLAttribute> contextAttributes = new ArrayList<>();
        contextAttributes.add(new UMLAttribute("fPrototype", "Figure"));
        contextAttributes.add(new UMLAttribute("fCreatedFigure", "Figure"));
        List<UMLOperation> contextOps = new ArrayList<>();
        List<Pair<String, String>> params = new ArrayList<>();
        contextOps.add(new UMLOperation("createFigure", params, "Figure"));
        contextOps.add(new UMLOperation("mouseDown", params, "void"));
        List<String> residingPackage = new ArrayList<>();
        residingPackage.add("CH");
        UMLClass creationTool = new UMLClass("CreationTool", residingPackage, new ArrayList<>(), contextAttributes, contextOps,
                new ArrayList<>(), false, new ArrayList<>(), new ArrayList<>(), "class");
        UMLClass figure = new UMLClass("Figure", residingPackage, new ArrayList<>(), new ArrayList<>(), new ArrayList<>(),
                new ArrayList<>(), false, new ArrayList<>(), new ArrayList<>(), "class");
        umlClassDiagram.addClassToDiagram(creationTool);
        umlClassDiagram.addClassToDiagram(figure);
        umlClassDiagram.addRelationshipToDiagram(creationTool, figure, RelationshipType.ASSOCIATION);
        umlClassDiagram.buildPackageTree();
        return new StatePattern(pi, umlClassDiagram);
    }

    @Ignore
    @Test
    public void testOneStateOneRequest(){
        strictConformance = new Conformance(stateSPSStrict, null, oneStateOneRequest, oneStateOneRequest.getUmlClassDiagram());
        relaxedConformance = new Conformance(stateSPSRelaxed, null, oneStateOneRequest, oneStateOneRequest.getUmlClassDiagram());
        List<RBMLMapping> rbmlStructureMappings = strictConformance.mapStructure();
        assertEquals(4, rbmlStructureMappings.size());

    }

    @AfterAll
    public void after(){
        System.setOut(System.out);
    }
}