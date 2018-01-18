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

import java.util.List;

//base class that represents a uml classifier.
@Getter
public abstract class UMLClassifier {
    protected String name;
    protected List<String> residingPackage;
    protected List<List<String>> imports;

    public UMLClassifier(String name, List<String> residingPackage, List<List<String>> imports){
        this.name = name;
        this.residingPackage = residingPackage;
        this.imports = imports;
    }


    public abstract String plantUMLTransform();
    //returns operations
    public abstract List<UMLOperation> getOperations();
    //returns attributes or null if the holding class does not have any attributes
    public abstract List<UMLAttribute> getAttributes();

    //used for uml class diagram generation
    public abstract List<String> getExtendsParents();
    public abstract List<String> getImplementsParents();
}
