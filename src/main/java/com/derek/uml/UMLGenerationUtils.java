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
package com.derek.uml;

import com.derek.uml.srcML.*;
import com.sun.javaws.jnl.XMLUtils;
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
                return true;
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
                return true;
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
    public static List<String> getUMLExtendsParents(SrcMLSuper srcMLSuper){
        List<String> parents = new ArrayList<>();
        //two types of supers; extends and implements
        if (srcMLSuper != null){
            for (SrcMLExtends srcMLExtends : srcMLSuper.getExtenders()){
                parents.add(srcMLExtends.getName());
            }
        }
        return parents;
    }
    public static List<String> getUMLImplementsParents(SrcMLSuper srcMLSuper){
        List<String> parents = new ArrayList<>();
        if (srcMLSuper != null) {
            for (SrcMLImplements srcMLImplements : srcMLSuper.getImplementors()) {
                parents.add(srcMLImplements.getName());
            }
        }
        return parents;
    }
    public static List<String> getResidingPackage(SrcMLBlock block){
        List<String> residingPackage = new ArrayList<>();
        for (SrcMLPackage srcMLPackage : block.getPackages()){
            //should only ever be 1 residing package.
            residingPackage = srcMLPackage.getNames();
        }
        return residingPackage;
    }

    /**
     * finds and returns a list of list of strings of all imports.
     *
     * @param block
     * @return first list is one import, second list is package structure UNDER import.
     */
    public static List<List<String>> getImports(SrcMLBlock block){
        List<List<String>> imports = new ArrayList<>();
        for (SrcMLImport srcMLImport : block.getImports()){
            imports.add(srcMLImport.getNames());
        }
        return imports;
    }
    /**
     * generates and returns a uml class from the child elements under this class.
     * @return
     */
    public static UMLClass getUMLClass(SrcMLClass srcMLClass, List<String> residingPackage, List<List<String>> imports){
        SrcMLBlock block = srcMLClass.getBlock();
        List<UMLAttribute> attributes = UMLGenerationUtils.getUMLAttributes(block);
        List<UMLOperation> operations = UMLGenerationUtils.getUMLOperations(block.getFunctions(), block.getFunctionDecls());
        List<UMLOperation> constructors = UMLGenerationUtils.getUMLConstructors(block.getConstructors(), block.getConstructors_decl());
        List<String> extendsParents = UMLGenerationUtils.getUMLExtendsParents(srcMLClass.getSuperLink());
        List<String> implementsParents = UMLGenerationUtils.getUMLImplementsParents(srcMLClass.getSuperLink());

        boolean isAbstract = UMLGenerationUtils.isAbstract(srcMLClass.getSpecifiers());
        UMLClass umlClass = new UMLClass(srcMLClass.getName(), residingPackage, imports, attributes, operations, constructors, isAbstract, extendsParents, implementsParents, "class");
        return umlClass;
    }

    /**
     * generates and return a uml interface from a srcMLInterface class
     * @param srcMLInterface
     *
     * @return
     */
    public static UMLInterface getUMLInterface(SrcMLInterface srcMLInterface, List<String> residingPackage, List<List<String>> imports){
        SrcMLBlock block = srcMLInterface.getBlock();
        List<UMLOperation> operations = UMLGenerationUtils.getUMLOperations(block.getFunctions(), block.getFunctionDecls());
        List<String> extendsParents = UMLGenerationUtils.getUMLExtendsParents(srcMLInterface.getSuperLink());
        List<String> implementsParents = UMLGenerationUtils.getUMLImplementsParents(srcMLInterface.getSuperLink());
        UMLInterface umlInterface = new UMLInterface(srcMLInterface.getName(), residingPackage, imports, operations, extendsParents, implementsParents);
        return umlInterface;
    }
    public static UMLClass getUMLEnum(SrcMLEnum srcMLEnum, List<String> residingPackage, List<List<String>> imports){
        SrcMLBlock block = srcMLEnum.getBlock();
        List<UMLAttribute> attributes = UMLGenerationUtils.getUMLAttributes(block);
        List<UMLOperation> operations = UMLGenerationUtils.getUMLOperations(block.getFunctions(), block.getFunctionDecls());
        List<UMLOperation> constructors = UMLGenerationUtils.getUMLConstructors(block.getConstructors(), block.getConstructors_decl());

        //null for extends parents because enums can't have extends parents.. while then can have implements (i think), srcml doesn't support
        //that as far as I can tell.
        UMLClass umlEnum = new UMLClass(srcMLEnum.getName(), residingPackage, imports, attributes, operations, constructors, false, null, null,"enum");
        return umlEnum;
    }

    public static List<UMLMessage> getUMLMessages(SrcMLBlock block){
        //from a block I have both expr_stmt and expr; expr leads to call
        List<UMLMessage> messages = new ArrayList<>();
        List<SrcMLExpression> expressions = new ArrayList<>();
        for (SrcMLBlock.SrcMLExprStmt exprStmt : block.getExpr_stmts()){
            for (SrcMLExpression expression : exprStmt.getExpressions()){
                expressions.add(expression);
            }
        }
        for (SrcMLExpression expression : expressions){
            for (SrcMLCall call : expression.getCalls()){
                messages.add(getUMLMessage(call));
            }
        }
        return messages;
    }
    public static UMLMessage getUMLMessage(SrcMLCall call){
        String callName = call.getName();


        //need to deal with argument lists here. the good news is that I only have 1 call in a <call>..



        UMLMessage message = new UMLMessage(null, null, null, false);

        return message;
    }

}
