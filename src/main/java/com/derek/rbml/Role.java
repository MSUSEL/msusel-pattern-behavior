package com.derek.rbml;

import javafx.util.Pair;
import lombok.Getter;

@Getter
public abstract class Role {
    protected String name;

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
        return new Pair<>(minimum, maximum);
    }

    protected abstract void printSummary();

}
