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

import javafx.util.Pair;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/***
 * class reponsible for holding all structural roles, such as Classifiers, Classes, and Features (Attributes and Operations)
 */
@Getter
public class StructuralRole extends Role{

    protected String type;
    private List<AttributeRole> attributes;
    private List<OperationRole> operations;

    public StructuralRole(String lineDescription){
        super(lineDescription);
    }

    /***
     * input: Classifier |Receiver,1..*[]{|Action():void,1..*}
     * @param lineDescription
     */
    @Override
    protected void parseLineDescription(String lineDescription) {
        //https://stackoverflow.com/questions/237061/using-regular-expressions-to-extract-a-value-in-java
        Pattern p = Pattern.compile("([a-zA-Z]+)\\s(\\|[a-zA-Z]+),(\\d\\.\\.[\\d|\\*])(\\[.*\\])(\\{.*\\})");
        Matcher m = p.matcher(lineDescription);

        if (m.find()) {
            //group counts can include index length of array.
            type = m.group(1);
            name = m.group(2);
            multiplicity = findMultiplicity(m.group(3));
            buildAttribues(m.group(4));
            buildOperations(m.group(5));
        }
    }

    /***
     * input will be [|state:|Receiver,1..1], and potentially [|state:|Receiver,1..1;|2:|CLASSIFIER,1..1]
     * @param s
     */
    private void buildAttribues(String s){
        attributes = new ArrayList<>();
        if (s.equals("[]")){
            //no attributes
            return;
        }else{
            s = s.replace("[", "");
            s = s.replace("]", "");
            if (s.contains(";")){
                //contains more than 1 attribute.
                String[] splitter = s.split(";");
                for (int i = 0; i < splitter.length; i++){
                    attributes.add(new AttributeRole(splitter[i]));
                }
            }else{
                attributes.add(new AttributeRole(s));
            }
        }
    }

    private void buildOperations(String s){
        operations = new ArrayList<>();
        if (s.equals("{}")){
            //no operations
            return;
        }else{
            s = s.replace("{", "");
            s = s.replace("}", "");
            if (s.contains(";")){
                String[] splitter = s.split(";");
                for (int i = 0; i < splitter.length; i++){
                    operations.add(new OperationRole(splitter[i]));
                }
            }else{
                operations.add(new OperationRole(s));
            }
        }
    }

    @Override
    protected void printSummary() {
        System.out.println("Structural Aspect");
        System.out.println("Name: " + name);
        System.out.println("Type: " + type);
        System.out.println("Multiplicities: " + multiplicity.getKey() + ".." + multiplicity.getValue());
        for (AttributeRole attr : attributes){
            attr.printSummary();
        }
        for (OperationRole op : operations){
            op.printSummary();
        }
    }
}
