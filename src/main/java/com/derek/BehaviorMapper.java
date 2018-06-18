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
            //might be null, but mapInteractionRole will take care of that.
            pair.setRight(mapInteractionRole(pair.getLeft()));
        }
        printPresenceMap();
    }

    private InteractionRole mapInteractionRole(CallTreeNode callTreeNode){
        callTreeNode.printTree();
        for (InteractionRole interactionRole : ips.getInteractions()){
            switch(interactionRole.getRoleType()){
                case STANDARD:
                    for (RBMLMapping structureMapping : structureMappings){
                        if (structureMapping.getRole().equals(interactionRole.getOperationRole())){
                            //get the UMLClassifier representation of this interaction role.. because only structure
                            //has been mapped to this point.
                            UMLOperation mappedOperation = (UMLOperation)structureMapping.getUmlArtifact();
                            String callTreeNameStripped = stripCallTreeName((String)callTreeNode.getName());
                            //shouldn't this be the return type? instead of the mapped op?
                            if (callTreeNameStripped.equals(mappedOperation.getName())){
                                return interactionRole;
                            }
                        }
                    }
                    break;
                case CONTROL_STRUCTURE:
                    if (callTreeNode.getTagName().equals(interactionRole.getName())){
                        return interactionRole;
                    }
            }

        }
        return  null;
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
    }
}
