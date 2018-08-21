package com.derek;

import lombok.Getter;

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

@Getter
public class BehaviorMapper {

    private List<MutablePair<CallTreeNode, InteractionRole>> presenceMap;
    //role map is basically the same as the presence map, but with all non-pattern behaviors removed. Its a collapsed presence map.
    private List<Pair<CallTreeNode, InteractionRole>> roleMap;
    private IPS ips;
    private List<CallTreeNode<String>> callTreeAsList;
    private List<RBMLMapping> structureMappings;
    private List<RBMLMapping> varMappings;

    private InteractionRole functionHeaderMapping;

    //want something like this to track behavioral violations.
    private List<BehavioralConformance> behavioralViolations;

    //tracks behavioral satisfactions
    private List<BehavioralConformance> behavioralSatisfactions;

    public BehaviorMapper(IPS ips, List<CallTreeNode<String>> callTreeAsList, List<RBMLMapping> structureMappings){
        this.ips = ips;
        this.callTreeAsList = callTreeAsList;
        this.structureMappings = structureMappings;
        this.varMappings = new ArrayList<>();
        behavioralViolations = new ArrayList<>();
        behavioralSatisfactions = new ArrayList<>();
        buildStructure();
        pass1();
        collapsePresenceMap();
        pass2();
        pass3();
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
    }

    /***
     * pass 2 is concerned with identifying order of calls.
     */
    private void pass2(){
        //mapped iterator tracks the
        int previousPointer = 0;
        for (int i = 0; i < roleMap.size(); i++){
            for (int j = 0; j < ips.getInteractions().size(); j++){
                if (roleMap.get(i).getRight().equals(ips.getInteractions().get(j))){
                    //found a match between mapped role and interaction role.
                    if (previousPointer > j){
                        //order violated
                        behavioralViolations.add(new BehavioralConformance(roleMap.get(i).getLeft(), roleMap.get(i).getRight(), BehavioralGrimeType.IMPROPER_ORDER));
                    }else {
                        behavioralSatisfactions.add(new BehavioralConformance(roleMap.get(i).getLeft(), roleMap.get(i).getRight()));
                        previousPointer = j;
                    }
                }
            }
        }
    }

    /***
     * pass 3 is concerned with repetition of calls. Here I just need to look for multiple mappings within 1 behavioral scope.
     */
    private void pass3(){
        List<InteractionRole> seenInteractionRoles = new ArrayList<>();
        for (int i = 0; i < roleMap.size(); i++){
            for (int j = 0; j < ips.getInteractions().size(); j++){
                if (roleMap.get(i).getRight().equals(ips.getInteractions().get(j))){
                    //found a match between mapped role and interaction role.
                    if (seenInteractionRoles.contains(roleMap.get(i).getRight())){
                        //already seen this
                        behavioralViolations.add(new BehavioralConformance(roleMap.get(i).getLeft(), roleMap.get(i).getRight(), BehavioralGrimeType.INTRA_REPETITION));
                    }else {
                        behavioralSatisfactions.add(new BehavioralConformance(roleMap.get(i).getLeft(), roleMap.get(i).getRight()));
                        seenInteractionRoles.add(roleMap.get(i).getRight());
                    }
                }
            }
        }
    }

    private InteractionRole mapInteractionRole(CallTreeNode callTreeNode){
        for (InteractionRole interactionRole : ips.getInteractions()){
            switch(interactionRole.getRoleType()){
                case STANDARD:
                    //I need to see if the call tree has a declaration.
                    //two options - call as callTreeTag or decl
                    if (callTreeNode.getTagName().equals("call") || callTreeNode.getTagName().equals("function")) {
                        for (RBMLMapping structureMapping : structureMappings) {
                            if (structureMapping.getRole().equals(interactionRole.getOperationRole())) {
                                //get the UMLClassifier representation of this interaction role.. because only structure
                                //has been mapped to this point.
                                UMLOperation mappedOperation = (UMLOperation) structureMapping.getUmlArtifact();
                                String callTreeNameStripped = stripCallTreeName((String) callTreeNode.getName());
                                if (callTreeNameStripped.equals(mappedOperation.getName())) {
                                    if (callTreeNode.getTagName().equals("function")){
                                        //this particular call tree node is the start of the call tree list, so set header
                                        //really this information is stored elsewhere, but putting it in this variable helps
                                        functionHeaderMapping = interactionRole;
                                    }
                                    return interactionRole;
                                }
                            }
                        }
                    } else if (callTreeNode.getTagName().contains("decl{")){
                        //call tree node has a decl
                        String declType = callTreeNode.parseDeclTagName();
                        for (RBMLMapping structureMapping : structureMappings){
                            UMLClassifier toMatch = structureMapping.getUMLClassifierArtifact();
                            if (toMatch != null) {
                                if (toMatch.getName().equals(declType)) {
                                    //found a match, from structural umlClassifier to call tree node decl.
                                    //note this is just the call tree node decl, not any usages of it. I need to track the var name from teh
                                    //call tree node to see if it is used again, and then match on calls coming from the var.
                                    //e.g.: callTreeNode: <obs decl{WeatherObservr}>; next callTreeNode: <obs.update call>;
                                    RBMLMapping varMapping = new RBMLMapping(structureMapping.getRole(), declType);
                                    varMappings.add(varMapping);
                                    //technically varMapping things are structural mappings, becuase they are variable declaration mappings.
                                    //however, I ultimately need to check their calls, which would be behavioral mappings
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

    private String stripCallTreeName(String originalName){
        if (originalName.contains(".")){
            String[] splitter = originalName.split("\\.");
            return splitter[splitter.length-1];
        }
        return originalName;
    }

    /***
     * method to collapse the presence map into the role map. This is really just an efficiency gain, to remove non-role calls.
     */
    private void collapsePresenceMap(){
        roleMap = new ArrayList<>();
        for (MutablePair<CallTreeNode, InteractionRole> presence : presenceMap){
            if (presence.getRight() != null){
                //mapping
                roleMap.add(presence);
            }
        }
    }

    public void printPresenceMap(List<Pair<CallTreeNode, InteractionRole>> map){
        System.out.print("| ");
        for (Pair<CallTreeNode, InteractionRole> pair : map){
            System.out.print(pair.getLeft().getName() + " | ");
        }
        System.out.println();
        System.out.print("| ");
        for (Pair<CallTreeNode, InteractionRole> pair : map){
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
