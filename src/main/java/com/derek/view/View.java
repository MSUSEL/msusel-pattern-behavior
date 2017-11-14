package com.derek.view;

import com.derek.model.Model;
import com.derek.model.PatternType;
import com.derek.model.SoftwareVersion;
import com.derek.model.patterns.PatternInstance;
import com.google.common.graph.MutableGraph;

import javax.swing.*;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

public class View {

    public Window w;
    private Model model;

    public View(){
        model = new Model();
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

    private DefaultListModel getPatternTypes(){
        DefaultListModel toRet = new DefaultListModel();
        for (PatternType pt : model.getPatternSummaryTable().columnKeySet()){
            toRet.addElement(pt.toString());
        }
        return toRet;
    }

    private void buildLeftPanel(){
        JPanel leftPanelSelector = new JPanel(new BorderLayout());
        DefaultListModel defaultStringOptions = new DefaultListModel();

        defaultStringOptions.addElement("Pattern Evolution");
        defaultStringOptions.addElement("Pattern Coupling");
        JList<String> defaultOptions = new JList<>(defaultStringOptions);
        JList<String> patternEvolutionOptions = new JList<>(getPatternTypes());
        defaultOptions.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        patternEvolutionOptions.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        //https://stackoverflow.com/questions/4344682/double-click-event-on-jlist-element
        //defaultOptions.addMouseListener(getDefaultLeftPanelMouseAdapter());


        //this may need to change
        //patternEvolutionOptions.addMouseListener(getPatternTypePanelMouseAdapter());

        JScrollPane defaultJsp = new JScrollPane(defaultOptions);
        JScrollPane patternEvolutionJSP = new JScrollPane(patternEvolutionOptions);


        JPanel cards = new JPanel(new CardLayout());
        CardLayout cardLayout = (CardLayout) cards.getLayout();
        cards.add(defaultJsp);
        cards.add(patternEvolutionJSP);

        JPanel buttonPanel = new JPanel(new BorderLayout());
        JButton prev = new JButton("Previous");
        JButton next = new JButton("Next");



        buttonPanel.add(prev, BorderLayout.WEST);
        prev.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                cardLayout.previous(cards);
            }
        });

        next.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                cardLayout.next(cards);
            }
        });

        buttonPanel.add(next, BorderLayout.EAST);
        cards.add(buttonPanel, BorderLayout.SOUTH);
        //leftPanelSelector.add(buttonPanel, BorderLayout.SOUTH);

        leftPanelSelector.add(cards, BorderLayout.WEST);
        leftPanelSelector.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
        w.getContentPane().add(leftPanelSelector, BorderLayout.WEST);
    }

    private void selectPatternType(){
        JPanel patternTypeSelector = new JPanel(new BorderLayout());
        DefaultListModel stringOptions = new DefaultListModel();
        for (PatternType tp : model.getPatternSummaryTable().columnKeySet()){
            stringOptions.addElement(tp.toString());
        }
        JList<String> options = new JList<>(stringOptions);
        options.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        //https://stackoverflow.com/questions/4344682/double-click-event-on-jlist-element
        options.addMouseListener(getPatternTypePanelMouseAdapter());

        JScrollPane jsp = new JScrollPane(options);

        patternTypeSelector.add(jsp, BorderLayout.WEST);
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

        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        //https://stackoverflow.com/questions/4344682/double-click-event-on-jlist-element
        table.addMouseListener(getPatternInstanceMouseAdapter(instances));

        JScrollPane jsp = new JScrollPane(table);

        patternInstanceSelector.add(jsp, BorderLayout.NORTH);
        patternInstanceSelector.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));

        BorderLayout layout = (BorderLayout) w.getContentPane().getLayout();
        //remove it if its already here - think of this as a new query
        Component c = layout.getLayoutComponent(BorderLayout.SOUTH);
        if (c != null){
            w.getContentPane().remove(c);
        }

        w.getContentPane().add(patternInstanceSelector, BorderLayout.SOUTH);
        w.getContentPane().revalidate();

    }

    private MouseAdapter getPatternInstanceMouseAdapter(Map<SoftwareVersion, List<PatternInstance>> instances ){
        return new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                JTable selector = (JTable)evt.getSource();
                if (evt.getClickCount() == 1){
                    int index = selector.getSelectedRow();
                    String indexContent = (String)selector.getValueAt(index, selector.getSelectedColumn());

                    //version is a string..
                    //need a SoftwareVersion object when I call get
                    int softwareVersionOfSelection = Integer.parseInt((String)selector.getValueAt(index, 0));
                    //I need to get a PatternInstance object here of the selected row
                    PatternInstance selection = instances.get(softwareVersionOfSelection).get(index);

                    MutableGraph<PatternInstance> evols = model.buildPatternEvolution(selection);
                    //PatternInstanceWindow piw = new PatternInstanceWindow(indexContent, model);

                    System.out.println("Querying for " + indexContent);
                }
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
