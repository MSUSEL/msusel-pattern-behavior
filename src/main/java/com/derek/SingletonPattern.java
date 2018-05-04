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
public class SingletonPattern extends PatternMapper {

    private Pair<String, UMLClassifier> singletonClassifier;
    private Pair<String, UMLAttribute> singletonAttribute;

    public SingletonPattern(PatternInstance pi, UMLClassDiagram umlClassDiagram) {
        super(pi, umlClassDiagram);
    }

    @Override
    protected void mapToUML() {
        singletonClassifier = new Pair<>("Singleton", getOneMajorRole(pi));
        List<Pair<String, String>> minorRoles = pi.getMinorRoles();
        //should only be 1 attribute for a singleton.
        Pair<String, String> attribute = minorRoles.get(0);
        singletonAttribute = new Pair<>("UniqueInstance", getAttributeFromString(singletonClassifier.getValue(), attribute.getValue()));
    }

    @Override
    public List<Pair<String, UMLClassifier>> getClassifierModelBlocks() {
        List<Pair<String, UMLClassifier>> blocks = new ArrayList<>();
        blocks.add(singletonClassifier);
        return blocks;
    }

    @Override
    public List<Pair<String, UMLOperation>> getOperationModelBlocks() {
        return new ArrayList<>();
    }

    @Override
    public List<Pair<String, UMLAttribute>> getAttributeModelBlocks() {
        List<Pair<String, UMLAttribute>> atts = new ArrayList<>();
        atts.add(singletonAttribute);
        return atts;
    }

    @Override
    public void printSummary() {
        System.out.println("Singleton role: " + singletonClassifier.getValue().getName());
        System.out.println("Singleton attribute role (attribute): " + singletonAttribute.getValue().getName());
    }
}
