package com.derek.uml;

import com.google.common.graph.MutableValueGraph;
import com.google.common.graph.ValueGraphBuilder;

public class UmlSequenceDiagram {
    private MutableValueGraph<UMLLifeline, UMLMessage> sequenceDiagram;

    public UmlSequenceDiagram() {
        sequenceDiagram = ValueGraphBuilder.directed().allowsSelfLoops(true).build();
    }

    public void addMessageToGraph(UMLMessage message){
        //sequenceDiagram.putEdgeValue(message.getFrom(), message.getTo(), message);
    }
}
