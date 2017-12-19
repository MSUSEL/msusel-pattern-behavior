package com.derek.uml.srcML;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

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

}
