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

import com.derek.uml.Relationship;
import lombok.Getter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

@Getter
public class SPS {

    //classifier roles are declared abstractly (as a classifier) but the actual role will be either a UMLInterface or UMLClass
    //I can use instanceof to further distinguish this, but realistically I shouldn't need to type check.
    private List<StructuralRole> classifierRoles;
    private List<RelationshipRole> associationRoles;
    private List<RelationshipRole> generalizationRoles;
    private List<RelationshipRole> dependencyRoles;
    private List<RelationshipRole> implementationRoles;
    private List<RelationshipRole> implementationOrGeneralizationRoles;

    public SPS(String descriptorFileName) {
        classifierRoles = new ArrayList<>();
        associationRoles = new ArrayList<>();
        generalizationRoles = new ArrayList<>();
        dependencyRoles = new ArrayList<>();
        implementationRoles = new ArrayList<>();
        implementationOrGeneralizationRoles = new ArrayList<>();
        parseRoles(descriptorFileName);
        buildRelationships();
    }

    /***
     * meant as a 'second pass', where the structural roles are matched up to each behavioral role.
     */
    private void buildRelationships(){
        for (RelationshipRole association : associationRoles){
            association.buildConnectionStructure(classifierRoles);
        }
        for (RelationshipRole generalization : generalizationRoles){
            generalization.buildConnectionStructure(classifierRoles);
        }
        for (RelationshipRole dependency : dependencyRoles){
            dependency.buildConnectionStructure(classifierRoles);
        }
        for (RelationshipRole implementation : implementationRoles){
            implementation.buildConnectionStructure(classifierRoles);
        }
        for (RelationshipRole implementationOrGeneralization : implementationOrGeneralizationRoles){
            implementationOrGeneralization.buildConnectionStructure(classifierRoles);
        }
    }

    private void parseRoles(String descriptorFileName){
        try{
            Scanner in = new Scanner(new File(descriptorFileName));
            while (in.hasNext()){
                String line = in.nextLine();
                parseRoleType(line);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void parseRoleType(String lineDescription){
        String type = lineDescription.split("\\ ")[0];
        switch (type){
            //type info
            case "Classifier":
            case "Class":
                classifierRoles.add(new StructuralRole(lineDescription));
                break;
            case "Association":
                associationRoles.add(new RelationshipRole(lineDescription));
                break;
            case "Generalization":
                generalizationRoles.add(new RelationshipRole(lineDescription));
                break;
            case "Dependency":
                dependencyRoles.add(new RelationshipRole(lineDescription));
                break;
            case "Realization":
                implementationRoles.add(new RelationshipRole(lineDescription));
                break;
            case "Generalization||Realization":
                implementationOrGeneralizationRoles.add(new RelationshipRole(lineDescription));
                break;

            default:
                System.out.println("did not fine an rbml role for the input. Debug parseRoleType. But likely the input was messed up. (sps text file)");
                System.exit(0);
        }
    }

    public void printRoles(){
        for (StructuralRole structuralRole : classifierRoles){
            structuralRole.printSummary();
        }
        for (RelationshipRole relationshipRole : associationRoles){
            relationshipRole.printSummary();
        }
        for (RelationshipRole relationshipRole : generalizationRoles){
            relationshipRole.printSummary();
        }
        for (RelationshipRole relationshipRole : dependencyRoles){
            relationshipRole.printSummary();
        }
        for (RelationshipRole relationshipRole : implementationRoles){
            relationshipRole.printSummary();
        }
        for (RelationshipRole relationshipRole : implementationOrGeneralizationRoles){
            relationshipRole.printSummary();
        }
    }

    public List<Role> getAllRoles(){
        List<Role> roles = new ArrayList<>();
        roles.addAll(classifierRoles);
        for (StructuralRole classifierRole : classifierRoles){
            roles.addAll(classifierRole.getAttributes());
            roles.addAll(classifierRole.getOperations());
        }
        roles.addAll(associationRoles);
        roles.addAll(generalizationRoles);
        roles.addAll(dependencyRoles);
        roles.addAll(implementationRoles);
        roles.addAll(implementationOrGeneralizationRoles);
        return roles;
    }

    public List<Role> getAllRolesWithMultiplicities(){
        List<Role> roles = new ArrayList<>();
        roles.addAll(classifierRoles);
        for (StructuralRole classifierRole : classifierRoles){
            roles.addAll(classifierRole.getAttributes());
            roles.addAll(classifierRole.getOperations());
        }
        roles.addAll(associationRoles);
        roles.addAll(dependencyRoles);
        return roles;
    }

    public List<OperationRole> getAllOperationRoles(){
        List<OperationRole> operationRoles = new ArrayList<>();
        for (StructuralRole structuralRole : classifierRoles){
            for (OperationRole operationRole : structuralRole.getOperations()){
                operationRoles.add(operationRole);
            }
            //operationRoles.addAll(structuralRole.getOperations());
        }
        return operationRoles;
    }


}
