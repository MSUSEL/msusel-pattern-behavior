package com.derek.uml;

public enum Relationship{
    REALIZATION,
    GENERALIZATION,
    ASSOCIATION,
    //I likely won't implement aggregation and composition as they are very tough to properly implement
    AGGREGATION,
    COMPOSITION,
    DEPENDENCY,
    OWNERSHIP,
    //not sure what you would use unspecified for, but im putting it in just in case
    UNSPECIFIED;

    public String plantUMLTransform(){
        switch(this){
            case REALIZATION:
                return "..|>";
            case GENERALIZATION:
                return "--|>";
            case ASSOCIATION:
                return "-->";
            case AGGREGATION:
                return "o--";
            case COMPOSITION:
                return "*--";
            case DEPENDENCY:
                return "..>";
            case OWNERSHIP:
                return "+--";
        }
        return "--";
    }
}
