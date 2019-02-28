package com.derek;

import com.derek.rbml.IPS;
import com.derek.rbml.InteractionRole;
import com.derek.rbml.RBMLMapping;
import com.derek.rbml.SPS;
import com.derek.uml.CallTreeNode;
import com.derek.uml.UMLClassifier;
import com.derek.uml.UMLOperation;
import lombok.Getter;
import org.apache.commons.lang3.tuple.MutablePair;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.List;

@Getter
public class BehaviorMappingUtils {
    private static SPS sps;
    private static IPS ips;
    private static List<RBMLMapping> structureMappings;

    public BehaviorMappingUtils(SPS sps, List<RBMLMapping> structureMappings, IPS ips){
        this.sps = sps;
        this.ips = ips;
        this.structureMappings = structureMappings;
    }

    /***
     * send in a uml operation, get a role map from the entire ips.
     *
     * @param umlOperation
     * @return
     */
    public static List<MutablePair<CallTreeNode, InteractionRole>> getRoleMap(UMLOperation umlOperation){
        //build presence map
        List<MutablePair<CallTreeNode, InteractionRole>> roleMap;
        roleMap = new ArrayList<>();
        List<CallTreeNode<String>> callTreeAsList = umlOperation.getCallTreeString().convertMeToOrderedList();
        for (CallTreeNode<String> callTreeNode : callTreeAsList){
            roleMap.add(new MutablePair<>(callTreeNode, null));
        }
        for (MutablePair<CallTreeNode, InteractionRole> pair : roleMap){
            //might be null, but mapInteractionRole will take care of that.
            pair.setRight(mapInteractionRole(pair.getLeft()));
        }
        return roleMap;

    }

    public static InteractionRole mapInteractionRole(CallTreeNode<String> callTreeNode){
        for (InteractionRole interactionRole : ips.getInteractions()){
            switch(interactionRole.getRoleType()){
                case STANDARD:
                    //I need to see if the call tree has a declaration.
                    //two options - call as callTreeTag or decl
                    if (callTreeNode.isCall() || callTreeNode.isFunction()) {
                        for (RBMLMapping structureMapping : structureMappings) {
                            if (structureMapping.getRole().equals(interactionRole.getOperationRole())) {
                                //get the UMLClassifier representation of this interaction role.. because only structure
                                //has been mapped to this point.
                                UMLOperation mappedOperation = (UMLOperation) structureMapping.getUmlArtifact();
                                String callTreeNameStripped = stripCallTreeName(callTreeNode.getName());
                                if (callTreeNameStripped.equals(mappedOperation.getName())) {
                                    return interactionRole;
                                }
                            }
                        }
                    }
                    break;
                case CONTROL_STRUCTURE:
                    if (callTreeNode.getTagName().equals(interactionRole.getName())){
                        return interactionRole;
                    }
                    break;
            }
        }
        return null;
    }

    private static String stripCallTreeName(String originalName){
        if (originalName.contains(".")){
            String[] splitter = originalName.split("\\.");
            return splitter[splitter.length-1];
        }
        return originalName;
    }

    public void printRoleMap(List<MutablePair<CallTreeNode, InteractionRole>> roleMap){
        System.out.print("| ");
        for (Pair<CallTreeNode, InteractionRole> pair : roleMap){
            int padSize = getPadSize(pair);
            String pad = buildPad(padSize - pair.getLeft().getName().toString().length());
            System.out.print(pair.getLeft().getName() + pad + " | ");
        }
        System.out.println();
        System.out.print("| ");
        for (Pair<CallTreeNode, InteractionRole> pair : roleMap){
            int padSize = getPadSize(pair);
            if (pair.getRight() == null){
                //null if not mapped.
                String pad = buildPad(padSize - 15);
                System.out.print("no role mapping" + pad + " | ");
            }else {
                String pad = buildPad(padSize - pair.getRight().getName().length());
                System.out.print(pair.getRight().getName() + pad + " | ");
            }
        }
        System.out.println("\n");
    }

    private int getPadSize(Pair<CallTreeNode, InteractionRole> pair){
        int leftSize = pair.getLeft().getName().toString().length();
        int rightSize = 0;
        if (pair.getRight() == null){
            rightSize = 15;
        }else {
            rightSize = pair.getRight().getName().length();
        }
        int max = Math.max(leftSize, rightSize);

        return max;
    }
    private String buildPad(int size){
        String pad = "";
        for (int i = 0; i < size; i++){
            pad += " ";
        }
        return pad;
    }
}
