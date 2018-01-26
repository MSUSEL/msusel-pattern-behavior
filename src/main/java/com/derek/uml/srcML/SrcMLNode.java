package com.derek.uml.srcML;

import com.sun.javaws.jnl.XMLUtils;
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
        Element childEle = XmlUtils.elementify(child);
        if (childEle != null) {
            nodeOrder.add(childEle.getTagName());
            while ((childEle = XmlUtils.elementify(childEle.getNextSibling())) != null) {
                nodeOrder.add(childEle.getTagName());
            }
        }
    }

    protected abstract void parse();
}
