package com.derek.rbml;

import javafx.util.Pair;
import lombok.Getter;

@Getter
public class RBMLMapping<T> {

    private Pair<Role, T> mappedPair;
    private Role role;
    private T umlArtifact;

    public RBMLMapping(Role role, T umlRole) {
        this.role = role;
        this.umlArtifact = umlRole;
        this.mappedPair = new Pair<>(role, umlRole);
    }

    public void printSummary(){
        System.out.println("RBML mapping from RBML element: " + mappedPair.getKey() + " to " + mappedPair.getValue());
    }
}
