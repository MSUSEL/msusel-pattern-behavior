package com.derek.rbml;

import com.derek.uml.Relationship;
import javafx.util.Pair;
import lombok.Getter;

import java.sql.Struct;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Getter
public class RelationshipRole extends Role {

    private Relationship type;
    private Pair<String, String> connection1String;
    private Pair<StructuralRole, StructuralRole> connection1;
    private Pair<Integer, Integer> connection1Multiplicities;
    //connection 2 is optional
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
        Pattern p = Pattern.compile("\\{(\\|[a-zA-Z]+),(\\|[a-zA-Z]+)([,\\d\\.\\.[\\d|\\*]]*)\\}");
        Matcher m = p.matcher(roleNames);
        m.find();
        connection1String = new Pair<>(m.group(1), m.group(2));
        //one single directional multiplicity
        String[] splitter = m.group(3).split(",");
        if (splitter.length > 1) {
            connection1Multiplicities = findMultiplicity(splitter[1]);
            if (splitter.length == 3) {
                connection2Multiplicities = findMultiplicity(splitter[2]);
            }
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

    protected void buildConnectionStructure(List<StructuralRole> structuralRoles){
        StructuralRole nodeU = matchStructuralRole(structuralRoles, connection1String.getKey());
        StructuralRole nodeV = matchStructuralRole(structuralRoles, connection1String.getValue());
        connection1 = new Pair<>(nodeU, nodeV);
    }

    private StructuralRole matchStructuralRole(List<StructuralRole> structuralRoles, String nameToMatch){
        for (StructuralRole structuralRole : structuralRoles){
            if (nameToMatch.equals(structuralRole.getName())){
                return structuralRole;
            }
        }
        System.out.println("Failed to match a structural role.  This is likely because of a bad text format.");
        return null;
    }

    @Override
    protected void printSummary() {
        System.out.println("Relationship");
        System.out.println("Name: " + name);
        System.out.println("Type: " + type);
        System.out.println("connection 1: " + connection1String.getKey() + " to " + connection1String.getValue());
        if (connection1Multiplicities != null) {
            //generalizations have no mults.
            System.out.println("connection 1 multiplicities: " + connection1Multiplicities.getKey() + " to " + connection1Multiplicities.getValue());
            System.out.println("connection 1 reverse multiplicities: " + connection2Multiplicities.getKey() + " to " + connection2Multiplicities.getValue());
        }

    }

}
