package com.derek.rbml;

import lombok.Getter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

@Getter
public class IPS {

    private String name;
    //sequence list needs to be ordered.
    private List<InteractionRole> interactions;

    public IPS(String descriptorFileName, SPS sps) {
        interactions = new ArrayList<>();
        parseRoles(descriptorFileName);

        matchSPSObjects(sps);
    }

    private void matchSPSObjects(SPS sps){
        for (InteractionRole sequenceRole : interactions){
            for (StructuralRole structuralRole : sps.getClassifierRoles()){
                if (sequenceRole.getStructuralRoleString().equals(structuralRole.getName())){
                    sequenceRole.setStructuralRole(structuralRole);
                    //find operation name now
                    for (OperationRole operationRole : structuralRole.getOperations()){
                        if (sequenceRole.getOperationRoleString().equals(operationRole.getName())){
                            //ffound mathcing operation name
                            sequenceRole.setOperationRole(operationRole);
                        }
                    }
                }
            }
        }
        //done matching, every thing should have a matched role now. if not here is loop that iwll catch that.
        for (InteractionRole sequenceRole : interactions){
            if (sequenceRole.getStructuralRole() == null){
                System.out.println("null structural role for sequence " + sequenceRole.getStructuralRoleString());
                System.out.println("consider debugging the textual representation of the rbml.");
                System.exit(0);
            }
            if (sequenceRole.getOperationRole() == null){
                System.out.println("null operation role for sequence " + sequenceRole.getOperationRoleString());
                System.out.println("consider debugging the textual representation of the rbml.");
                System.exit(0);
            }
        }
    }

    private void parseRoles(String descriptorFileName){
        try{
            Scanner in = new Scanner(new File(descriptorFileName));
            String name = in.nextLine();
            this.name = name;
            while (in.hasNext()){
                String line = in.nextLine();
                interactions.add(new InteractionRole(line));
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }


}
