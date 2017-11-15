package com.derek.view;

import com.derek.model.patterns.PatternInstance;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

public class PatternInstanceDiagrams {

    private JFrame frame;
    private PatternInstance patternInstance;

    public PatternInstanceDiagrams(PatternInstance pi){
        this.patternInstance = pi;
        frame = new JFrame("UML Diagrams for pattern instance " + pi);
        frame.setSize(800, 800);
        frame.setLayout(new GridLayout(1, 2, 5, 5));

        frame.add(getClassDiagram());
        frame.add(getSequenceDiagram());

        frame.setVisible(true);
    }

    private JPanel getClassDiagram(){
        try {
            //https://stackoverflow.com/questions/299495/how-to-add-an-image-to-a-jpanel
            BufferedImage classDiagramIO = ImageIO.read(new File("resources/observer_class.png"));
            JLabel picLabel = new JLabel(new ImageIcon(classDiagramIO));
            JPanel classDiagram = new JPanel();
            classDiagram.add(picLabel);
            return classDiagram;

        }catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }

    private JPanel getSequenceDiagram(){
        try {
            //https://stackoverflow.com/questions/299495/how-to-add-an-image-to-a-jpanel
            BufferedImage classDiagramIO = ImageIO.read(new File("resources/observer.png"));
            JLabel picLabel = new JLabel(new ImageIcon(classDiagramIO));
            JPanel sequenceDiagram = new JPanel();
            sequenceDiagram.add(picLabel);
            return sequenceDiagram;

        }catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }

}
