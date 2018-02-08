package com.derek.uml.srcML;

import lombok.Getter;
import org.w3c.dom.Element;

@Getter
public class SrcMLSynchronized extends SrcMLNode {
    private SrcMLInit init;
    private SrcMLBlock block;

    public SrcMLSynchronized(Element element){
        super(element);
    }

    @Override
    protected void parse() {
        init = parseInit();
        block = parseBlock();
    }

}
