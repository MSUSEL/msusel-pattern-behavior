package com.derek;

import com.derek.model.patterns.PatternInstance;
import com.derek.uml.UMLAttribute;
import com.derek.uml.UMLClassDiagram;
import com.derek.uml.UMLClassifier;
import com.derek.uml.UMLOperation;
import javafx.util.Pair;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class TemplateMethodPattern extends PatternMapper{

    private Pair<String, UMLClassifier> templateClassifier;
    private List<Pair<String, UMLOperation>> templateOperations;

    public TemplateMethodPattern(PatternInstance pi, UMLClassDiagram umlClassDiagram) {
        super(pi, umlClassDiagram);
    }

    @Override
    protected void mapToUML() {
        templateClassifier = new Pair<>("AbstractTemplate", getOneMajorRole(pi));
        List<Pair<String, String>> minorRoles = pi.getMinorRoles();
        List<Pair<String, String>> templateOperationsString = new ArrayList<>();
        for (Pair<String, String> p : minorRoles){
            if (p.getKey().equals("TemplateMethod()")){
                templateOperationsString.add(new Pair<>("TemplateMethod", p.getValue()));
            }
        }
        templateOperations= new ArrayList<>();
        for (Pair<String, String> s : templateOperationsString){
            templateOperations.add(new Pair<>(s.getKey(), getOperationFromString(templateClassifier.getValue(), s.getValue())));
        }
    }

    @Override
    public List<Pair<String, UMLClassifier>> getClassifierModelBlocks() {
        List<Pair<String, UMLClassifier>> blocks = new ArrayList<>();
        blocks.add(templateClassifier);
        return blocks;
    }

    @Override
    public List<Pair<String, UMLOperation>> getOperationModelBlocks() {
        return templateOperations;
    }

    @Override
    public List<Pair<String, UMLAttribute>> getAttributeModelBlocks() {
        return new ArrayList<>();
    }

    @Override
    public void printSummary() {
        System.out.println("Template role: " + templateClassifier.getValue().getName());
        for (Pair<String, UMLOperation> op : templateOperations){
            System.out.println("Template() role (operation): " + op.getValue().getName());
        }
    }
}
