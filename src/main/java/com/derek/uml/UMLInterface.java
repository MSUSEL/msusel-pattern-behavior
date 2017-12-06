package com.derek.uml;

import java.util.List;

public class UMLInterface extends UMLClassifier {

    private List<UMLOperation> operations;

    public UMLInterface(String name, List<UMLOperation> operations) {
        super(name);
        this.operations = operations;
    }

    public List<UMLOperation> getOperations() {
        return operations;
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
