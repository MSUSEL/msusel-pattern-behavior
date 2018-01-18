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
package com.derek.view;

import com.derek.model.Model;
import com.derek.model.patterns.PatternInstance;
import com.google.common.graph.MutableGraph;
import javafx.util.Pair;
import oracle.jrockit.jfr.JFR;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class PatternInstanceWindow {
    private JFrame frame;
    private Model model;
    private List<ExpandablePatternInstance> expandablePatternInstanceList;
    private MutableGraph<PatternInstance> patternEvolution;

    public PatternInstanceWindow(PatternInstance instance, Model model){
        this.model = model;
        this.patternEvolution = model.buildPatternEvolution(instance);
        frame = new JFrame("Evolution of Pattern Instance " + instance);

        frame.setLayout(new BorderLayout());

        frame.add(buildPatternInstanceList(), BorderLayout.CENTER);
        frame.add(buildPatternDemographics(instance), BorderLayout.EAST);

        frame.setSize(1200, 800);
        frame.setVisible(true);
    }



    private JList<String> buildPatternDemographics(PatternInstance pi){
        DefaultListModel stringOptions = new DefaultListModel();
        stringOptions.addElement("Pattern Type: " + pi.getPatternType());
        stringOptions.addElement("Versions that pattern appears in: " + getListOfVersions());
        stringOptions.addElement("\tFirst Version appearance: " + getFirstAppearance());
        stringOptions.addElement("\tLast Version apperance: " + getLastAppearance());
        stringOptions.addElement("\tIs Pattern Instance continuous: " + isPatternContinuous());
        stringOptions.addElement("Does pattern change: TODO"  );// + doesPatternChange());

        return new JList<String>(stringOptions);
    }

    private String doesPatternChange(){
        String toRet = "yes";
        //TODO - it woudl be super helpful to order the nodes in my mutablegraph obj

        for (PatternInstance pi : patternEvolution.nodes()){


        }

        return toRet;
    }

    private String isPatternContinuous(){
        String toRet = "yes";
        ArrayList<Integer> versions = new ArrayList<>();
        for (PatternInstance pi : patternEvolution.nodes()){
            versions.add(pi.getSoftwareVersion().getVersionNum());
        }
        int prev = versions.get(0);
        for (int i = 0; i < versions.size()-1; i++){
            if (versions.get(i+1) != null){
                if (versions.get(i) != versions.get(i+1)-1){
                    //not continuous
                    toRet = "no";
                }
            }
        }
        return toRet;
    }

    //jenky method, but it works. Jenky because sets are unordered by default, but I am actually ordering it because
    //of the SoftwareVersion immplementing comparator.
    private String getFirstAppearance(){
        for (PatternInstance pi : patternEvolution.nodes()){
            return pi.getSoftwareVersion().getVersionNum() + "";
        }
        return "";
    }

    //jenky again. see comments for getFirstAppearance()
    private String getLastAppearance(){
        String version = "";
        for (PatternInstance pi : patternEvolution.nodes()){
            version = pi.getSoftwareVersion().getVersionNum() + "";
        }
        return version;
    }

    private String getListOfVersions(){
        String toRet = "";
        for (PatternInstance pi : patternEvolution.nodes()){
            toRet += pi.getSoftwareVersion().getVersionNum() + ", ";
        }

        return toRet;
    }

    private JScrollPane buildPatternInstanceList(){
        JPanel patternInstanceList = new JPanel();
        patternInstanceList.setLayout(new BoxLayout(patternInstanceList, BoxLayout.Y_AXIS));

        for (PatternInstance pi : patternEvolution.nodes()){
            patternInstanceList.add(buildPatternBox(pi));
        }

        return new JScrollPane(patternInstanceList);

    }

    private JPanel buildPatternBox(PatternInstance pi){
        JPanel patternBox = new JPanel(new BorderLayout());
        patternBox.setBorder(BorderFactory.createLineBorder(Color.black));

        JTextField version = new JTextField(pi.getSoftwareVersion().toString());
        version.setFont(new Font("Arial", Font.BOLD, 16));
        version.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
        version.setEditable(false);

        JTextField firstMajorRole = new JTextField(pi.getValueOfMajorRole(pi));
        JTextField secondMajorRole = new JTextField(pi.getValueOfSecondMajorRole(pi));
        Color greenGray = new Color(60, 160, 120);
        firstMajorRole.setBackground(greenGray);
        firstMajorRole.setEditable(false);
        secondMajorRole.setBackground(greenGray);
        secondMajorRole.setEditable(false);

        patternBox.add(version, BorderLayout.NORTH);
        patternBox.add(firstMajorRole, BorderLayout.WEST);
        patternBox.add(secondMajorRole, BorderLayout.EAST);

        patternBox.add(getListOfMinorRoles(pi), BorderLayout.SOUTH);


        patternBox.setComponentPopupMenu(getDiagramPopupFunctionality(pi));

        return patternBox;
    }

    private JPopupMenu getDiagramPopupFunctionality(PatternInstance pi){
        JPopupMenu diagramPopup = new JPopupMenu();
        JMenuItem diagramSelection = new JMenuItem("Show UML Diagrams (Class and Sequence)");
        JMenuItem sourceCodeSelection = new JMenuItem("Show Source code");

        diagramSelection.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                PatternInstanceDiagrams pids = new PatternInstanceDiagrams(pi);
            }
        });
        sourceCodeSelection.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                PatternInstanceSourceCode pisc = new PatternInstanceSourceCode(pi, patternEvolution);
            }
        });
        diagramPopup.add(diagramSelection);
        diagramPopup.add(sourceCodeSelection);
        return diagramPopup;
    }

    private JList<String> getListOfMinorRoles(PatternInstance pi){
        DefaultListModel stringOptions = new DefaultListModel();
        for (Pair<String, String> allMinorRoles : pi.getListOfPatternRoles()){
            if (!allMinorRoles.getValue().equals(pi.getValueOfMajorRole(pi)) && !allMinorRoles.getValue().equals(pi.getValueOfSecondMajorRole(pi))) {
                stringOptions.addElement(allMinorRoles.getValue());
            }
        }
        return new JList<String>(stringOptions);
    }

}
