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

import com.derek.uml.RelationshipType;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import lombok.Getter;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Getter
public class RelationshipRole extends Role {

    private RelationshipType type;
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

    private RelationshipType getTypeFromString(String tempType){
        switch (tempType){
            case "Association":
                return RelationshipType.ASSOCIATION;
            case "Generalization":
                return RelationshipType.GENERALIZATION;
            case "Dependency":
                return RelationshipType.DEPENDENCY;
            case "Realization":
                return RelationshipType.REALIZATION;
            case "Generalization||Realization":
                return RelationshipType.UNSPECIFIED;
            default:
                System.out.println("did not find a type for the relationship descriptor. Likely an issue with the rbml text.");
        }
        System.exit(0);
        return RelationshipType.UNSPECIFIED;
    }

    private void parseRelationshipRoleNames(String roleNames){
        Pattern p = Pattern.compile("\\{(\\|[a-zA-Z]+),(\\|[a-zA-Z]+)([,\\d\\.\\.[\\d|\\*]]*)\\}");
        Matcher m = p.matcher(roleNames);
        m.find();
        connection1String = new ImmutablePair<>(m.group(1), m.group(2));
        //one single directional multiplicity
        String[] splitter = m.group(3).split(",");
        if (splitter.length > 1) {
            connection1Multiplicities = findMultiplicity(splitter[1]);
            multiplicity = connection1Multiplicities;
            if (splitter.length == 3) {
                connection2Multiplicities = findMultiplicity(splitter[2]);
            }
        }
    }

    protected void buildConnectionStructure(List<StructuralRole> structuralRoles){
        StructuralRole nodeU = matchStructuralRole(structuralRoles, connection1String.getKey());
        StructuralRole nodeV = matchStructuralRole(structuralRoles, connection1String.getValue());
        connection1 = new ImmutablePair<>(nodeU, nodeV);
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
