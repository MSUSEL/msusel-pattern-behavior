package com.derek.rbml;

import javafx.util.Pair;
import lombok.Getter;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Getter
public class RoleOperation extends Role {

    protected String type;
    private Pair<Integer, Integer> multiplicity;

    //while not explicitly required, at most we will have one param name and param type.
    private String paramName;
    private String paramType;

    public RoleOperation(String lineDescription) {
        super(lineDescription);
    }

    @Override
    protected void parseLineDescription(String lineDescription) {
        //parameters not implemented yet. soon though.
        Pattern p = Pattern.compile("\\{(\\|[a-zA-Z]+)\\(\\):(\\|[a-zA-Z]+|void),(\\d\\.\\.[\\d|\\*])\\}");
        Matcher m = p.matcher(lineDescription);
        if (m.matches()){
            name = m.group(1);
            //type might be void, which is the big difference from attributes.
            type = m.group(2);
            multiplicity = findMultiplicity(m.group(3));
        }
    }

    @Override
    protected void printSummary() {
        System.out.println("Operation");
        System.out.println("Name: " + name);
        System.out.println("Type: " + type);
        System.out.println("Multiplicities: " + multiplicity.getKey() + ".." + multiplicity.getValue());
    }

}
