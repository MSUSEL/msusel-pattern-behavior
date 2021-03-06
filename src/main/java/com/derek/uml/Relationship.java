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
import com.google.common.base.Objects;
import lombok.Getter;


@Getter
public class Relationship{
    private UMLClassifier from;
    private UMLClassifier to;
    private RelationshipType relationshipType;

    public Relationship(UMLClassifier from, UMLClassifier to, RelationshipType relationshipType){
        this.from = from;
        this.to = to;
        this.relationshipType = relationshipType;
    }

    @Override
    public boolean equals(Object o){
        if (o instanceof Relationship){
            Relationship other = (Relationship)o;
            if (this.getFrom().equals(other.getFrom())){
                if (this.getTo().equals(other.getTo())){
                    if (this.getRelationshipType().equals(other.getRelationshipType())){
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /***
     * Overriding hashcode so that sets will allow for relationships.
     * @return
     */
    @Override
    public int hashCode(){
        return Objects.hashCode(this.from, this.to, this.relationshipType);
    }

    /***
     * slightly different equals() override, meant to check if two relationships are equal even if they are different classifier obj ids.
     * This is used to track classifiers across versions.
     * @param o
     * @return
     */
    public boolean equalsFromClassifierName(Object o){
        if (o instanceof Relationship){
            Relationship other = (Relationship)o;
            if (this.getFrom().getName().equals(other.getFrom().getName())){
                if (this.getTo().getName().equals(other.getTo().getName())){
                    if (this.getRelationshipType().equals(other.getRelationshipType())){
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public String toString(){
        return this.getFrom().getName() + " to " + this.getTo().getName() + "\n";
    }

}
