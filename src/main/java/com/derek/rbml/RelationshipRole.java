package com.derek.rbml;

import com.derek.uml.Relationship;
import javafx.util.Pair;
import lombok.Getter;

import java.util.regex.Pattern;

@Getter
public class RelationshipRole extends Role {

    private Relationship type;
    private String name;
    private Pair<String, String> connection1;
    private Pair<Integer, Integer> connection1Multiplicities;
    //connection 2 is optional
    private Pair<String, String> connection2;
    private Pair<Integer, Integer> connection2Multiplicities;

    public RelationshipRole(String lineDescription){
        super(lineDescription);
    }

    /***
     * input will look like:
     * Association |ClntRecvr{|Client,|Receiver,1..*}{|Receiver,|Client,1..*}
     * Generalization |CommandAbstraction{|AbstractCommand,|Command}
     * Dependency |CreateCommand{|Client,|ConcereteCommand,1..*}
     *
     * @param lineDescription see above input comment.
     */
    @Override
    protected void parseLineDescription(String lineDescription){
        //https://stackoverflow.com/questions/237061/using-regular-expressions-to-extract-a-value-in-java
        Pattern p = Pattern.compile("[A-Za-z]+[ ]|[A-Za-z]+,\\d\\.\\.[\\d|\\*]");

    }

    @Override
    protected void printSummary() {
        System.out.println("Relationship");
        System.out.println("Name: " + name);
        System.out.println("Type: " + type);
        //TODO
    }

}
