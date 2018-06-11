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

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import lombok.Getter;

@Getter
public abstract class Role {
    protected String name;

    protected Pair<Integer, Integer> multiplicity;

    public Role(String lineDescription){
        parseLineDescription(lineDescription);
    }

    protected abstract void parseLineDescription(String lineDescription);

    /***
     * Utility method --
     * s will be of this form: 0..0, or 1..*, but will always be exactly 4 chars.
     * @param s
     */
    protected Pair<Integer, Integer> findMultiplicity(String s){
        Integer minimum = Integer.parseInt(s.substring(0,1));
        Integer maximum = 0;
        String stringMax = s.substring(3,4);
        if (stringMax.equals("*")){
            maximum = Integer.MAX_VALUE;
        }else{
            maximum = Integer.parseInt(stringMax);
        }
        return new ImmutablePair<>(minimum, maximum);
    }

    protected abstract void printSummary();

    public String compareName(){
        return name.replace("|", "");
    }

}
