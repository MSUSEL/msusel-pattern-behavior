package com.derek.uml;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class UMLInterface extends UMLClassifier {

    private List<UMLOperation> operations;

    public UMLInterface(String name, List<UMLOperation> operations) {
        super(name);
        this.operations = operations;
    }

    @Override
    public List<UMLAttribute> getAttributes() {
        //interfaces don't have attributes, so this will always return null. If this bugs out consider returning an empty (but initialized) list
        //it bugged - so now I'm returning an empty arraylist
        return new ArrayList<>();
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
