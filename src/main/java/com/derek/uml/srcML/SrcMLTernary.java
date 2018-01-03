package com.derek.uml.srcML;

import lombok.Getter;
import org.w3c.dom.Element;

public class SrcMLTernary {
    //likely not have to use this.

    @Getter
    private Element ternaryEle;
    public SrcMLTernary(Element ternaryEle){
        this.ternaryEle = ternaryEle;
    }
}
