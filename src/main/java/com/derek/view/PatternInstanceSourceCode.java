package com.derek.view;

import com.derek.model.patterns.PatternInstance;

import javax.swing.*;
import java.awt.*;

public class PatternInstanceSourceCode {

    private JFrame frame;
    private PatternInstance patternInstance;

    public PatternInstanceSourceCode(PatternInstance pi){
        this.patternInstance = pi;
        frame = new JFrame("UML Diagrams for pattern instance " + pi);
        frame.setSize(800, 800);
        frame.setLayout(new BorderLayout());

        frame.add(sourceCodePane());

        frame.setVisible(true);
    }

    public JPanel sourceCodePane(){
        //TODO
        return null;
    }
}
