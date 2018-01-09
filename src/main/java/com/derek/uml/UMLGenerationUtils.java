package com.derek.uml;

import com.derek.uml.srcML.*;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;

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

    /**
     * from a list of string specifiers from srcML, return true if abstract is a specifier
     * @param specifiers
     * @return
     */
    public static boolean isAbstract(List<String> specifiers){
        boolean isAbstract = false;
        for (String s : specifiers){
            if (s.equals("abstract")){
                isAbstract = true;
            }
        }
        return isAbstract;
    }

    /**
     * from a list of string specifiers from srcML, return true if static is a specifier
     * @param specifiers
     * @return
     */
    public static boolean isStatic(List<String> specifiers){
        boolean isStatic = false;
        for (String s : specifiers){
            if (s.equals("static")){
                isStatic = true;
            }
        }
        return isStatic;
    }
    public static List<UMLAttribute> getUMLAttributes(List<SrcMLBlock.SrcMLDeclStmt> declStmts){
        List<UMLAttribute> attributes = new ArrayList<>();
        //multiple decl_stmts would happen with something like: int a,b,c;
        //otherwise each decl_Stmt has 1 decl.

        for (SrcMLBlock.SrcMLDeclStmt declStmt: declStmts){


        }


        return attributes;
    }

    /**
     * Collects parameters from the SrcML data structures and returns a strcture in a much more edible form
     *
     * @return (array)list of Pairs of Strings, where string1 is datatype and string2 is name
     */
    public static List<Pair<String, String>> getParameters(SrcMLParameterList parameterList){
        List<Pair<String, String>> params = new ArrayList<>();
        for (SrcMLParameterList.SrcMLParameter p : parameterList.getParameters()){
            if (p.getDecl() != null){
                //reference to comment in SrcMLParameter class
                params.add(new Pair<String, String>(p.getDecl().getType().getName(), p.getDecl().getName()));
            }
        }
        return params;
    }

    /**
     * finds and collects/returns a list of uml operations from a list of srcml function and srcml function decls.
     * @param functions
     * @param functionDecls
     * @return
     */
    public static List<UMLOperation> getUMLOperations(List<SrcMLFunction> functions, List<SrcMLFunction> functionDecls){
        List<UMLOperation> operations = new ArrayList<>();
        for (SrcMLFunction function : functions){
            operations.add(function.getOperation());
        }
        for (SrcMLFunction function : functionDecls){
            operations.add(function.getOperation());
        }
        return operations;
    }
    public static List<UMLOperation> getUMLConstructors(List<SrcMLConstructor> constructorsList, List<SrcMLConstructor> constructorDecls){
        List<UMLOperation> constructors = new ArrayList<>();
        for (SrcMLConstructor constructor : constructorsList){
            constructors.add(constructor.getOperation());
        }
        for (SrcMLConstructor constructor : constructorDecls){
            constructors.add(constructor.getOperation());
        }
        return constructors;
    }
}
