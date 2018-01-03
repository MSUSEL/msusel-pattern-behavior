package com.derek.uml.srcML;

import lombok.Getter;
import org.w3c.dom.Element;

public class SrcMLCall {
    //will likely have ot use this class for sequence diagram generation
    @Getter
    private Element callEle;

    public SrcMLCall(Element callEle){
        this.callEle = callEle;
    }
}
