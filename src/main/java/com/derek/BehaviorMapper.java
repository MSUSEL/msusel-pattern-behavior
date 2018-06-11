package com.derek;

import com.derek.rbml.IPS;
import com.derek.rbml.InteractionRole;
import com.derek.rbml.RBMLMapping;
import com.derek.uml.CallTreeNode;
import com.derek.uml.UMLClassifier;
import com.derek.uml.UMLOperation;
import org.apache.commons.lang3.tuple.MutablePair;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.List;

public class BehaviorMapper {

    private List<Pair<CallTreeNode, Boolean>> presenceMap;
    private IPS ips;
    private List<CallTreeNode<String>> callTreeAsList;
    private List<RBMLMapping> structureMappings;

    public BehaviorMapper(IPS ips, List<CallTreeNode<String>> callTreeAsList, List<RBMLMapping> structureMappings){
        this.ips = ips;
        this.callTreeAsList = callTreeAsList;
        this.structureMappings = structureMappings;
        buildStructure();
        pass1();
        printPresenceMap();

    }


    private void buildStructure(){
        presenceMap = new ArrayList<>();
        for (CallTreeNode callTreeNode : callTreeAsList){
            presenceMap.add(new MutablePair<>(callTreeNode, false));
        }
    }

    /***
     * assign presence/non-presence (not absence because this is presence in the IPS
     */
    private void pass1(){
        for (Pair<CallTreeNode, Boolean> pair : presenceMap){
            if (doesMapToRBML(pair.getKey())){
                pair.setValue(true);
            }
        }
    }

    private boolean doesMapToRBML(CallTreeNode callTreeNode){
        boolean mapExists = false;
        for (InteractionRole interactionRole : ips.getInteractions()){
            for (RBMLMapping structureMapping : structureMappings){
                if (structureMapping.getRole().equals(interactionRole.getOperationRole())){
                    UMLOperation mappedOperation = (UMLOperation)structureMapping.getUmlArtifact();
                    UMLClassifier opReturnType = mappedOperation.getType();
                    if (opReturnType != null){
                        //would be null if 'void', or if we couldn't match the umlclassifier type
                        String callTreeNameStripped = stripCallTreeName((String)callTreeNode.getName());
                        if (callTreeNameStripped.equals(mappedOperation.getName())){//opReturnType)){
                            //think i found a match.. not sure tho.
                            mapExists = true;
//                            System.out.println("found a match!!!! ");
//                            System.out.println("UML " + mappedOperation.getName() + " and returning type: " + mappedOperation.getType().getName());
//                            System.out.println("Matched to: " + callTreeNode.getName());
//                            System.out.println();
//                            callTreeNode.printTree();
                        }
                    }

//                    System.out.println("Interaction role: " + interactionRole.getRoleType() + interactionRole.getOperationRole().getName());
//                    System.out.println("UML op from interaction role: " + ((UMLOperation)structureMapping.getUmlArtifact()).getName());
//                    System.out.println("UML type from op " + ((UMLOperation)structureMapping.getUmlArtifact()).getStringReturnDataType());
//                    System.out.println(callTreeNode.getName() + " " + callTreeNode.getTagName());
//                    System.out.println();
//                    callTreeNode.printTree();
//                    System.out.println("\n\n");
                }
            }
        }

        return mapExists;
    }

    private String stripCallTreeName(String originalName){
        if (originalName.contains(".")){
            String[] splitter = originalName.split("\\.");
            return splitter[splitter.length-1];
        }
        return originalName;
    }


    public void printPresenceMap(){
        System.out.print("| ");
        for (Pair<CallTreeNode, Boolean> pair : presenceMap){
            System.out.print(pair.getKey().getName() + " | ");
        }
        System.out.println();
        System.out.print("| ");
        for (Pair<CallTreeNode, Boolean> pair : presenceMap){
            System.out.print(pair.getValue() + " | ");
        }
        System.out.println("\n");
    }
}
