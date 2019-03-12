package com.derek;

import com.derek.grime.BehavioralGrimeType;
import com.derek.rbml.InteractionRole;
import com.derek.uml.CallTreeNode;
import com.derek.uml.UMLAttribute;
import lombok.Getter;

@Getter
public class BehavioralMapping {

    private BehavioralGrimeType behavioralGrimeType;
    //declaration is for unnecessary actions
    private UMLAttribute declaration;
    private CallTreeNode perpetrator;
    private InteractionRole interactionRole;

    /***
     * constructor for when a call tree node violates a behavioral role.
     * @param perpetrator
     * @param interactionRole
     * @param behavioralGrimeType
     */
    public BehavioralMapping(CallTreeNode perpetrator, InteractionRole interactionRole, BehavioralGrimeType behavioralGrimeType){
        this.perpetrator = perpetrator;
        this.interactionRole = interactionRole;
        this.behavioralGrimeType = behavioralGrimeType;
    }

    //constructor for when a call tree node satisfies a role.
    public BehavioralMapping(CallTreeNode perpetrator, InteractionRole interactionRole){
        this.perpetrator = perpetrator;
        this.interactionRole = interactionRole;
        this.behavioralGrimeType = BehavioralGrimeType.NONE;
    }

    //this constructor refers to unnecessary actions grime.
    public BehavioralMapping(UMLAttribute umlAttribute, BehavioralGrimeType behavioralGrimeType){
        this.declaration = umlAttribute;
        this.behavioralGrimeType = behavioralGrimeType;
    }

    public String printMe(){
        if (behavioralGrimeType == BehavioralGrimeType.NONE){
            return "Call: " + getPerpetrator().getName() + " satisfies Role: " + interactionRole.getName();
        }else if (behavioralGrimeType == BehavioralGrimeType.UNNECESSARY_ACTIONS){
            return "Call: " + getDeclaration().getName() + " is of type: " + behavioralGrimeType;
        }
        return "Call: " + getPerpetrator().getName() + " violates Role: " + interactionRole.getName() + " and is of type: " + behavioralGrimeType;
    }


}
