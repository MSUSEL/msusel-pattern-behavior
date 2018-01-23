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

import lombok.Getter;
import lombok.Setter;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import java.util.ArrayList;
import java.util.List;

@Getter
public class SrcMLDataType extends SrcMLNode{
    private Element typeEle;
    private List<String> specifiers;
    private List<String> modifiers;
    //not sure if I'll need argumentlist, but at least it is there.
    private SrcMLArgumentList argumentList;
    private List<SrcMLName> names;

    //type<type>(typeTypeAttr?):(specifier|modifier|decltype|templateArgumentList|name)*;

    //this variable represents nested types, which are usually generics. Bear in mind this field can also be null.
    //not sure how I am going to access types n deep yet.. such as List<List<List<List<List<....String>>>>...>> (how do I get the string?
    private SrcMLDataType nestedType;

    public SrcMLDataType(Element typeEle){
        super(typeEle);
        this.typeEle = typeEle;
        parse();
    }

    protected void parse(){
        parseSpecifiers();
        parseModifiers();
        parseArgumentList();
        parseName();
    }

    private void parseSpecifiers(){
        //in the context of types, this could be any combination or number of: "final, public, static", etc.
        specifiers = new ArrayList<>();
        List<Node> specifierNodes = XmlUtils.getImmediateChildren(typeEle, "specifier");
        for (Node specifier : specifierNodes){
            specifiers.add(specifier.getTextContent());
        }
    }

    private void parseModifiers(){
        modifiers = new ArrayList<>();
        List<Node> modifierNodes = XmlUtils.getImmediateChildren(typeEle, "modifier");
        for (Node modifier : modifierNodes){
            modifiers.add(modifier.getTextContent());
        }
    }

    private void parseArgumentList(){
        //I don't know how one can get more than 1 argumentlist.. but its in the docs.
        //I am making an executive decision to only have 1 argument list.
        List<Node> argumentListNodes = XmlUtils.getImmediateChildren(typeEle, "argument_list");
        for (Node argumentListNode : argumentListNodes){
            argumentList = new SrcMLArgumentList(XmlUtils.elementify(argumentListNode));
        }
    }

    private void parseName(){
        //should only be 1 name, but it can have many apparently
        names = new ArrayList<>();
        List<Node> nameNodes = XmlUtils.getImmediateChildren(typeEle, "name");
        for (Node nameNode : nameNodes){
            names.add(new SrcMLName(XmlUtils.elementify(nameNode)));
        }
    }
    public String getName(){
        if (names.size() > 1){
            //dont' know how this is possible
            System.out.println("More than 1 name in a data type.. requires debugging.");
            System.exit(0);
            return null;
        }else {
            return names.get(0).getName();
        }
    }
}
