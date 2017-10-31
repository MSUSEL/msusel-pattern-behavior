package com.derek.view;

import com.derek.model.Model;
import com.derek.model.PatternType;
import com.derek.model.SoftwareVersion;
import com.derek.model.patterns.PatternInstance;

import javax.swing.*;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

public class View {

    public Window w;
    public Model model;

    public View(){
        model = new Model();
        defaultView();
    }

    public void defaultView(){
        w = new Window();
        w.setSize(1200, 800);
        buildLeftPanel(w);


        w.setVisible(true);
    }

    private void buildLeftPanel(Window w){
        JPanel leftPanelSelector = new JPanel(new BorderLayout());
        DefaultListModel stringOptions = new DefaultListModel();
        stringOptions.addElement("Pattern Evolution");
        stringOptions.addElement("Pattern Coupling");
        JList<String> options = new JList<>(stringOptions);
        options.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);



        //https://stackoverflow.com/questions/4344682/double-click-event-on-jlist-element
        options.addMouseListener(getDefaultLeftPanelMouseAdapter());

        JScrollPane jsp = new JScrollPane(options);

        JButton searchButton = new JButton("Search");
        //requires more logic to implement. TODO (might not even be worth it honestly)
//        searchButton.addActionListener(new ActionListener() {
//            public void actionPerformed(ActionEvent e) {
//                System.out.println("You clicked the button, using an ActionListener");
//            }
//        });

        leftPanelSelector.add(jsp, BorderLayout.NORTH);
        leftPanelSelector.add(searchButton, BorderLayout.SOUTH);
        leftPanelSelector.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
        w.add(leftPanelSelector, BorderLayout.WEST);
    }

    private void selectPatternType(){
        JPanel patternTypeSelector = new JPanel(new BorderLayout());
        DefaultListModel stringOptions = new DefaultListModel();
        for (PatternType tp : model.getPatternSummaryTable().columnKeySet()){
            stringOptions.addElement(tp.toString());
        }
        System.out.println(" options menu size " + stringOptions.size());
        JList<String> options = new JList<>(stringOptions);
        options.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        //https://stackoverflow.com/questions/4344682/double-click-event-on-jlist-element
        options.addMouseListener(getPatternTypePanelMouseAdapter());

        JScrollPane jsp = new JScrollPane(options);

        patternTypeSelector.add(jsp, BorderLayout.NORTH);
        patternTypeSelector.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
        w.getContentPane().add(patternTypeSelector, BorderLayout.CENTER);
        w.getContentPane().revalidate();

    }

    private void selectPatternInstance(int indexSelected){
        JPanel patternInstanceSelector = new JPanel(new BorderLayout());
        //DefaultListModel stringOptions = new DefaultListModel();
        JTable table;


        //Map<SoftwareVersion, List<PatternInstance>> instances = model.getPatternSummaryTable().column(model.getPatternTypeFromIndex(indexSelected));
        Map<SoftwareVersion, List<PatternInstance>> instances = model.getPatternSummaryTable().columnMap().get(model.getPatternTypeFromIndex(indexSelected));

        table = buildJTableFromMapData(instances);

        System.out.println(" pattern instances row count: " + table.getRowCount());

        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        //https://stackoverflow.com/questions/4344682/double-click-event-on-jlist-element
        table.addMouseListener(getPatternInstanceMouseAdapter());

        JScrollPane jsp = new JScrollPane(table);

        patternInstanceSelector.add(jsp, BorderLayout.NORTH);
        patternInstanceSelector.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
        w.getContentPane().add(patternInstanceSelector, BorderLayout.SOUTH);
        w.getContentPane().revalidate();
    }

    private MouseAdapter getPatternInstanceMouseAdapter(){
        return new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                JTable selector = (JTable)evt.getSource();
                if (evt.getClickCount() == 2) {

                    // Double-click detected
                    int index = selector.getSelectedRow();
                    System.out.println("Selected pattern instance " + index);

                } else if (evt.getClickCount() == 3) {

                    // Triple-click detected
                    int index = selector.getSelectedRow();
                }
            }
        };
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

            System.out.println("Version: " + i + "     Number of pattern instances " + pis.size());
            if (pis != null) {
                for (int j = 0; j < pis.size(); j++) {
                    //System.out.println("I is " + i + "  and j is " + j);
                    int total = (i * j) + j;
                    System.out.println("Combined data value is " + total);
                    data[nextRow][0] = v.toString(); //version, might consider changing to mapData.get(i).
                    data[nextRow][1] = pis.get(j).getValueOfMajorRole(pis.get(j));//primary role only
                    data[nextRow][2] = pis.get(j).getValueOfSecondMajorRole(pis.get(j)); //secondary role
                    nextRow++;
                }
            }
            i++;
        }
        return new JTable(data, columnNames);
    }

    private void selectSoftwareVersion(){
        //TODO
        System.out.println("TODO");
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

    private MouseAdapter getDefaultLeftPanelMouseAdapter(){
        return new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                JList list = (JList)evt.getSource();
                if (evt.getClickCount() == 2) {

                    // Double-click detected
                    int index = list.locationToIndex(evt.getPoint());
                    switch(index){
                        case 0:
                            System.out.println("entering select pattern type");
                            selectPatternType(); break;
                        case 1: selectSoftwareVersion(); break;
                    }
                } else if (evt.getClickCount() == 3) {

                    // Triple-click detected
                    int index = list.locationToIndex(evt.getPoint());
                }
            }
        };
    }

}
