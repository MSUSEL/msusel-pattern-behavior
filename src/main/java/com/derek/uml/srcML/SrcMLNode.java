package com.derek.uml.srcML;

import lombok.Getter;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import java.util.ArrayList;
import java.util.List;

@Getter
public abstract class SrcMLNode {
    protected Element element;
    protected List<String> nodeOrder;

    public SrcMLNode(Element element){
        this.element = element;
        nodeOrder = new ArrayList<>();
        if (element != null) {
            findOrder();
        }
    }
    private void findOrder(){
        Node child = element.getFirstChild();
        if (child != null) {
            nodeOrder.add(child.getNodeName());
            while ((child = child.getNextSibling()) != null) {
                nodeOrder.add(child.getNodeName());
            }
        }
    }

    protected abstract void parse();
}
