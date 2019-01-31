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
import lombok.Setter;

@Getter
public class UMLAttribute {

    private String name;

    //keeping as a String and not an enum because the datatype this takes on could be a class, and I want to
    //dynamically build them and not have static via an enum.
    private String stringDataType;
    private Visibility visibility;
    private boolean isStatic;
    private boolean isFinal;

    //type will be set after the first passthrough.
    @Setter
    private UMLClassifier type;

    @Setter
    private UMLClassifier owningClassifier;

    public UMLAttribute(String name, String stringDataType, Visibility visibility) {
        this.name = name;
        this.stringDataType = stringDataType;
        this.visibility = visibility;
    }
    public UMLAttribute(String name, String stringDataType) {
        this.name = name;
        this.stringDataType = stringDataType;
    }
}
