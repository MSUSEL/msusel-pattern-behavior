package com.derek.uml.srcML;

import lombok.Getter;
import org.w3c.dom.Element;

public class SrcMLImplements {

    @Getter
    private Element implementsEle;

    public SrcMLImplements(Element implementsEle) {
        this.implementsEle = implementsEle;
    }
}
