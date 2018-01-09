package com.derek.uml;

import lombok.Getter;

@Getter
public class UMLAttribute {

    private String name;

    //keeping as a String and not an enum because the datatype this takes on could be a class, and I want to
    //dynamically build them and not have static via an enum.
    private String dataType;
    private Visibility visibility;
    private boolean isStatic;
    private boolean isFinal;

    public UMLAttribute(String name, String dataType, Visibility visibility) {
        this.name = name;
        this.dataType = dataType;
        this.visibility = visibility;
    }
}
