package com.derek.uml.srcML;

import javafx.util.Pair;
import lombok.Getter;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import java.util.ArrayList;
import java.util.List;

@Getter
public class SrcMLParameterList {
    //similar to SrcMLArgumentList, I'm not sure how much of this I will need, but I'm creatin gthe class just in case.
    private Element parameterListEle;
    private List<SrcMLParameter> parameters;

    public SrcMLParameterList(Element parameterListEle){
        this.parameterListEle = parameterListEle;
        parse();
    }
    private void parse(){
        parseParameters();
    }
    private void parseParameters(){
        parameters = new ArrayList<>();
        List<Node> parameterNodes = XmlUtils.getImmediateChildren(parameterListEle, "parameter");
        for (Node parameterNode : parameterNodes){
            parameters.add(new SrcMLParameter(XmlUtils.elementify(parameterNode)));
        }
    }

    @Getter
    public class SrcMLParameter{
        //after reviewing examples of parameterlist output, it appears that the documentation for this is wrong..
        //it appears that the <parameter> element ALWAYS has a <decl> element under it and not a <type> element
        private Element parameterEle;
        private SrcMLDecl decl;
        private SrcMLDataType type;
        private SrcMLName name;

        public SrcMLParameter(Element parameterEle) {
            this.parameterEle = parameterEle;
            parse();
        }
        private void parse(){
            parseDecl();
            parseType();
            parseName();
        }
        private void parseDecl(){
            List<Node> declNodes = XmlUtils.getImmediateChildren(parameterEle, "decl");
            for (Node declNode : declNodes){
                decl = new SrcMLDecl(XmlUtils.elementify(declNode));
            }
        }
        private void parseType(){
            List<Node> typeNodes = XmlUtils.getImmediateChildren(parameterEle, "type");
            for (Node typeNode : typeNodes){
                type = new SrcMLDataType(XmlUtils.elementify(typeNode));
            }
        }
        private void parseName(){
            List<Node> nameNodes = XmlUtils.getImmediateChildren(parameterEle, "name");
            for (Node nameNode : nameNodes){
                name = new SrcMLName(XmlUtils.elementify(nameNode));
            }
        }
        public String getName(){
            return name.getName();
        }
    }

    public List<Pair<String, String>> getParams(){
        List<Pair<String, String>> params = new ArrayList<>();

        for (SrcMLParameter p : parameters){
            if (p.getDecl() != null){
                //reference to comment in SrcMLParameter class
                params.add(new Pair<String, String>(p.getDecl().getType().getName(), p.getName()));
            }
        }

        return params;
    }
}
