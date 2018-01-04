package com.derek.uml.srcML;

import lombok.Getter;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import java.util.List;

public class SrcMLCatch {
    //not sure if I need the multicatch.. or even the catch at all.
    @Getter
    private Element catchEle;
    private SrcMLParameterList parameterList;
    private SrcMLBlock block;

    public SrcMLCatch(Element catchEle) {
        this.catchEle = catchEle;
        parse();
    }
    private void parse(){
        parseParameterList();
        parseBlock();
    }
    private void parseParameterList(){
        List<Node> parameterNodes = XmlUtils.getImmediateChildren(catchEle, "parameter_list");
        for (Node parameterNode : parameterNodes){
            parameterList = new SrcMLParameterList(XmlUtils.elementify(parameterNode));
        }
    }
    private void parseBlock(){
        List<Node> blockNodes = XmlUtils.getImmediateChildren(catchEle, "block");
        for (Node blockNode : blockNodes){
            block = new SrcMLBlock(XmlUtils.elementify(blockNode));
        }
    }
}
