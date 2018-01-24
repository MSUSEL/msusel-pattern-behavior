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

import com.google.common.graph.MutableGraph;
import lombok.Getter;

import java.util.List;
import java.util.Map;

@Getter
public class UMLMessage {

    private MutableGraph<String> callForest;
    //has been processed refers to the two step transformation from identification to actually buliding out the message.
    private boolean hasBeenProcessed;
    private UMLClassifier from;
    private UMLClassifier to;
    private boolean isRequest;

    public UMLMessage(UMLClassifier from, UMLClassifier to, boolean isRequest) {
        this.from = from;
        this.to = to;
        this.isRequest = isRequest;
        hasBeenProcessed = true;
    }
    public UMLMessage(MutableGraph<String> callForest){
        this.callForest = callForest;
        this.hasBeenProcessed = false;
    }

    public void process(){
        //method to be used during the second step transformation from string-> split up message with uml classifiers identified.

        hasBeenProcessed = true;
    }
}