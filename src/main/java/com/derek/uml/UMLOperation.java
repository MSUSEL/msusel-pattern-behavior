package com.derek.uml;

import javafx.util.Pair;

import java.util.List;

public class UMLOperation {

    private String name;
    //list of pairs, where each pair corresponds to a datatype and a name.
    private List<Pair<String,String>> parameters;

    //datatype of return value, empty string if void.
    private String returnDataType;
    private Visibility visibility;
    private boolean isStatic;

    public UMLOperation(String name, List<Pair<String, String>> parameters, String returnDataType, Visibility visibility) {
        this.name = name;
        this.parameters = parameters;
        this.returnDataType = returnDataType;
        this.visibility = visibility;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Pair<String, String>> getParameters() {
        return parameters;
    }

    public void setParameters(List<Pair<String, String>> parameters) {
        this.parameters = parameters;
    }

    public String getReturnDataType() {
        return returnDataType;
    }

    public Visibility getVisibility() {
        return visibility;
    }

    public boolean isStatic() {
        return isStatic;
    }

    public void setStatic(boolean aStatic) {
        isStatic = aStatic;
    }


    public String buildParamsForPlantUMLDiagram(){
        StringBuilder s = new StringBuilder();
        //we have params
        for (int i = 0; i < parameters.size(); i++){
            Pair<String, String> param = parameters.get(i);
            if (i == parameters.size() - 1){
                if (param.getKey() == ""){
                    //no params - this will happen during the first iteration of the loop and only if there are no params.
                    return "";
                }else {
                    //last param in param list
                    s.append(param.getKey() + " " + param.getValue());
                }
            }else{
                //defualt case in a sense; multiple params and we are not on the last one
                s.append(param.getKey() + " " + param.getValue() + ", ");
            }
        }
        return s.toString();
    }
}
