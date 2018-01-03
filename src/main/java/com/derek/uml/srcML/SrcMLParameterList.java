package com.derek.uml.srcML;

import lombok.Getter;
import org.w3c.dom.Element;

public class SrcMLParameterList {
    //similar to SrcMLArgumentList, I'm not sure how much of this I will need, but I'm creatin gthe class just in case.
    @Getter
    private Element parameterListEle;

    public SrcMLParameterList(Element parameterListEle){
        this.parameterListEle = parameterListEle;
    }



}
