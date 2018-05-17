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

import javafx.util.Pair;
import lombok.Getter;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Getter
public class OperationRole extends Role {

    protected String type;

    //while not explicitly required, at most we will have one param name and param type.
    private String paramType;

    public OperationRole(String lineDescription) {
        super(lineDescription);
    }

    @Override
    protected void parseLineDescription(String lineDescription) {
        //parameters not implemented yet. soon though.
        Pattern p = Pattern.compile("(\\|[a-zA-Z]+)(\\([\\|[a-zA-Z]+,]*\\*{0,1}\\)):(\\|[a-zA-Z]+|void|\\*),(\\d\\.\\.[\\d|\\*])");
        Matcher m = p.matcher(lineDescription);
        m.find();
        if (m.matches()){
            name = m.group(1);
            //type might be void, which is the big difference from attributes.
            //group 2 is parameters... not implemented yet... and i might not need it.
            type = m.group(3);
            multiplicity = findMultiplicity(m.group(4));
        }
    }

    @Override
    protected void printSummary() {
        System.out.println("Operation");
        System.out.println("Name: " + name);
        System.out.println("Type: " + type);
        System.out.println("Multiplicities: " + multiplicity.getKey() + ".." + multiplicity.getValue());
    }

}
