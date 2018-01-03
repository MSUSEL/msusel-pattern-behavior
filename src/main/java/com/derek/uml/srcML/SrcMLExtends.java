package com.derek.uml.srcML;

import lombok.Getter;
import org.w3c.dom.Element;

public class SrcMLExtends {
    @Getter
    private Element extendsEle;

    public SrcMLExtends(Element extendsEle) {
        this.extendsEle = extendsEle;
    }
}
