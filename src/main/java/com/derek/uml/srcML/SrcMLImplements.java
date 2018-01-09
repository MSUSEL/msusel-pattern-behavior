package com.derek.uml.srcML;

import lombok.Getter;
import org.w3c.dom.Element;

@Getter
public class SrcMLImplements {

    private Element implementsEle;

    public SrcMLImplements(Element implementsEle) {
        this.implementsEle = implementsEle;
    }
}
