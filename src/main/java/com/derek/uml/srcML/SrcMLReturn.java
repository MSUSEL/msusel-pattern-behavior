package com.derek.uml.srcML;

import lombok.Getter;
import org.w3c.dom.Element;

@Getter
public class SrcMLReturn extends SrcMLNode {
    private SrcMLExpression expression;

    public SrcMLReturn(Element element) {
        super(element);
    }
    protected void parse(){
        expression = parseExpression();
    }
}
