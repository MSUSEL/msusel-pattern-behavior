package com.derek.rbml;

import javafx.util.Pair;
import lombok.Getter;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Getter
public class AttributeRole extends Role {

    protected String type;

    public AttributeRole(String lineDescription) {
        super(lineDescription);
    }

    /***
     * input will look like: [|state:|Receiver,1..1]
     * @param lineDescription
     */
    @Override
    protected void parseLineDescription(String lineDescription) {
        Pattern p = Pattern.compile("(\\|[a-zA-Z]+):(\\|[a-zA-Z]+|\\*),(\\d\\.\\.[\\d|\\*])");
        Matcher m = p.matcher(lineDescription);
        if (m.matches()){
            name = m.group(1);
            type = m.group(2);
            multiplicity = findMultiplicity(m.group(3));
        }
    }

    @Override
    protected void printSummary() {
        System.out.println("Attribute");
        System.out.println("Name: " + name);
        System.out.println("Type: " + type);
        System.out.println("Multiplicities: " + multiplicity.getKey() + ".." + multiplicity.getValue());
    }
}
