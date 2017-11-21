package com.derek.model.patterns;

import com.derek.model.PatternType;
import com.derek.model.SoftwareVersion;
import javafx.util.Pair;

import java.util.List;

public class PatternInstance {

    //key: role
    //value: class that fulfills that role
    protected List<Pair<String, String>> listOfPatternRoles;


    protected PatternType patternType;

    //software version this pattern instance appeared in
    protected SoftwareVersion softwareVersion;


    public PatternInstance(List<Pair<String, String>> listOfPatternRoles, PatternType patternType, SoftwareVersion version) {
        this.listOfPatternRoles = listOfPatternRoles;
        this.patternType = patternType;
        this.softwareVersion = version;
    }

    public List<Pair<String, String>> getListOfPatternRoles() {
        return listOfPatternRoles;
    }

    private String getMajorRole(){
        switch(patternType){
            case FACTORY_METHOD:return "Creator";
            case CHAIN_OF_RESPONSIBILITY:return "Handler";
            case PROTOTYPE:return "TODO";//TODO
            case SINGLETON:return "Singleton";
            case OBJECT_ADAPTER:return "Adaptee";
            case COMMAND:return "Receiver";
            case COMPOSITE:return "TODO";//TODO
            case DECORATOR:return "Decorator";//TODO
            case OBSERVER:return "TODO";//TODO
            case STATE:return "State";
            case STRATEGY:return "Strategy";
            case BRIDGE:return "Abstraction";
            case TEMPLATE_METHOD:return "AbstractClass";
            case VISITOR:return "TODO";//TODO
            case PROXY:return "Proxy";
            case PROXY2:return "Proxy";
            }
        //if we get here that means a pattern type is not configured yet.

        System.out.println("Should not have reached here, considering each pattern has at least one major role.");
        System.exit(0);
        return "";
    }

    public void setListOfPatternRoles(List<Pair<String, String>> listOfPatternRoles) {
        this.listOfPatternRoles = listOfPatternRoles;
    }

    public String toString(){
        String toRet = "";
            for (Pair<String, String> p : listOfPatternRoles){
                toRet += p.getKey() + ": " + p.getValue() + ", ";
            }
        return toRet;
    }

    public String getValueOfMajorRole(PatternInstance pi){
        for (Pair<String, String> p : pi.getListOfPatternRoles()){
            String key = p.getKey();
            if (p.getKey().equals(getMajorRole())){
                //major role of pattern, now what I care about is the value
                return p.getValue();
            }
        }
        System.out.println("Major role of pattern not found... Every pattern should have one and only one major role (I think)...");
        return "null";
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
            case COMMAND:return "ConcreteCommand";
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

    //method to determine if two patterns instances are the same. This can happen across versions in which
    //one (major) pattern role exists and persists, but the minor roles might change.
    //each concrete class should implement this based on their own major pattern roles (i.e., factor = creator)
    public boolean isInstanceEqual(PatternInstance other) {
        if (this.getValueOfMajorRole(this).equals(other.getValueOfMajorRole(other)) &&
                this.getValueOfSecondMajorRole(this).equals(other.getValueOfSecondMajorRole(other))){
            return true;
        }else{
            return false;
        }
    }

    public String getValueOfSecondMajorRole(PatternInstance pi) {
        for (Pair<String, String> p : pi.getListOfPatternRoles()){
            String key = p.getKey();
            if (p.getKey().equals(getSecondMajorRole())){
                //second major role of pattern, now what I care about is the value
                return p.getValue();
            }
        }
        //either pattern only has one major role or we are looking at two different pattern isntances
        return "null";
    }

    public PatternType getPatternType() {
        return patternType;
    }

    public SoftwareVersion getSoftwareVersion() {
        return softwareVersion;
    }

    public void setSoftwareVersion(SoftwareVersion softwareVersion) {
        this.softwareVersion = softwareVersion;
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


    //Command:
    //Intuitively, I don't think that the Receiver should be a major role. The first line describing the Command pattern via the
    //GoF book says that the behaviors that happen should happen without knowing anything about the receiver of the request.
    //With that said, if the Receiver is kept abstract then that describes the family of classes that can implement the commands.
    //I think the detection tool defines 'Receiver' as that abstract class, and 'receiver' as the implementing classes.
    //In that case, I think Receiver is a major role.
    //Unfortunately there is no Invoker role listed in the detector tool. I feel almost like Invoker would be a role by itself.
    //but maybe the tool expects the Invoker role to be encapsulated within the command or client -??

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
