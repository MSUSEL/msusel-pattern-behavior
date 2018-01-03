package com.derek.uml.srcML;

import lombok.Getter;
import org.w3c.dom.Element;

public class SrcMLJavaAnonymousClass {
    //will most likely have to use this class. I will be treating anonymous classes as separate uml classes.

    @Getter
    private Element anonymousEle;

    public SrcMLJavaAnonymousClass(Element anonymousEle){
        this.anonymousEle = anonymousEle;
    }
}
