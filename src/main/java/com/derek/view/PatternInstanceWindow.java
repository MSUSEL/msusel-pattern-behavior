package com.derek.view;

import com.derek.model.Model;
import oracle.jrockit.jfr.JFR;

import javax.swing.*;
import java.util.regex.Pattern;

public class PatternInstanceWindow {
    private JFrame frame;
    private Model model;

    public PatternInstanceWindow(String instanceName, Model model){
        this.model = model;

        frame = new JFrame("Evolution of Pattern Instance " + instanceName);
        frame.setSize(1200, 800);
        frame.setVisible(true);


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
