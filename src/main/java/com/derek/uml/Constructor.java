package com.derek.uml;

import javafx.util.Pair;

import java.util.List;

public class Constructor {

    private String name;
    //list of pairs, where each pair corresponds to a datatype and a name.
    private List<Pair<String,String>> parameters;
    private Visibility visibility;

    public Constructor(String name, List<Pair<String, String>> parameters, Visibility visibility) {
        this.name = name;
        this.parameters = parameters;
        this.visibility = visibility;
    }

    public String getName() {
        return name;
    }

    public List<Pair<String, String>> getParameters() {
        return parameters;
    }

    public Visibility getVisibility() {
        return visibility;
    }
}
