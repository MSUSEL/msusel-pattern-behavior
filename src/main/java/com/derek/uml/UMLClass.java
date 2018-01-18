package com.derek.uml;

import javafx.util.Pair;
import jdk.nashorn.internal.runtime.regexp.joni.constants.OPCode;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class UMLClass extends UMLClassifier{

    protected Visibility visibility;
    protected boolean isAbstract;
    protected List<UMLAttribute> attributes;
    protected List<UMLOperation> operations;
    protected List<UMLOperation> constructors;
    protected List<String> extendsParents;
    protected List<String> implementsParents;
    protected String identifier;

    public UMLClass(String name, List<UMLAttribute> attributes, List<UMLOperation> operations, List<UMLOperation> constructors,
                    boolean isAbstract, List<String> extendsParents, List<String> implementsParents, String identifier) {
        super(name);
        this.attributes = attributes;
        this.operations = operations;
        this.constructors = constructors;
        this.isAbstract = isAbstract;
        this.extendsParents = extendsParents;
        this.implementsParents = implementsParents;
        this.identifier = identifier;
    }
    public List<String> getExtendsParents(){
        if (extendsParents == null){
            return new ArrayList<>();
        }else{
            return extendsParents;
        }
    }
    public List<String> getImplementsParents(){
        if (implementsParents== null){
            return new ArrayList<>();
        }else{
        }
        return implementsParents;
    }

    @Override
    public String plantUMLTransform() {
        StringBuilder output = new StringBuilder();
        if (isAbstract){
            output.append("abstract ");
        }
        output.append(identifier + " " + this.getName() + "{\n");
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
