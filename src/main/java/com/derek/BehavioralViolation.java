package com.derek;

import com.derek.rbml.InteractionRole;
import com.derek.uml.CallTreeNode;
import lombok.Getter;

@Getter
public class BehavioralViolation {

    private BehavioralGrimeType behavioralGrimeType;
    private CallTreeNode perpetrator;
    private InteractionRole interactionRole;

    public BehavioralViolation(CallTreeNode perpetrator, InteractionRole interactionRole, BehavioralGrimeType behavioralGrimeType){
        this.perpetrator = perpetrator;
        this.interactionRole = interactionRole;
        this.behavioralGrimeType = behavioralGrimeType;
    }

    public String printViolation(){
        return "Call: " + getPerpetrator().getName() + " violates " + interactionRole.getName() + " and is of type: " + behavioralGrimeType;
    }


}
