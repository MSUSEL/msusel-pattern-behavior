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
    private IPS ips;
    private List<CallTreeNode<String>> callTreeAsList;
    private List<RBMLMapping> structureMappings;
    private List<RBMLMapping> varMappings;

    public BehaviorMapper(IPS ips, List<CallTreeNode<String>> callTreeAsList, List<RBMLMapping> structureMappings){
        this.ips = ips;
        this.callTreeAsList = callTreeAsList;
        this.structureMappings = structureMappings;
        this.varMappings = new ArrayList<>();
        buildStructure();
        pass1();
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
        printPresenceMap();
    }

    /***
     * pass 2 is concerned with identifying order of calls.
     */
    private void pass2(){


    }

    /***
     * pass 3 is concerned with repetition of calls. Here I just need to look for multiple mappings within 1 behavioral scope.
     */
    private void pass3(){

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
                    //this code below does nothing. (no declarations in ips)
                case DECLARATION:
                    //points at a type, which may or may not be part of the pattern
                    for (RBMLMapping structuralMapping : structureMappings){
                        if (structuralMapping.getUmlArtifact() instanceof UMLClassifier){
                            UMLClassifier mappedArtifact = (UMLClassifier) structuralMapping.getUmlArtifact();
                            if (callTreeNode.getName().equals(mappedArtifact.getName())) {
                                System.out.println("found a match here for call tree nodes.");
                                System.out.println(callTreeNode.getName() + "    " + mappedArtifact.getName());

                            }
                        }
                    }
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
