package com.derek.uml.srcML;

import lombok.Getter;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import java.util.List;

public class SrcMLDecl {
    @Getter
    private Element declEle;
    @Getter
    private SrcMLDataType type;
    @Getter
    private SrcMLName name;
    @Getter
    private SrcMLRange range;
    @Getter
    private SrcMLInit init;

    public SrcMLDecl(Element declEle) {
        this.declEle = declEle;
        parse();
    }
    private void parse(){
        parseDataType();
        parseName();
        parseRange();
        parseInit();
    }
    private void parseDataType(){
        List<Node> typeNodes = XmlUtils.getImmediateChildren(declEle, "type");
        for (Node typeNode : typeNodes){
            type = new SrcMLDataType(XmlUtils.elementify(typeNode));
        }
    }
    private void parseName(){
        List<Node> nameNodes = XmlUtils.getImmediateChildren(declEle, "name");
        for (Node nameNode : nameNodes){
            name = new SrcMLName(XmlUtils.elementify(nameNode));
        }
    }
    private void parseRange(){
        List<Node> rangeNodes = XmlUtils.getImmediateChildren(declEle, "range");
        for (Node rangeNode : rangeNodes){
            range = new SrcMLRange(XmlUtils.elementify(rangeNode));
        }
    }
    private void parseInit(){
        List<Node> initNodes = XmlUtils.getImmediateChildren(declEle, "init");
        for (Node initNode : initNodes){
            init = new SrcMLInit(XmlUtils.elementify(initNode));
        }
    }


    public class SrcMLRange{
        //only used by decl and is a pretty simple class
        @Getter
        private Element rangeEle;
        @Getter
        private SrcMLExpression expression;

        public SrcMLRange(Element rangeEle) {
            this.rangeEle = rangeEle;
            parse();
        }
        private void parse(){
            parseExpression();
        }
        private void parseExpression(){
            List<Node> expressionNodes = XmlUtils.getImmediateChildren(rangeEle, "expr");
            for (Node expressionNode : expressionNodes){
                //can have 0 or 1
                expression = new SrcMLExpression(XmlUtils.elementify(expressionNode));
            }
        }
    }
}