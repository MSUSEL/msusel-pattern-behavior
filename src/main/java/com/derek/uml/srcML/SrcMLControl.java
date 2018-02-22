package com.derek.uml.srcML;

import com.derek.uml.CallTreeNode;
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
    public void fillCallTree(CallTreeNode<SrcMLNode> root){
        //init is its own structural lifeline, and it will have a callTree independently.
        root.addChild(init.getCallTree());

        if (condition != null) {
            buildCallTree(root, condition.getExpression());
        }
        if (incr != null) {
            buildCallTree(root, incr.getExpression());
        }

    }
}
