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
public class UMLInterface extends UMLClassifier {

    private List<UMLOperation> operations;

    public UMLInterface(String name, List<String> residingPackage, List<List<String>> imports, List<UMLOperation> operations, List<String> extendsParents, List<String> implementsParents) {
        super(name, residingPackage, imports);
        this.operations = operations;
        this.extendsParentsString = extendsParents;
        this.implementsParentsString = implementsParents;
    }

    @Override
    public List<UMLAttribute> getAttributes() {
        //interfaces don't have attributes, so this will always return null. If this bugs out consider returning an empty (but initialized) list
        //it bugged - so now I'm returning an empty arraylist
        return new ArrayList<>();
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
        }
        return implementsParentsString;
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
        output.append("interface " + this.getName() + "{\n");
        for (UMLOperation operation : this.getOperations()){
            output.append("\t" + operation.getName() + "(");
            output.append(operation.buildParamsForPlantUMLDiagram());
            output.append(") : " + operation.getStringReturnDataType()  + "\n");
        }
        output.append("}\n");
        return output.toString();
    }
}
