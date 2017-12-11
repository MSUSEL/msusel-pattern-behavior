package com.derek.uml;

import com.google.common.collect.Table;

import java.util.ArrayList;
import java.util.List;

/**
 * An enum can be simple, with only type declaration, or can be complicated with allowable values for type decs, constructors, and operations
 */
public class UMLEnum extends UMLClass {

    //types in an enum can be declared {FOO, BAR}, or as allowable values {FOO("fooA", "fooB"), BAR("barA", "barB")}
    private Table<String, String, String> types;

    public UMLEnum(String name, Table<String, String, String> types){
        super(name, null,  null, null, false);
        this.types = types;
    }

    public UMLEnum(String name, Table<String, String, String> types, List<UMLAttribute> attributes, List<UMLOperation> operations, List<UMLOperation> constructors){
        //can't have an abstract enum
        super(name, attributes, operations, constructors, false);
        this.types = types;
    }

    @Override
    public String plantUMLTransform() {
        StringBuilder output = new StringBuilder();
        output.append("enum " + this.getName() + "{\n");
        for (String type : this.getTypes().columnKeySet()){
            output.append("\t" + type + "\n");
        }
        output.append("}\n");
        return output.toString();
    }

    public Table<String, String, String> getTypes() {
        return types;
    }
}
