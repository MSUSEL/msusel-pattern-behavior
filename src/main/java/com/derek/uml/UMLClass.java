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

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class UMLClass extends UMLClassifier {

    protected Visibility visibility;
    protected boolean isAbstract;
    protected List<UMLAttribute> attributes;
    protected List<UMLOperation> operations;
    protected List<UMLOperation> constructors;

    public UMLClass(String name, List<String> residingPackage, List<List<String>> imports, List<UMLAttribute> attributes, List<UMLOperation> operations, List<UMLOperation> constructors,
                    boolean isAbstract, List<String> extendsParents, List<String> implementsParents, String identifier) {
        super(name, residingPackage, imports, identifier);
        this.attributes = attributes;
        this.operations = operations;
        this.constructors = constructors;
        this.isAbstract = isAbstract;
        this.extendsParentsString = extendsParents;
        this.implementsParentsString = implementsParents;
    }
    public List<String> getExtendsParentsString(){
        if (extendsParentsString == null){
            return new ArrayList<>();
        }else{
            return extendsParentsString;
        }
    }
    public List<String> getImplementsParentsString(){
        if (implementsParentsString == null){
            return new ArrayList<>();
        }else{
            return implementsParentsString;
        }
    }

    @Override
    public List<UMLClassifier> getExtendsParents() {
        if (extendsParents == null){
            return new ArrayList<>();
        }else{
            return extendsParents;
        }
    }

    @Override
    public List<UMLClassifier> getImplementsParents() {
        if (implementsParents == null){
            return new ArrayList<>();
        }else{
            return implementsParents;
        }
    }

    @Override
    public String plantUMLTransform() {
        StringBuilder output = new StringBuilder();
        if (isAbstract){
            output.append("abstract ");
        }
        output.append(identifier + " " + this.getName() + "{\n");
        for (UMLAttribute attribute : this.getAttributes()){
            output.append("\t" + attribute.getName() + " : " + attribute.getStringDataType() + "\n");
        }
        for (UMLOperation constructor : this.getConstructors()){
            output.append("\t" + constructor.getName() + "(");
            output.append(constructor.buildParamsForPlantUMLDiagram());
            output.append(")\n");
        }
        for (UMLOperation operation : this.getOperations()){
            output.append("\t" + operation.getName() + "(");
            output.append(operation.buildParamsForPlantUMLDiagram());
            output.append(") : " + operation.getStringReturnDataType()  + "\n");
        }
        output.append("}\n");
        return output.toString();
    }

    @Override
    public List<UMLOperation> getOperationsIncludingConstructorsIfExists() {
        List<UMLOperation> ops = new ArrayList<>();
        ops.addAll(getConstructors());
        ops.addAll(getOperations());
        return ops;
    }

    /***
     * this one is tricky because I need to return all super-class attributes as well as attributes within the scope of htis class.
     * @return
     */
    @Override
    public List<UMLAttribute> getAttributes(){
        List<UMLAttribute> attributes = new ArrayList<>();
        attributes.addAll(this.attributes);
        for (UMLClassifier umlClassifier : this.getExtendsParents()){
            attributes.addAll(umlClassifier.getAttributes());
        }
        return attributes;
    }

    public List<UMLAttribute> getLocalAttributes(){
        if (attributes == null){
            attributes = new ArrayList<>();
        }
        return attributes;
    }

}
