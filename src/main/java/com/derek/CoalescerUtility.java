package com.derek;

import com.derek.uml.CallTreeNode;
import com.derek.uml.UMLAttribute;
import com.derek.uml.UMLClassifier;

import java.util.ArrayList;
import java.util.List;

public class CoalescerUtility {

    /***
     * this method finds and returns all umlattributes that connect the param owningClassifier to the second role
     *
     * @param owningClassifier
     * @param secondRole
     * @return
     */
    public static List<UMLAttribute> getInterPatternAttributes(UMLClassifier owningClassifier, UMLClassifier secondRole){
        List<UMLAttribute> attributes = new ArrayList<>();
        for (UMLAttribute umlAttribute : owningClassifier.getAttributes()){
            if (umlAttribute.getType().equals(secondRole)){
                attributes.add(umlAttribute);
            }
        }
        return attributes;
    }

    /***
     * this method iterates over the call tree node list and identifies variable decls that have the same name as the secondRole param, uml classifier.
     * @param callTreeNodeList
     * @param secondRole
     * @return
     */
    public static List<String> getInterMethodPatternAttriubtes(List<CallTreeNode<String>> callTreeNodeList, UMLClassifier secondRole){
        List<String> types = new ArrayList<>();
        for (CallTreeNode<String> node : callTreeNodeList){
            if (node.getTagName().contains("decl{")){
                String declType = node.parseDeclTagName();
                if (declType.equals(secondRole.getName())){
                    types.add(node.getName());
                }
            }
        }
        return types;
    }
}
