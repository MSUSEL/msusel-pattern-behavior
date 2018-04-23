package com.derek.rbml;

import javafx.util.Pair;
import lombok.Getter;

@Getter
public class RBMLMapping<T> {

    private Pair<Role, T> mappedPair;

    public RBMLMapping(Role role, T umlRole) {
        this.mappedPair = new Pair<>(role, umlRole);
    }

    public void printSummary(){
        System.out.println("RBML mapping from RBML element: " + mappedPair.getKey() + " to " + mappedPair.getValue());
    }

    public Role getRole(){
        return mappedPair.getKey();
    }

    public T getUMLArtifact(){
        return mappedPair.getValue();
    }
}
