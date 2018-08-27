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

import com.derek.uml.UMLClassifier;
import org.apache.commons.lang3.tuple.MutablePair;
import org.apache.commons.lang3.tuple.Pair;
import lombok.Getter;

@Getter
public class RBMLMapping<T> {

    private Pair<Role, T> mappedPair;
    private Role role;
    private T umlArtifact;

    public RBMLMapping(Role role, T umlRole) {
        this.role = role;
        this.umlArtifact = umlRole;
        this.mappedPair = new MutablePair<>(role, umlRole);
    }

    /***
     * gets a uml classifier as correct type if the umlArtifact var is of type.
     * @return
     */
    public UMLClassifier getUMLClassifierArtifact(){
        if (umlArtifact instanceof UMLClassifier){
            return (UMLClassifier) umlArtifact;
        }
        return null;
    }

    public Pair<UMLClassifier, UMLClassifier> getRelationshipArtifact(){
        if (umlArtifact instanceof Pair){
            return (Pair<UMLClassifier, UMLClassifier>) umlArtifact;
        }
        return null;
    }

    public void printSummary(){
        System.out.println("RBML mapping from RBML element: " + mappedPair.getKey().getName() + " to " + mappedPair.getValue());
    }
}
