package com.derek;

import javafx.util.Pair;

import java.util.List;

public class SPS {

    //classifier roles are declared abstractly (as a classifier) but the actual role will be either a UMLInterface or UMLClass
    //I can use instanceof to further distinguish this, but realistically I shouldn't need to type check.
    private List<String> classifierRoles;
    private List<Pair<String, String>> associationRoles;
    private List<Pair<String, String>> generalizationRoles;
    private List<Pair<String, String>> dependencyRoles;



}
