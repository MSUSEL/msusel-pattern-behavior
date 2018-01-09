package com.derek.uml.srcML;

import lombok.Getter;
import org.w3c.dom.Element;

@Getter
public class SrcMLExtends {
    private Element extendsEle;

    public SrcMLExtends(Element extendsEle) {
        this.extendsEle = extendsEle;
    }
}
