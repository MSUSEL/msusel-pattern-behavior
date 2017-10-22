package com.derek.patterns;

import com.derek.PatternType;
import javafx.util.Pair;

import java.util.List;
import java.util.regex.Pattern;

public class PatternInstanceTwoRoles extends PatternInstance {

    public PatternInstanceTwoRoles(List<Pair<String, String>> listOfPatternRoles, PatternType patternType) {
        super(listOfPatternRoles, patternType);
    }


    //some patterns do not have a secondary major role, so they return blank. They will never get called anyway because
    //of the way this class is being instantiated.
    private String getSecondMajorRole(){
        switch(patternType){
            case FACTORY_METHOD:return "";
            case CHAIN_OF_RESPONSIBILITY:return "";
            case PROTOTYPE:return "TODO";//TODO
            case SINGLETON:return "";
            case OBJECT_ADAPTER:return "Adapter";
            case COMMAND:return "TODO";//TODO
            case COMPOSITE:return "TODO";//TODO
            case DECORATOR:return "Component";//TODO
            case OBSERVER:return "TODO";//TODO
            case STATE:return "Context";
            case STRATEGY:return "Context";
            case BRIDGE:return "Implementor";
            case TEMPLATE_METHOD:return "";
            case VISITOR:return "TODO";//TODO
            case PROXY:return "RealSubject";
            case PROXY2:return "RealSubject";
        }
        //if we get here that means a pattern type is not configured yet.

        System.out.println("Should not have reached here, considering each pattern has at least one major role.");
        System.exit(0);
        return "";
    }


    //A bit crazy logic going on here. Debug if I need to. Might be worth writing some UTs for this too.
    @Override
    public boolean isInstanceEqual(PatternInstance other) {
        if (super.isInstanceEqual(other) && this.getValueOfSecondMajorRole(this).equals(((PatternInstanceTwoRoles)other).getValueOfSecondMajorRole(other))){
            return true;
        }else{
            return false;
        }
    }

    protected String getValueOfSecondMajorRole(PatternInstance pi) {
        for (Pair<String, String> p : pi.getListOfPatternRoles()){
            String key = p.getKey();
            if (p.getKey().equals(getSecondMajorRole())){
                //second major role of pattern, now what I care about is the value
                return p.getValue();
            }
        }
        System.out.println("Second major role of pattern not found.");
        return "null";
    }


    //comments from an earlier refactoring::
    //DECORATOR:
            //after looking at this for a while, I think that the Decorator pattern has one major role...
            //while the output from the detection tool suggests that Components are also a role, the defining
            //reason why Components aren't used is because a Component can be decorated in differenct ways, thus the decorator.

            //after thinking about this a bit more it seems that a decorator can decorate for more than Component...
            //Does this mean its a different pattern instance though?
            //I think it does.. Meaning that we need to use 2 major elements.

    //Object Adapter:
            //this is a tricky one because there are three unique roles for the objecct adapter output (Adaptee, Adapter, and adaptee).
            //I think the constitution for a unique Object Adpater instance is that it has a unique pair of Adaptee and Adapter

    //Proxy2:
            //the tool makes it so Proxy2 includes the abstract Subject class. Becuase RealSubject (should) implement Subject,
            //I am not considering the Subject role as a unique identifier.

    //State:
            //for the state pattern instance I found an issue with the detection tool:

            //the tool correclty identifies that a state pattern exists, but it assumes that the State role is filled by the wrong object.
            //In guava23, the tool points at com.google.common.collect.Multiset as the State role, but actually the Multiset object
            //HOLDS the state role (which is adequately named 'State').
            //I think I can still get around this issue but I need to make sure I manually verify anything concerning the state pattern.

            //Also, I am going to use a unique pair of State and Context to identify State patterns.




}
