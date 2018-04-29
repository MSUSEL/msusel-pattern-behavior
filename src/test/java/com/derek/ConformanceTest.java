package com.derek;

import com.derek.model.PatternType;
import com.derek.model.SoftwareVersion;
import com.derek.model.patterns.PatternInstance;
import com.derek.rbml.RBMLMapping;
import com.derek.rbml.SPS;
import com.derek.uml.*;
import javafx.util.Pair;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class ConformanceTest {
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

    @Before
    public void buildTest(){
        oneStateOneRequest = buildOneStateOneRequest();
        twoStateOneRequest = buildTwoStateOneRequest();
        oneStateTwoRequest = buildOneStateTwoRequest();
        twoStateTwoRequest = buildTwoStateTwoRequest();
        stateSPSRelaxed = new SPS("src/test/resources/sps/StateSPSRelaxed.txt");
        stateSPSStrict = new SPS("src/test/resources/sps/StateSPSStrict.txt");

        testOneStateOneRequest();

    }

    public StatePattern buildOneStateOneRequest(){
        List<Pair<String, String>> patternRoles = new ArrayList<>();
        patternRoles.add(new Pair<>("Context", "CH.ifa.draw.applet.DrawApplet"));
        patternRoles.add(new Pair<>("State", "CH.ifa.draw.framework.Tool"));
        patternRoles.add(new Pair<>("state", "CH.ifa.draw.applet.DrawApplet::fTool:CH.ifa.draw.framework.Tool"));
        patternRoles.add(new Pair<>("Request()", "CH.ifa.draw.applet.DrawApplet::setTool(CH.ifa.draw.framework.Tool, java.lang.String):void"));
        PatternInstance pi = new PatternInstance(patternRoles, PatternType.STATE, new SoftwareVersion(1));

        UMLClassDiagram umlClassDiagram = new UMLClassDiagram();
        List<UMLAttribute> drawAppletAttributes = new ArrayList<>();
        drawAppletAttributes.add(new UMLAttribute("fTool", "Tool"));
        List<UMLOperation> drawAppletOps = new ArrayList<>();
        List<Pair<String, String>> params = new ArrayList<>();
        params.add(new Pair<>("Tool", "a"));
        params.add(new Pair<>("String", "b"));
        drawAppletOps.add(new UMLOperation("setTool", params, "void"));
        List<String> residingPackage = new ArrayList<>();
        residingPackage.add("CH");
        UMLClass drawApplet = new UMLClass("DrawApplet", null, null, drawAppletAttributes, drawAppletOps,
                null, false, null, null, "class");
        UMLClass tool = new UMLClass("Tool", null, null, null, null,
                null, false, null, null, "class");
        umlClassDiagram.addClassToDiagram(drawApplet);
        umlClassDiagram.addClassToDiagram(tool);
        umlClassDiagram.addRelationshipToDiagram(drawApplet, tool, Relationship.ASSOCIATION);
        return new StatePattern(pi, umlClassDiagram);
    }

    public StatePattern buildTwoStateOneRequest(){
        List<Pair<String, String>> patternRoles = new ArrayList<>();
        patternRoles.add(new Pair<>("Context", "CH.ifa.draw.figures.LineConnection"));
        patternRoles.add(new Pair<>("State", "CH.ifa.draw.framework.Connector"));
        patternRoles.add(new Pair<>("state", "CH.ifa.draw.figures.LineConnection::fStart:CH.ifa.draw.framework.Connector"));
        patternRoles.add(new Pair<>("state", "CH.ifa.draw.figures.LineConnection::fEnd:CH.ifa.draw.framework.Connector"));
        patternRoles.add(new Pair<>("Request()", "CH.ifa.draw.figures.LineConnection::updateConnection():void"));
        PatternInstance pi = new PatternInstance(patternRoles, PatternType.STATE, new SoftwareVersion(1));

        UMLClassDiagram umlClassDiagram = new UMLClassDiagram();
        List<UMLAttribute> contextAttributes = new ArrayList<>();
        contextAttributes.add(new UMLAttribute("fStart", "Connector"));
        contextAttributes.add(new UMLAttribute("fEnd", "Connector"));
        List<UMLOperation> contextOps = new ArrayList<>();
        List<Pair<String, String>> params = new ArrayList<>();
        contextOps.add(new UMLOperation("updateConnection", params, "void"));
        UMLClass lineConnection = new UMLClass("LineConnection", null, null, contextAttributes, contextOps,
                null, false, null, null, "class");
        UMLClass connector = new UMLClass("Connector", null, null, null, null,
                null, false, null, null, "class");
        umlClassDiagram.addClassToDiagram(lineConnection);
        umlClassDiagram.addClassToDiagram(connector);
        umlClassDiagram.addRelationshipToDiagram(lineConnection, connector, Relationship.ASSOCIATION);
        return new StatePattern(pi, umlClassDiagram);
    }


    public StatePattern buildOneStateTwoRequest(){
        List<Pair<String, String>> patternRoles = new ArrayList<>();
        patternRoles.add(new Pair<>("Context", "CH.ifa.draw.standard.ConnectionHandle"));
        patternRoles.add(new Pair<>("State", "CH.ifa.draw.framework.Figure"));
        patternRoles.add(new Pair<>("state", "CH.ifa.draw.standard.ConnectionHandle::fTarget:CH.ifa.draw.framework.Figure"));
        patternRoles.add(new Pair<>("Request()", "CH.ifa.draw.standard.ConnectionHandle::invokeEnd():void"));
        patternRoles.add(new Pair<>("Request()", "CH.ifa.draw.standard.ConnectionHandle::invokeStep():void"));
        PatternInstance pi = new PatternInstance(patternRoles, PatternType.STATE, new SoftwareVersion(1));

        UMLClassDiagram umlClassDiagram = new UMLClassDiagram();
        List<UMLAttribute> contextAttributes = new ArrayList<>();
        contextAttributes.add(new UMLAttribute("fTarget", "Figure"));
        List<UMLOperation> contextOps = new ArrayList<>();
        List<Pair<String, String>> params = new ArrayList<>();
        contextOps.add(new UMLOperation("invokeEnd", params, "void"));
        contextOps.add(new UMLOperation("invokeStep", params, "void"));
        UMLClass connectionHandle = new UMLClass("ConnectionHandle", null, null, contextAttributes, contextOps,
                null, false, null, null, "class");
        UMLClass figure = new UMLClass("Figure", null, null, null, null,
                null, false, null, null, "class");
        umlClassDiagram.addClassToDiagram(connectionHandle);
        umlClassDiagram.addClassToDiagram(figure);
        umlClassDiagram.addRelationshipToDiagram(connectionHandle, figure, Relationship.ASSOCIATION);
        return new StatePattern(pi, umlClassDiagram);
    }

    public StatePattern buildTwoStateTwoRequest(){
        List<Pair<String, String>> patternRoles = new ArrayList<>();
        patternRoles.add(new Pair<>("Context", "CH.ifa.draw.standard.CreationTool"));
        patternRoles.add(new Pair<>("State", "CH.ifa.draw.framework.Figure"));
        patternRoles.add(new Pair<>("state", "CH.ifa.draw.standard.CreationTool::fPrototype:CH.ifa.draw.framework.Figure"));
        patternRoles.add(new Pair<>("state", "CH.ifa.draw.standard.CreationTool::fCreatedFigure:CH.ifa.draw.framework.Figure"));
        patternRoles.add(new Pair<>("Request()", "CH.ifa.draw.standard.CreationTool::createFigure():CH.ifa.draw.framework.Figure"));
        patternRoles.add(new Pair<>("Request()", "CH.ifa.draw.standard.CreationTool::mouseDown():void"));
        PatternInstance pi = new PatternInstance(patternRoles, PatternType.STATE, new SoftwareVersion(1));

        UMLClassDiagram umlClassDiagram = new UMLClassDiagram();
        List<UMLAttribute> contextAttributes = new ArrayList<>();
        contextAttributes.add(new UMLAttribute("fPrototype", "Figure"));
        contextAttributes.add(new UMLAttribute("fCreatedFigure", "Figure"));
        List<UMLOperation> contextOps = new ArrayList<>();
        List<Pair<String, String>> params = new ArrayList<>();
        contextOps.add(new UMLOperation("createFigure", params, "Figure"));
        contextOps.add(new UMLOperation("mouseDown", params, "void"));
        UMLClass creationTool = new UMLClass("CreationTool", null, null, contextAttributes, contextOps,
                null, false, null, null, "class");
        UMLClass figure = new UMLClass("Figure", null, null, null, null,
                null, false, null, null, "class");
        umlClassDiagram.addClassToDiagram(creationTool);
        umlClassDiagram.addClassToDiagram(figure);
        umlClassDiagram.addRelationshipToDiagram(creationTool, figure, Relationship.ASSOCIATION);
        return new StatePattern(pi, umlClassDiagram);
    }

    @Test
    public void testOneStateOneRequest(){
        strictConformance = new Conformance(stateSPSStrict, null, oneStateOneRequest, oneStateOneRequest.getUmlClassDiagram());
        relaxedConformance= new Conformance(stateSPSRelaxed, null, oneStateOneRequest, oneStateOneRequest.getUmlClassDiagram());
        List<RBMLMapping> rbmlStructureMappings = strictConformance.mapStructure();
        assertEquals(4, rbmlStructureMappings.size());
    }
}