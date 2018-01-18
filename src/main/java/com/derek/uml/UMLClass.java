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
package com.derek.uml;

import javafx.util.Pair;
import jdk.nashorn.internal.runtime.regexp.joni.constants.OPCode;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class UMLClass extends UMLClassifier{

    protected Visibility visibility;
    protected boolean isAbstract;
    protected List<UMLAttribute> attributes;
    protected List<UMLOperation> operations;
    protected List<UMLOperation> constructors;
    protected List<String> extendsParents;
    protected List<String> implementsParents;
    protected String identifier;

    public UMLClass(String name, List<String> residingPackage, List<List<String>> imports, List<UMLAttribute> attributes, List<UMLOperation> operations, List<UMLOperation> constructors,
                    boolean isAbstract, List<String> extendsParents, List<String> implementsParents, String identifier) {
        super(name, residingPackage, imports);
        this.attributes = attributes;
        this.operations = operations;
        this.constructors = constructors;
        this.isAbstract = isAbstract;
        this.extendsParents = extendsParents;
        this.implementsParents = implementsParents;
        this.identifier = identifier;
    }
    public List<String> getExtendsParents(){
        if (extendsParents == null){
            return new ArrayList<>();
        }else{
            return extendsParents;
        }
    }
    public List<String> getImplementsParents(){
        if (implementsParents== null){
            return new ArrayList<>();
        }else{
        }
        return implementsParents;
    }

    @Override
    public String plantUMLTransform() {
        StringBuilder output = new StringBuilder();
        if (isAbstract){
            output.append("abstract ");
        }
        output.append(identifier + " " + this.getName() + "{\n");
        for (UMLAttribute attribute : this.getAttributes()){
            output.append("\t" + attribute.getName() + " : " + attribute.getDataType() + "\n");
        }
        for (UMLOperation constructor : this.getConstructors()){
            output.append("\t" + constructor.getName() + "(");
            output.append(constructor.buildParamsForPlantUMLDiagram());
            output.append(")\n");
        }
        for (UMLOperation operation : this.getOperations()){
            output.append("\t" + operation.getName() + "(");
            output.append(operation.buildParamsForPlantUMLDiagram());
            output.append(") : " + operation.getReturnDataType()  + "\n");
        }
        output.append("}\n");
        return output.toString();
    }

}
