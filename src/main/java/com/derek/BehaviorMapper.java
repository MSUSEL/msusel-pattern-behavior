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

    private List<MutablePair<CallTreeNode, InteractionRole>> presenceMap;
    private IPS ips;
    private List<CallTreeNode<String>> callTreeAsList;
    private List<RBMLMapping> structureMappings;

    public BehaviorMapper(IPS ips, List<CallTreeNode<String>> callTreeAsList, List<RBMLMapping> structureMappings){
        this.ips = ips;
        this.callTreeAsList = callTreeAsList;
        this.structureMappings = structureMappings;
        buildStructure();
        pass1();
    }

    private void buildStructure(){
        presenceMap = new ArrayList<>();
        for (CallTreeNode callTreeNode : callTreeAsList){
            presenceMap.add(new MutablePair<>(callTreeNode, null));
        }
    }

    /***
     * assign presence/non-presence (not absence because this is presence in the IPS)
     * also maps calltree node to correct interaction role.
     */
    private void pass1(){
        for (MutablePair<CallTreeNode, InteractionRole> pair : presenceMap){
            mapInteractionRole(pair);
        }
        printPresenceMap();
    }

    private void mapInteractionRole(MutablePair<CallTreeNode, InteractionRole> pair){
        CallTreeNode callTreeNode = pair.getLeft();
        for (InteractionRole interactionRole : ips.getInteractions()){
            switch(interactionRole.getRoleType()){
                case STANDARD:
                    for (RBMLMapping structureMapping : structureMappings){
                        structureMapping.printSummary();
                        if (structureMapping.getRole().equals(interactionRole.getOperationRole())){
                            //get the UMLClassifier representation of this interaction role.. because only structure
                            //has been mapped to this point.
                            UMLOperation mappedOperation = (UMLOperation)structureMapping.getUmlArtifact();
                            UMLClassifier opReturnType = mappedOperation.getType();
                            if (opReturnType != null){
                                //would be null if 'void', or if we couldn't match the umlclassifier type
                                String callTreeNameStripped = stripCallTreeName((String)callTreeNode.getName());
                                //shouldn't this be the return type? instead of the mapped op?
                                if (callTreeNameStripped.equals(mappedOperation.getName())){
                                    //think i found a match.. not sure tho.
                                    System.out.println("found a match!!!! ");
                                    System.out.println("UML " + mappedOperation.getName() + " and returning type: " + mappedOperation.getType().getName());
                                    System.out.println("Matched to: " + callTreeNode.getName());
                                    System.out.println();
                                    callTreeNode.printTree();
                                    pair.setRight(interactionRole);
                                }
                            }
                            System.out.println("Interaction role: " + interactionRole.getRoleType() + interactionRole.getOperationRole().getName());
                            System.out.println("UML op from interaction role: " + ((UMLOperation)structureMapping.getUmlArtifact()).getName());
                            System.out.println("UML type from op " + ((UMLOperation)structureMapping.getUmlArtifact()).getStringReturnDataType());
                            System.out.println(callTreeNode.getName() + " " + callTreeNode.getTagName());
                            System.out.println();
                            callTreeNode.printTree();
                            System.out.println("\n\n");
                        }
                    }
                    break;
                case CONTROL_STRUCTURE:
                    if (callTreeNode.getTagName().equals(interactionRole.getName())){
                        pair.setRight(interactionRole);
                    }
            }

        }

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
        for (Pair<CallTreeNode, InteractionRole> pair : presenceMap){
            System.out.print(pair.getLeft().getName() + " | ");
        }
        System.out.println();
        System.out.print("| ");
        for (Pair<CallTreeNode, InteractionRole> pair : presenceMap){
            if (pair.getRight() == null){
                //null if not mapped.
                System.out.print("no role mapping | ");
            }else {
                System.out.print(pair.getRight().getName() + " | ");
            }
        }
        System.out.println("\n");
        System.exit(0);
    }
}
