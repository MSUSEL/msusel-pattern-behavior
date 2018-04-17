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

public enum Relationship{
    REALIZATION,
    GENERALIZATION,
    ASSOCIATION,
    //I likely won't implement aggregation and composition as they are very tough to properly implement
    AGGREGATION,
    COMPOSITION,
    DEPENDENCY,
    OWNERSHIP,
    //not sure what you would use unspecified for, but im putting it in just in case
    UNSPECIFIED;

    public String plantUMLTransform(){
        switch(this){
            case REALIZATION:
                return "..|>";
            case GENERALIZATION:
                return "--|>";
            case ASSOCIATION:
                return "-->";
            case AGGREGATION:
                return "o--";
            case COMPOSITION:
                return "*--";
            case DEPENDENCY:
                return "..>";
            case OWNERSHIP:
                return "+--";
        }
        return "--";
    }
}
