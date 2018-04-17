package com.derek.rbml;

import com.derek.uml.Relationship;
import javafx.util.Pair;
import lombok.Getter;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Getter
public class RelationshipRole extends Role {

    private Relationship type;
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
        Pattern p = Pattern.compile("([a-zA-Z]+)\\s(\\|[a-zA-Z]+)(.*)");
        Matcher m = p.matcher(lineDescription);
        m.find();
        String tempType = m.group(1);
        type = getTypeFromString(tempType);
        name = m.group(2);
        parseRelationshipRoleNames(m.group(3));
    }

    private Relationship getTypeFromString(String tempType){
        switch (tempType){
            case "Association":
                return Relationship.ASSOCIATION;
            case "Generalization":
                return Relationship.GENERALIZATION;
            case "Dependency":
                return Relationship.DEPENDENCY;
            default:
                System.out.println("did not find a type for the relationship descriptor. Likely an issue with the rbml text.");
        }
        System.exit(0);
        return Relationship.UNSPECIFIED;
    }

    private void parseRelationshipRoleNames(String roleNames){
        Pattern p = Pattern.compile("(\\{.*\\})*");
        Matcher m = p.matcher(roleNames);
        m.find();
        if (m.groupCount() == 1){
            //single directional relationship
            connection1 = parseConnection(m.group(1));
            if (!this.type.equals(Relationship.GENERALIZATION)){
                //no multiplicities on generalization.
                connection1Multiplicities = parseConnectionMultiplicities(m.group(1));
            }
        }else{
            //bidirectional
            connection2 = parseConnection(m.group(2));
            connection2Multiplicities = parseConnectionMultiplicities(m.group(2));
        }
    }

    private Pair<String, String> parseConnection(String s){
        s = s.replace("{","");
        s = s.replace("}","");
        String[] splitter = s.split(",");
        return new Pair<>(splitter[0], splitter[1]);
    }

    private Pair<Integer, Integer> parseConnectionMultiplicities(String s){
        String[] splitter = s.split(",");
        return findMultiplicity(splitter[2]);
    }

    @Override
    protected void printSummary() {
        System.out.println("Relationship");
        System.out.println("Name: " + name);
        System.out.println("Type: " + type);
        System.out.println("connection 1: " + connection1.getKey() + " to " + connection1.getValue());
        if (connection1Multiplicities != null) {
            //generalizations have no mults.
            System.out.println("connection 1 multiplicities: " + connection1Multiplicities.getKey() + " to " + connection1Multiplicities.getValue());
        }
        if (connection2 != null){
            System.out.println("connection 2: " + connection2.getKey() + " to " + connection2.getValue());
            if (connection2Multiplicities != null){
                System.out.println("connection 2 multiplicities: " + connection2Multiplicities.getKey() + " to " + connection2Multiplicities.getValue());
            }
        }
    }

}
