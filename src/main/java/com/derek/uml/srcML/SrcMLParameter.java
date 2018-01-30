package com.derek.uml.srcML;

import lombok.Getter;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import java.util.List;

@Getter
public class SrcMLParameter extends SrcMLNode{
    //after reviewing examples of parameterlist output, it appears that the documentation for this is wrong..
    //it appears that the <parameter> element ALWAYS has a <decl> element under it and not a <type> element
    private SrcMLDecl decl;
    private SrcMLDataType type;

    public SrcMLParameter(Element parameterEle) {
        super(parameterEle);
    }
    protected void parse() {
        decl = parseDecl();
        type = parseDataType();
    }
    private SrcMLDecl parseDecl(){
        return parseDecls().isEmpty() ? null : parseDecls().get(0);
    }

}
