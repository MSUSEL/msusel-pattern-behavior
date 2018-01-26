/**
 * MIT License
 *
 * Copyright (c) 2017 Montana State University, Gianforte School of Computing
 * Software Engineering Laboratory
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.derek.uml.srcML;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.*;

public class XmlUtils {

    private XmlUtils(){
        //private constructor for only 1 instance
    }

    public static List<Element> getChildElements(Element e){
        List<Element> children = new ArrayList<>();
        NodeList nl = e.getChildNodes();

        for (int i = 0; i < nl.getLength(); i++){
            Element ele = elementify(nl.item(i));
            if (ele != null) {
                children.add(ele);
            }
        }
        return children;
    }

    public static Element elementify(Node node){
        if (node.getNodeType() == Node.ELEMENT_NODE) {
            return (Element) node;
        }else{
            return null;
        }
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
