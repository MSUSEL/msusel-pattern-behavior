package com.derek;

import com.derek.rbml.InteractionRole;
import com.derek.uml.CallTreeNode;
import lombok.Getter;

@Getter
public class BehavioralConformance {

    private BehavioralGrimeType behavioralGrimeType;
    private CallTreeNode perpetrator;
    private InteractionRole interactionRole;

    /***
     * constructor for when a call tree node violates a behavioral role.
     * @param perpetrator
     * @param interactionRole
     * @param behavioralGrimeType
     */
    public BehavioralConformance(CallTreeNode perpetrator, InteractionRole interactionRole, BehavioralGrimeType behavioralGrimeType){
        this.perpetrator = perpetrator;
        this.interactionRole = interactionRole;
        this.behavioralGrimeType = behavioralGrimeType;
    }

    //constructor for when a call tree node satisfies a role.
    public BehavioralConformance(CallTreeNode perpetrator, InteractionRole interactionRole){
        this.perpetrator = perpetrator;
        this.interactionRole = interactionRole;
        this.behavioralGrimeType = BehavioralGrimeType.NONE;
    }

    public String printMe(){
        if (behavioralGrimeType == BehavioralGrimeType.NONE){
            return "Call: " + getPerpetrator().getName() + " satisfies Role: " + interactionRole.getName();
        }
        return "Call: " + getPerpetrator().getName() + " violates Role: " + interactionRole.getName() + " and is of type: " + behavioralGrimeType;
    }


}
