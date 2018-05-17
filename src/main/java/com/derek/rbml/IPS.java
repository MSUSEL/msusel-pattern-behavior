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
