/**
 * MIT License
 *
 * Copyright (c) 2017 Montana State University, Gianforte School of Computing
 * Software Engineering Laboratory
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.derek.rbml;

import lombok.Getter;
import lombok.Setter;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Getter
public class InteractionRole extends Role {

    //upon initial file parsing, roles will be stored as strings. In a second pass, after SPS has been configured, they
    //will be set to actual SPS Role objects.
    private String operationRoleString;
    private String structuralRoleString;
    //role type will be decls, control structures, or 'standard'
    protected InteractionRoleType roleType;

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
     * input can also be control structures (will be denoted with a '<loop>').. at this point its only loops I care about.
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
            this.name = operationRoleString;
            roleType = InteractionRoleType.STANDARD;
        }else{
            //might be a control structure (loop)
            if (lineDescription.equals("{loop}")){
                this.name = "loop";
                roleType = InteractionRoleType.CONTROL_STRUCTURE;
            }else if (lineDescription.equals("{/loop}")){
                this.name = "loop";
                roleType = InteractionRoleType.CONTROL_STRUCTURE;
            }else if (lineDescription.equals("{conditional}")){
                this.name = "conditional";
                roleType = InteractionRoleType.CONTROL_STRUCTURE;
            }else if (lineDescription.equals("{/conditional}")){
                this.name = "conditional";
                roleType = InteractionRoleType.CONTROL_STRUCTURE;
            }
        }
    }

    @Override
    protected void printSummary() {
        System.out.println("Class called: " + structuralRoleString);
        System.out.println("Operation called: " + operationRoleString);
    }
}
