package com.derek.uml;

import javafx.util.Pair;
import jdk.nashorn.internal.runtime.regexp.joni.constants.OPCode;

import java.util.List;

public class UMLClass extends UMLClassifier{

    private Visibility visibility;
    private boolean isAbstract;
    private List<UMLAttribute> attributes;
    private List<UMLOperation> operations;
    private List<UMLOperation> constructors;

    public UMLClass(String name, List<UMLAttribute> attributes, List<UMLOperation> operations, List<UMLOperation> constructors, boolean isAbstract) {
        super(name);
        this.attributes = attributes;
        this.operations = operations;
        this.constructors = constructors;
        this.isAbstract = isAbstract;
    }

    public Visibility getVisibility() {
        return visibility;
    }

    public void setVisibility(Visibility visibility) {
        this.visibility = visibility;
    }

    public List<UMLAttribute> getAttributes() {
        return attributes;
    }

    public void setAttributes(List<UMLAttribute> attributes) {
        this.attributes = attributes;
    }

    public List<UMLOperation> getOperations() {
        return operations;
    }

    public void setOperations(List<UMLOperation> operations) {
        this.operations = operations;
    }

    public List<UMLOperation> getConstructors() {
        return constructors;
    }

    public void setConstructors(List<UMLOperation> constructors) {
        this.constructors = constructors;
    }

    @Override
    public String plantUMLTransform() {
        StringBuilder output = new StringBuilder();
        output.append("class " + this.getName() + "{\n");
        for (UMLAttribute attribute : this.getAttributes()){
            output.append("\t" + attribute.getName() + " : " + attribute.getDataType() + "\n");
        }
        for (UMLOperation constructor : this.getConstructors()){
            output.append("\t" + constructor.getName() + "(");
            output.append(constructor.buildParamsForPlantUMLDiagram());
            output.append(")\n");
        }
        for (UMLOperation operation : this.getOperations()){
            output.append("\t" + operation.getName() + "(");
            output.append(operation.buildParamsForPlantUMLDiagram());
            output.append(") : " + operation.getReturnDataType()  + "\n");
        }
        output.append("}\n");
        return output.toString();
    }

}
