package com.derek.uml.srcML;

import lombok.Getter;
import org.w3c.dom.Element;

public class SrcMLBlock {
    //likely nbot have to use this class

    @Getter
    private Element blockEle;

    public SrcMLBlock(Element blockEle){
        this.blockEle = blockEle;
    }

}
