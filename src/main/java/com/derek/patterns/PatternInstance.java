package com.derek.patterns;

import com.derek.PatternType;
import javafx.util.Pair;

import java.util.List;

public class PatternInstance {

    //key: role
    //value: class that fulfills that role
    protected List<Pair<String, String>> listOfPatternRoles;
    protected PatternType patternType;

    public PatternInstance(List<Pair<String, String>> listOfPatternRoles, PatternType patternType) {
        this.listOfPatternRoles = listOfPatternRoles;
        this.patternType = patternType;
    }

    public List<Pair<String, String>> getListOfPatternRoles() {
        return listOfPatternRoles;
    }

    //method to determine if two patterns instances are the same. This can happen across versions in which
    //one (major) pattern role exists and persists, but the minor roles might change.
    //each concrete class should implement this based on their own major pattern roles (i.e., factor = creator)
    public boolean isInstanceEqual(PatternInstance other){
        boolean toRet = false;
        if (this.getValueOfMajorRole(this).equals(other.getValueOfMajorRole(other))){
            toRet = true;
        }
        return toRet;
    }

    private String getMajorRole(){
        switch(patternType){
            case FACTORY_METHOD:return "Creator";
            case CHAIN_OF_RESPONSIBILITY:return "Handler";
            case PROTOTYPE:return "TODO";//TODO
            case SINGLETON:return "Singleton";
            case OBJECT_ADAPTER:return "Adaptee";
            case COMMAND:return "TODO";//TODO
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

    protected String getValueOfMajorRole(PatternInstance pi){
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
}
