package com.derek.uml;

import lombok.Getter;

@Getter
public class UMLMessage {

    private UMLClassifier from;
    private UMLClassifier to;

    public UMLMessage(UMLClassifier from, UMLClassifier to) {
        this.from = from;
        this.to = to;
    }
}
