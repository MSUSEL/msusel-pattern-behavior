package com.derek.uml.srcML;

import lombok.Getter;
import org.w3c.dom.Element;

@Getter
public class SrcMLControl extends SrcMLNode{
    //'control ' element is only used by for loops.
    private SrcMLInit init;
    private SrcMLCondition condition;
    private SrcMLIncr incr;

    public SrcMLControl(Element controlEle){
        super(controlEle);
    }

    protected void parse(){
        init = parseInit();
        condition = parseCondition();
        incr = parseIncr();
    }
}
