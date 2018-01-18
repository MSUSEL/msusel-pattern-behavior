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
