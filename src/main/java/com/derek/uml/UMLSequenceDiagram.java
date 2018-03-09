package com.derek.uml;

import com.google.common.graph.MutableValueGraph;
import com.google.common.graph.ValueGraphBuilder;
import lombok.Getter;

@Getter
public class UMLSequenceDiagram {

    private MutableValueGraph<UMLLifeline, UMLMessage> sequenceDiagram;

    public UMLSequenceDiagram(){
        sequenceDiagram = ValueGraphBuilder.directed().allowsSelfLoops(true).build();
    }

}
