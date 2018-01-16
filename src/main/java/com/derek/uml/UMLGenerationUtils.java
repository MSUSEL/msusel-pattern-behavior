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
    public static List<UMLAttribute> getUMLAttributes(SrcMLBlock block){
        List<UMLAttribute> attributes = new ArrayList<>();
        //first conditional happens in special cases (enums with both decls and decl_stmts)
        if (block.getDecls().size() > 0){
            //dealing an enum (list of decls)
            List<SrcMLDecl> decls = block.getDecls();
            for (SrcMLDecl decl : decls) {
                //in this case the type is the same as the name... I think.. lol
                attributes.add(new UMLAttribute(decl.getName(), decl.getName()));
            }
        }
        //standard case
        if (block.getDeclStmts().size() > 0){
            //default case, dealing with attributes
            List<SrcMLDeclStmt> declStmts = block.getDeclStmts();
            for (SrcMLDeclStmt declStmt : declStmts) {
                //most of the time this will just be 1
                for (SrcMLDecl decl : declStmt.getDecls()) {
                    //multiple decl would happen with something like: int a,b,c;
                    //otherwise each decl has 1 name/type/etc..
                    attributes.add(new UMLAttribute(decl.getName(), decl.getType().getName()));
                }
            }
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
                params.add(new Pair<>(p.getDecl().getType().getName(), p.getDecl().getName()));
            }
        }
        return params;
    }

    public static UMLOperation getUMLOperation(SrcMLFunction srcMLFunction){
        List<Pair<String, String>> params = UMLGenerationUtils.getParameters(srcMLFunction.getParameterList());
        String name = srcMLFunction.getName();
        String returnType = srcMLFunction.getType().getName();
        //I need to include use dependencies in here eventually.
        return new UMLOperation(name, params, returnType);
    }

    public static UMLOperation getUMLConstructor(SrcMLConstructor srcMLConstructor){
        List<Pair<String, String>> params = UMLGenerationUtils.getParameters(srcMLConstructor.getParameterList());
        String name = srcMLConstructor.getName();
        //constructors don't have return types
        String returnType = "null";
        //I need to include use dependencies in here eventually.
        return new UMLOperation(name, params, returnType);
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
            operations.add(getUMLOperation(function));
        }
        for (SrcMLFunction function : functionDecls){
            operations.add(getUMLOperation(function));
        }
        return operations;
    }

    public static List<UMLOperation> getUMLConstructors(List<SrcMLConstructor> constructorsList, List<SrcMLConstructor> constructorDecls){
        List<UMLOperation> constructors = new ArrayList<>();
        for (SrcMLConstructor constructor : constructorsList){
            constructors.add(getUMLConstructor(constructor));
        }
        for (SrcMLConstructor constructor : constructorDecls){
            //bug here. I don't think I can have constructor decls in java.
            //The bug can be seen in selenium3.6-UrlChecker.java.. SrcML INCORRECTLY classifies a system utility call as a constructor.
            //until I figure this out more, im going to ignore this.
            constructors.add(getUMLConstructor(constructor));
        }
        return constructors;
    }
    /**
     * generates and returns a uml class from the child elements under this class.
     * @return
     */
    public static UMLClass getUMLClass(SrcMLClass srcMLClass){
        SrcMLBlock block = srcMLClass.getBlock();
        List<UMLAttribute> attributes = UMLGenerationUtils.getUMLAttributes(block);
        List<UMLOperation> operations = UMLGenerationUtils.getUMLOperations(block.getFunctions(), block.getFunctionDecls());
        List<UMLOperation> constructors = UMLGenerationUtils.getUMLConstructors(block.getConstructors(), block.getConstructors_decl());

        boolean isAbstract = UMLGenerationUtils.isAbstract(srcMLClass.getSpecifiers());
        UMLClass umlClass = new UMLClass(srcMLClass.getName(), attributes, operations, constructors, isAbstract, "class");
        return umlClass;
    }

    /**
     * generates and return a uml interface from a srcMLInterface class
     * @param srcMLInterface
     * @return
     */
    public static UMLInterface getUMLInterface(SrcMLInterface srcMLInterface){
        SrcMLBlock block = srcMLInterface.getBlock();
        List<UMLOperation> operations = UMLGenerationUtils.getUMLOperations(block.getFunctions(), block.getFunctionDecls());
        UMLInterface umlInterface = new UMLInterface(srcMLInterface.getName(), operations);
        return umlInterface;
    }
    public static UMLClass getUMLEnum(SrcMLEnum srcMLEnum){
        SrcMLBlock block = srcMLEnum.getBlock();
        List<UMLAttribute> attributes = UMLGenerationUtils.getUMLAttributes(block);
        List<UMLOperation> operations = UMLGenerationUtils.getUMLOperations(block.getFunctions(), block.getFunctionDecls());
        List<UMLOperation> constructors = UMLGenerationUtils.getUMLConstructors(block.getConstructors(), block.getConstructors_decl());

        UMLClass umlEnum = new UMLClass(srcMLEnum.getName(), attributes, operations, constructors, false, "enum");
        return umlEnum;
    }

}
