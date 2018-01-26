package com.derek.uml.srcML;

import lombok.Getter;
import org.w3c.dom.Element;

import java.util.ArrayList;
import java.util.List;

@Getter
public abstract class SrcMLNode {
    protected Element element;
    protected List<String> childNodeOrder;

    public SrcMLNode(Element element){
        if (element != null) {
            //will be null for anonymous classes, and I really don't care about them.
            this.element = element;
            collectOrderOfChildNodes();
            parse();
        }
    }

    private void collectOrderOfChildNodes(){
        childNodeOrder = new ArrayList<>();
        List<Element> childElements = XmlUtils.getChildElements(element);
        for (Element e : childElements){
            childNodeOrder.add(e.getNodeName());
        }
    }

    protected abstract void parse();
}
