package com.derek.uml.srcML;

import lombok.Getter;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import java.util.List;

@Getter
public class SrcMLJavaLambda {
    //might not need to use this class.
    private Element lambdaEle;
    private SrcMLParameterList parameterList;
    private SrcMLBlock block;

    public SrcMLJavaLambda(Element lambdaEle) {
        this.lambdaEle = lambdaEle;
        parse();
    }
    private void parse() {
        parseParameterList();
        parseBlock();
    }
    private void parseParameterList(){
        List<Node> parameterNodes = XmlUtils.getImmediateChildren(lambdaEle, "parameter_list");
        for (Node parameterNode : parameterNodes){
            parameterList = new SrcMLParameterList(XmlUtils.elementify(parameterNode));
        }
    }
    private void parseBlock(){
        List<Node> blockNodes = XmlUtils.getImmediateChildren(lambdaEle, "block");
        for (Node blockNode : blockNodes){
            block = new SrcMLBlock(XmlUtils.elementify(blockNode));
        }
    }

}
