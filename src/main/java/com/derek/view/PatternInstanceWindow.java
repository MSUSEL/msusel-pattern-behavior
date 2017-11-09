package com.derek.view;

import com.derek.model.Model;
import oracle.jrockit.jfr.JFR;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class PatternInstanceWindow {
    private JFrame frame;
    private Model model;
    private List<ExpandablePatternInstance> expandablePatternInstanceList;

    public PatternInstanceWindow(String instanceName, Model model){
        this.model = model;

        frame = new JFrame("Evolution of        Pattern Instance " + instanceName);
        buildExpandablePatternInstanceList();

        frame.setLayout(buildSpringLayout());
        frame.setSize(1200, 800);
        frame.setVisible(true);
    }

    public SpringLayout buildSpringLayout(){
        SpringLayout layout = new SpringLayout();
        for (int i = 0; i < expandablePatternInstanceList.size()-1; i++){
            ExpandablePatternInstance mine = expandablePatternInstanceList.get(i);
            ExpandablePatternInstance next = expandablePatternInstanceList.get(i+1);
            layout.putConstraint(SpringLayout.WEST, mine, 5, SpringLayout.WEST, frame.getContentPane());
            layout.putConstraint(SpringLayout.NORTH, mine, 5, SpringLayout.NORTH, frame.getContentPane());
            layout.putConstraint(SpringLayout.EAST, mine, 20, SpringLayout.WEST, next);
            frame.add(mine);
        }

        return layout;
    }


    public void buildExpandablePatternInstanceList(){
        expandablePatternInstanceList = new ArrayList<>();
        for (int i = 0; i < 10; i++){
            expandablePatternInstanceList.add(new ExpandablePatternInstance(i + ""));
        }
    }

//    public void printEvolution(){
//        for (Pair<SoftwareVersion, PatternInstance> patternPair : model.buildPatternEvolutions(//name of pattern, i need the instance object)){
//
//        }
//    }

//    public class PatternInstanceCard {
//
//
//    }
}
