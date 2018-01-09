package com.derek.uml;

import javafx.util.Pair;
import lombok.Getter;

import java.util.List;

@Getter
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

    /***
     * Constructor without method visibility
     * @param name function name
     * @param parameters list of pairs of strings for function's params
     * @param returnDataType function return data type
     */
    public UMLOperation(String name, List<Pair<String, String>> parameters, String returnDataType) {
        this.name = name;
        this.parameters = parameters;
        this.returnDataType = returnDataType;
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
