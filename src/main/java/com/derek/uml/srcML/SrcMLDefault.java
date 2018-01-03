package com.derek.uml.srcML;

import lombok.Getter;
import org.w3c.dom.Element;

public class SrcMLDefault {
    //will likely not have to use this class
    @Getter
    private Element defaultEle;

    public SrcMLDefault(Element defaultEle){
        this.defaultEle = defaultEle;
    }
}
