package com.derek.uml.srcML;

import lombok.Getter;
import org.w3c.dom.Element;

public class SrcMLJavaLambda {
    //might not need to use this class.
    @Getter
    private Element lambdaEle;

    public SrcMLJavaLambda(Element lambdaEle){
        this.lambdaEle = lambdaEle;
    }
}
