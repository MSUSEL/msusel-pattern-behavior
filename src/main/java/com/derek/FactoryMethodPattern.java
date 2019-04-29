package com.derek;

import com.derek.model.patterns.PatternInstance;
import com.derek.uml.UMLAttribute;
import com.derek.uml.UMLClassDiagram;
import com.derek.uml.UMLClassifier;
import com.derek.uml.UMLOperation;
import org.apache.commons.lang3.tuple.Pair;

import java.util.List;

public class FactoryMethodPattern extends PatternMapper {


    public FactoryMethodPattern(PatternInstance pi, UMLClassDiagram umlClassDiagram) {
        super(pi, umlClassDiagram);
    }

    @Override
    protected void mapToUML() {

    }

    @Override
    public List<Pair<String, UMLClassifier>> getClassifierModelBlocks() {
        return null;
    }

    @Override
    public List<Pair<String, UMLClassifier>> getClassModelBlocks() {
        return null;
    }

    @Override
    public List<Pair<String, UMLOperation>> getOperationModelBlocks() {
        return null;
    }

    @Override
    public List<Pair<String, UMLAttribute>> getAttributeModelBlocks() {
        return null;
    }

    @Override
    public List<Pair<String, UMLClassifier>> getAllParticipatingClasses() {
        return null;
    }

    @Override
    protected String getPatternCommonNamesFileName() {
        return null;
    }

    @Override
    public void printSummary() {

    }

    @Override
    protected void coalescePattern() {

    }
}
