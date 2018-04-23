package com.derek.rbml;

import lombok.Getter;
import lombok.Setter;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Getter
public class InteractionRole extends Role {

    private String operationRoleString;
    private String structuralRoleString;

    @Setter
    private OperationRole operationRole;
    @Setter
    private StructuralRole structuralRole;


    public InteractionRole(String lineDescription) {
        super(lineDescription);
    }

    /***
     * input is of form: |Request(){|Context}, where |Context is the owning classifier name,
     * |Request is the operation role that classifier calls.
     * @param lineDescription
     */
    @Override
    protected void parseLineDescription(String lineDescription) {
        Pattern p = Pattern.compile("(\\|[a-zA-Z]+\\(\\))\\{(\\|[a-zA-Z]+)\\}");
        Matcher m = p.matcher(lineDescription);

        if (m.find()) {
            //remove the parens.
            operationRoleString = m.group(1).replace("()","");
            structuralRoleString = m.group(2);
        }
    }

    @Override
    protected void printSummary() {
        System.out.println("Class called: " + structuralRoleString);
        System.out.println("Operation called: " + operationRoleString);
    }
}
