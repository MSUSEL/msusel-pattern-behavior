package com.derek.uml.srcML;

import com.google.common.graph.MutableGraph;
import lombok.Getter;
import org.w3c.dom.Element;

import java.util.List;

@Getter
public class SrcMLSingleExpression extends SrcMLNode {
    private SrcMLExpression expression;

    public SrcMLSingleExpression(Element element){
        super(element);
    }
    @Override
    protected void parse() {
        expression = parseExpression();
    }
    private SrcMLExpression parseExpression(){
        return parseExpressions().isEmpty() ? null : parseExpressions().get(0);
    }


}
