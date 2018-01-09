package com.derek.uml.srcML;

import lombok.Getter;
import org.w3c.dom.Element;

@Getter
public class SrcMLDefault {
    //will likely not have to use this class
    private Element defaultEle;

    public SrcMLDefault(Element defaultEle){
        this.defaultEle = defaultEle;
    }
}
