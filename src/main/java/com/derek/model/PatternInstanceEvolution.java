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
package com.derek.model;

import com.derek.model.patterns.PatternInstance;
import javafx.util.Pair;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class PatternInstanceEvolution {

    private SoftwareVersion startingPoint;
    private SoftwareVersion endingPoint;

    private PatternType patternType;
    //index in the list points at pattern software version
    private List<Pair<SoftwareVersion, PatternInstance>> patternLifetime;

    public PatternInstanceEvolution(SoftwareVersion startingPoint, SoftwareVersion endingPoint, PatternType patternType, List<Pair<SoftwareVersion, PatternInstance>> patternLifetime) {
        this.startingPoint = startingPoint;
        this.endingPoint = endingPoint;
        this.patternType = patternType;
        this.patternLifetime = patternLifetime;
    }

    public PatternInstanceEvolution(List<Pair<SoftwareVersion, PatternInstance>> patternLifetime){
        this.patternLifetime = patternLifetime;
    }

    public PatternInstanceEvolution(SoftwareVersion startingPoint, PatternType patternType, PatternInstance patternInstance){
        this.startingPoint = startingPoint;

        this.patternType = patternType;
        patternLifetime = new ArrayList<>();
        for (int i = 0; i < startingPoint.getVersionNum(); i++){
            //this signifies that a pattern did not exist at a particular time.
            //
            patternLifetime.add(new Pair<>(new SoftwareVersion(i), null));
        }
        patternLifetime.add(new Pair<>(startingPoint, patternInstance));
    }



    public void addPatternInstanceToEvolution(PatternInstance pi, SoftwareVersion v){
        //add pattern instance to a particular version number.
        patternLifetime.add(new Pair<>(v, pi));
    }

    public PatternInstance getFirstPatternInstance(){
        return patternLifetime.get(startingPoint.getVersionNum()).getValue();
    }

    public List<Pair<SoftwareVersion, PatternInstance>> getPatternLifetime() {
        return patternLifetime;
    }


    public String getCSVVersions(){
        String toRet = "";
        for (Pair<SoftwareVersion, PatternInstance> pairs : patternLifetime){
            if (pairs.getValue() != null){
                toRet += pairs.getKey().getVersionNum() + ", ";
            }
        }
        return toRet;
    }
}
