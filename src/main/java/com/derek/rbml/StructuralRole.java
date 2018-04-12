package com.derek.rbml;

import javafx.util.Pair;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/***
 * class reponsible for holding
 */
@Getter
public class StructuralRole extends Role{
    private String type;
    private String name;
    //how many instances of class x we can have
    private Pair<Integer, Integer> structuralMultiplicity;
    private List<RoleAttribute> attributes;
    private List<RoleOperation> operations;

    public StructuralRole(String lineDescription){
        super(lineDescription);
        parseLineDescription(lineDescription);
    }

    /***
     * input: Classifier |Receiver,1..*[]{|Action():void,1..*}
     * @param lineDescription
     */
    @Override
    protected void parseLineDescription(String lineDescription) {
        //https://stackoverflow.com/questions/237061/using-regular-expressions-to-extract-a-value-in-java
        Pattern p = Pattern.compile("([a-zA-Z]+)\\s(\\|[a-zA-Z]+),(\\d\\.\\.[\\d|\\*])(\\[.*\\])(\\{.*\\})");
        Matcher m = p.matcher(lineDescription);

        if (m.find()) {
            //group counts can include index length of array.
            type = m.group(1);
            name = m.group(2);
            findStructuralMultiplicity(m.group(3));
            buildAttribues(m.group(4));
        }
    }

    /***
     * s will be of this form: 0..0, or 1..*, but will always be exactly 4 chars.
     * @param s
     */
    private void findStructuralMultiplicity(String s){
        Integer minimum = Integer.parseInt(s.substring(0,1));
        Integer maximum = 0;
        String stringMax = s.substring(3,4);
        if (stringMax.equals("*")){
            maximum = Integer.MAX_VALUE;
        }else{
            maximum = Integer.parseInt(stringMax);
        }
        structuralMultiplicity = new Pair<>(minimum, maximum);
    }

    /***
     * input will be [|state:|Receiver,1..1], and potentially [|state:|Receiver,1..1;|2:|CLASSIFIER,1..1]
     * @param s
     */
    private void buildAttribues(String s){
        if (s.equals("[]")){
            //no attributes
            attributes = new ArrayList<>();
        }else{
            Pattern p = Pattern.compile("\\[\\]");
            //Matcher m = p.matcher(s);
        }

    }
}
