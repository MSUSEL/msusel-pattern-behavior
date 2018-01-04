package com.derek.uml;

import com.derek.uml.Visibility;

public class UMLGenerationUtils {
    //utility class for common uml things..

    public static Visibility getVisibilityFromSpecifier(String specifier){
        switch(specifier.toLowerCase()){
            case "private":
                return Visibility.PRIVATE;
            case "public":
                return Visibility.PUBLIC;
            case "protected":
                return Visibility.PROTECTED;
        }
        return Visibility.UNSPECIFIED;
    }



    //returns true if the type is a primitive type (including Strring)
    //flase otherwise
    public static boolean isTypePrimitive(String type){
        switch(type){
            case "boolean":
            case "byte":
            case "char":
            case "short":
            case "int":
            case "long":
            case "float":
            case "double":
                //while String technically isn't a primitive, I think its worth including because its so common and
                //I don't want to pull from the String java class.
            case "String":
                return true;
        }
        return false;
    }


}
