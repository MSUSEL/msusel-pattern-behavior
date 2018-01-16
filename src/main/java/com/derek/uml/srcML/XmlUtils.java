package com.derek.uml.srcML;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.*;

public class XmlUtils {

    private XmlUtils(){
        //private constructor for only 1 instance
    }

    public static Element elementify(Node node){
        if (node.getNodeType() == Node.ELEMENT_NODE) {
            return (Element) node;
        }else{
            return null;
        }
    }


    //utility class to get the string value from an xml element. Supply the parent element, the name of the element you are looking for, and the index for
    //which numbered child you want
    public static String getTextFromXmlElement(Element element, String childName, int index){
        Node toRet = element.getElementsByTagName(childName).item(index);
        if (toRet == null){
            //node did not exist
            return "";
        }else {
            return toRet.getTextContent();
        }
    }


    //special and most common case for getting text from xml elements.
    public static String getFirstTextFromXmlElement(Element element, String childName){
        return getTextFromXmlElement(element, childName, 0);
    }


    //returns a nodelist of only the immediate children. This prevents nested elements with the same name
    //from returning children members
    //ex: <function><type><name>foo</name></type><name>bar</name></function>
    //Because of dom, under element.getElementsByName, you would get both foo and bar. But we only want bar.
    public static List<Node> getImmediateChildren(Node parentNode, String searchString){
        NodeList allChildren = elementify(parentNode).getElementsByTagName(searchString);
        List<Node> toRet = new ArrayList<>();

        for (int i = 0; i < allChildren.getLength(); i++){
            Node child = allChildren.item(i);
            if (child.getParentNode().equals(parentNode)){
                //found first element we care about
                toRet.add(child);
            }
        }
        return toRet;
    }
    public static String stringifyNames(LinkedList<List<String>> names){
        String stringBuilder = "";

        Iterator<List<String>> t = names.iterator();
        List<String> current = t.next();
        stringBuilder += stringifyNames(current);
        int counter = 0;
        while(t.hasNext()){
            stringBuilder += "<";
            current = t.next();
            stringBuilder += stringifyNames(current);
            counter++;
        }
        while (counter > 0){
            stringBuilder += ">";
            counter--;
        }
        return stringBuilder;
    }
    public static String stringifyNames(List<String> names){
        String stringBuilder = "";
        for (int i = 0; i < names.size(); i++){
            stringBuilder += names.get(i);
            if (i != names.size()-1){
                //not last element
                stringBuilder += ".";
            }
        }
        return stringBuilder;
    }
}
