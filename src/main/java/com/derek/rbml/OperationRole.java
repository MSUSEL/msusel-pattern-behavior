package com.derek.rbml;

import javafx.util.Pair;
import lombok.Getter;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Getter
public class OperationRole extends Role {

    protected String type;

    //while not explicitly required, at most we will have one param name and param type.
    private String paramName;
    private String paramType;

    public OperationRole(String lineDescription) {
        super(lineDescription);
    }

    @Override
    protected void parseLineDescription(String lineDescription) {
        //parameters not implemented yet. soon though.
        Pattern p = Pattern.compile("\\{(\\|[a-zA-Z]+)(\\([\\|[a-zA-Z]+,]*\\*{0,1}\\)):(\\|[a-zA-Z]+|void|\\*),(\\d\\.\\.[\\d|\\*])\\}");
        Matcher m = p.matcher(lineDescription);
        m.find();
        if (m.matches()){
            name = m.group(1);
            //type might be void, which is the big difference from attributes.
            //group 2 is parameters... not implemented yet... and i might not need it.
            type = m.group(3);
            multiplicity = findMultiplicity(m.group(4));
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
