package com.derek.uml;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class UMLInterface extends UMLClassifier {

    private List<UMLOperation> operations;
    private List<String> extendsParents;
    private List<String> implementsParents;

    public UMLInterface(String name, List<String> residingPackage, List<List<String>> imports, List<UMLOperation> operations, List<String> extendsParents, List<String> implementsParents) {
        super(name, residingPackage, imports);
        this.operations = operations;
        this.extendsParents = extendsParents;
        this.implementsParents = implementsParents;
    }

    @Override
    public List<UMLAttribute> getAttributes() {
        //interfaces don't have attributes, so this will always return null. If this bugs out consider returning an empty (but initialized) list
        //it bugged - so now I'm returning an empty arraylist
        return new ArrayList<>();
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
        output.append("interface " + this.getName() + "{\n");
        for (UMLOperation operation : this.getOperations()){
            output.append("\t" + operation.getName() + "(");
            output.append(operation.buildParamsForPlantUMLDiagram());
            output.append(") : " + operation.getReturnDataType()  + "\n");
        }
        output.append("}\n");
        return output.toString();
    }
}
