package com.derek.uml.srcML;

import lombok.Getter;
import org.w3c.dom.Element;


@Getter
public class SrcMLContinue extends SrcMLNode{
    private SrcMLName name;

    public SrcMLContinue(Element continueEle) {
        super(continueEle);
    }
    protected void parse(){
        name = parseName();
    }

}
