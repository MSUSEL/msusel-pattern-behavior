package com.derek.uml.srcML;

import lombok.Getter;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import java.util.ArrayList;
import java.util.List;

@Getter
public class SrcMLDeclStmt{

    private Element declStmtEle;
    private List<SrcMLDecl> decls;

    public SrcMLDeclStmt(Element declStmtEle) {
        this.declStmtEle = declStmtEle;
        parse();
    }
    private void parse() {
        parseDecls();
    }
    private void parseDecls(){
        decls = new ArrayList<>();
        List<Node> declNodes = XmlUtils.getImmediateChildren(declStmtEle, "decl");
            for (Node declNode : declNodes){
            decls.add(new SrcMLDecl(XmlUtils.elementify(declNode)));
        }
    }
}
