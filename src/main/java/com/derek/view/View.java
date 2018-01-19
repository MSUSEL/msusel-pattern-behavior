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
import com.derek.model.PatternType;
import com.derek.model.SoftwareVersion;
import com.derek.model.patterns.PatternInstance;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.Map;

public class View {

    public Window w;
    private Model model;

    public View(Model model){
        this.model = model;
        defaultView();
    }

    public void defaultView(){
        w = new Window();
        w.setSize(1200, 800);
        JPanel contentPane = new JPanel(new BorderLayout());
        w.setContentPane(contentPane);
        buildLeftPanel();
        w.setVisible(true);
    }

    private DefaultListModel getPatternTypes() {
        DefaultListModel toRet = new DefaultListModel();
        for (PatternType pt : model.getPatternSummaryTable().columnKeySet()) {
            toRet.addElement(pt.toString());
        }
        return toRet;
    }

    private void buildLeftPanel(){
        JPanel leftPanelSelector = new JPanel(new BorderLayout());

        JList<String> patternEvolutionOptions = new JList<>(getPatternTypes());
        patternEvolutionOptions.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JButton patternEvolutionSearch = new JButton("Search");
        patternEvolutionSearch.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int patternIndex = patternEvolutionOptions.getSelectedIndex();
                selectPatternInstance(patternIndex);
            }
        });

        JScrollPane patternEvolutionJSP = new JScrollPane(patternEvolutionOptions);

        leftPanelSelector.add(patternEvolutionJSP, BorderLayout.CENTER);
        leftPanelSelector.add(patternEvolutionSearch, BorderLayout.SOUTH);
        leftPanelSelector.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
        w.getContentPane().add(leftPanelSelector, BorderLayout.WEST);
    }

    private void selectPatternInstance(int indexSelected){
        JPanel patternInstanceSelector = new JPanel(new BorderLayout());

        JTable table;
        Map<SoftwareVersion, List<PatternInstance>> instances = model.getPatternSummaryTable().columnMap().get(model.getPatternTypeFromIndex(indexSelected));

        table = buildJTableFromMapData(instances);

        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);


        JScrollPane jsp = new JScrollPane(table);

        JButton instanceSearch = new JButton("Search");
        instanceSearch.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int tableRow = table.getSelectedRow();
                PatternInstance selection = getPatternInstanceFromTableIndex(tableRow, instances);
                PatternInstanceWindow piw = new PatternInstanceWindow(selection, model);
            }
        });

        patternInstanceSelector.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
        patternInstanceSelector.add(jsp, BorderLayout.CENTER);
        patternInstanceSelector.add(instanceSearch, BorderLayout.SOUTH);

        BorderLayout layout = (BorderLayout) w.getContentPane().getLayout();
        //remove it if its already here - think of this as a new query
        Component c = layout.getLayoutComponent(BorderLayout.CENTER);
        if (c != null){
            w.getContentPane().remove(c);
        }

        w.getContentPane().add(patternInstanceSelector, BorderLayout.CENTER);
        w.getContentPane().revalidate();

    }

    private PatternInstance getPatternInstanceFromTableIndex(int tableIndex, Map<SoftwareVersion, List<PatternInstance>> instances){
        PatternInstance toRet = null;

        int counter = 0;
        for (SoftwareVersion v : instances.keySet()){
            for (PatternInstance pi : instances.get(v)){
                if (counter == tableIndex){
                    return pi;
                }else{
                    counter++;
                }
            }
        }
        return toRet;
    }

    private JTable buildJTableFromMapData(Map<SoftwareVersion, List<PatternInstance>> mapData){
        //https://stackoverflow.com/questions/16795437/populating-jtable-with-map-data
        String columnNames[] = { "Version Number", "Pattern Instance Primary Role", "Pattern Instance Second Primary Role" };

        int totalPatternInstances = 1;
        //calcualte size of array.. ugly af.
        for (SoftwareVersion v : mapData.keySet()) {
            totalPatternInstances += mapData.get(v).size();
        }
        System.out.println(totalPatternInstances);

        String[][] data = new String[totalPatternInstances][3];

        int nextRow = 0;
        int i = 0;
        for (SoftwareVersion v : mapData.keySet()){

            List<PatternInstance> pis = mapData.get(v);

            if (pis != null) {
                for (int j = 0; j < pis.size(); j++) {
                    //System.out.println("I is " + i + "  and j is " + j);
                    int total = (i * j) + j;
                    data[nextRow][0] = v.getVersionNum() + "";//use this if I want a string 'Version x' in teh cell --- v.toString(); //version, might consider changing to mapData.get(i).
                    data[nextRow][1] = pis.get(j).getValueOfMajorRole(pis.get(j));//primary role only
                    data[nextRow][2] = pis.get(j).getValueOfSecondMajorRole(pis.get(j)); //secondary role
                    nextRow++;
                }
            }
            i++;
        }
        return new JTable(data, columnNames);
    }

    private MouseAdapter getPatternTypePanelMouseAdapter(){
        return new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                JList list = (JList)evt.getSource();
                if (evt.getClickCount() == 2) {

                    // Double-click detected
                    int index = list.locationToIndex(evt.getPoint());
                    selectPatternInstance(index);

                } else if (evt.getClickCount() == 3) {

                    // Triple-click detected
                    int index = list.locationToIndex(evt.getPoint());
                }
            }
        };
    }



}
