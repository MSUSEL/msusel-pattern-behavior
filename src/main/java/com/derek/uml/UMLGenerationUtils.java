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
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

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

    public static List<SrcMLDecl> getAllDeclsBelowMe(SrcMLNode node){
        List<SrcMLDecl> decls = new ArrayList<>();
        for (SrcMLNode child : node.getChildNodeOrder()){
            if (child instanceof SrcMLDecl){
                decls.add((SrcMLDecl) child);
            }
            decls.addAll(getAllDeclsBelowMe(child));
        }
        return decls;
    }

    public static List<UMLAttribute> getUMLAttributes(SrcMLBlock block){
        List<UMLAttribute> attributes = new ArrayList<>();
        //first conditional happens in special cases (enums with both decls and decl_stmts)
        if (block.getDecls().size() > 0){
            //dealing an enum (list of decls)
            List<SrcMLDecl> decls = block.getDecls();
            for (SrcMLDecl decl : decls) {
                UMLAttribute toAdd = new UMLAttribute(decl.getName(), decl.getName());
                //in this case the type is the same as the name... I think.. lol
                if (decl.getInit() != null){
                    CallTreeNode<SrcMLNode> callTreeSrcML = decl.getCallTree();
                    //call tree is a string here. I will make it a call tree of umlClassifiers in the 4th pass.
                    toAdd.setInstantiation((UMLMessageGenerationUtils.convertSrcMLCallTreeToString(callTreeSrcML)));
                }
                attributes.add(toAdd);
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
                    UMLAttribute toAdd = new UMLAttribute(decl.getName(), decl.getType().getName());
                    //in this case the type is the same as the name... I think.. lol
                    if (decl.getInit() != null){
                        CallTreeNode<SrcMLNode> callTreeSrcML = decl.getCallTree();
                        //call tree is a string here. I will make it a call tree of umlClassifiers in the 4th pass.
                        toAdd.setInstantiation((UMLMessageGenerationUtils.convertSrcMLCallTreeToString(callTreeSrcML)));
                    }
                    attributes.add(toAdd);
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
        for (SrcMLParameter p : parameterList.getParameters()){
            if (p.getDecl() != null){
                //reference to comment in SrcMLParameter class
                params.add(new ImmutablePair<>(p.getDecl().getType().getName(), p.getDecl().getName()));
            }
        }
        return params;
    }

    public static List<UMLAttribute> getLocalUMLAttributeDecls(SrcMLBlock srcMLBlock){
        List<UMLAttribute> localAtts = new ArrayList<>();
        List<SrcMLDecl> decls = getAllDeclsBelowMe(srcMLBlock);

        for (SrcMLDecl decl : decls){
            //decl.getType might be null if the variable declaration is a lambda expression where the right hand size of the lambda
            //points to a variable that hasn't been declared yet.
            //im just ignoring this for now.
            if (decl.getType() != null) {
                UMLAttribute toAdd = new UMLAttribute(decl.getName(), decl.getType().getName());
                if (decl.getInit() != null) {
                    CallTreeNode<SrcMLNode> callTreeSrcML = decl.getCallTree();
                    //call tree is a string here. I will make it a call tree of umlClassifiers in the 4th pass.
                    toAdd.setInstantiation((UMLMessageGenerationUtils.convertSrcMLCallTreeToString(callTreeSrcML)));
                }
                localAtts.add(toAdd);
            }
        }
        return localAtts;
    }

    public static void setUMLOperationLocalVariableUsagesAndDecls(UMLOperation umlOperation, SrcMLBlock srcMLBlock){
        if (srcMLBlock != null) {
            //happens when this is an interface or abstract declaration. Of course such a type will not have any attributes, so skip it.
            List<UMLAttribute> localAttDecls = getLocalUMLAttributeDecls(srcMLBlock);
            umlOperation.setLocalVariableDecls(localAttDecls);
        }
    }

    public static UMLOperation getUMLOperation(SrcMLFunction srcMLFunction){
        List<Pair<String, String>> params = UMLGenerationUtils.getParameters(srcMLFunction.getParameterList());
        String name = srcMLFunction.getName();
        String returnType = srcMLFunction.getType().getName();

        //create toreturn object.. Though I need to see if there are any local attributes before returning it.
        UMLOperation toRet = new UMLOperation(name, params, returnType);
        setUMLOperationLocalVariableUsagesAndDecls(toRet, srcMLFunction.getBlock());
        return toRet;
    }

    public static UMLOperation getUMLConstructor(SrcMLConstructor srcMLConstructor){
        List<Pair<String, String>> params = UMLGenerationUtils.getParameters(srcMLConstructor.getParameterList());
        String name = srcMLConstructor.getName();
        //constructors don't have return types
        String returnType = "null";
        //I need to include use dependencies in here eventually. -- see comment above for umloperation
        UMLOperation toRet = new UMLOperation(name, params, returnType);
        setUMLOperationLocalVariableUsagesAndDecls(toRet, srcMLConstructor.getBlock());
        return toRet;
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
            UMLOperation operation = getUMLOperation(function);

            //this code is put here because function_decls don't ahve behaviors, but I want structural umloperations
            //being instantiated the same way for functions and function_decls
            CallTreeNode<SrcMLNode> callTreeSrcML = function.getCallTree();
            //call tree is a string here. I will make it a call tree of umlClassifiers in the 4th pass.
            operation.setCallTreeString(UMLMessageGenerationUtils.convertSrcMLCallTreeToString(callTreeSrcML));
            operations.add(operation);
        }
        for (SrcMLFunction function : functionDecls){
            operations.add(getUMLOperation(function));
        }
        return operations;
    }

    public static List<UMLOperation> getUMLConstructors(List<SrcMLConstructor> constructorsList, List<SrcMLConstructor> constructorDecls){
        List<UMLOperation> constructors = new ArrayList<>();
        for (SrcMLConstructor constructor : constructorsList){

            UMLOperation constructor2 = getUMLConstructor(constructor);
            //see comment above for functions -- constructor decls will not have behavior.
            CallTreeNode<SrcMLNode> callTreeSrcML = constructor.getCallTree();
            //again, call tree is a string here.

            if (constructor.getBlock() != null){
                //another srcml bug here. This bug can be seen in selenium3.6-Keys.java.. the LEFT_SHIFT enum type thinks 'Keys.Left' is a
                //constructor, when in fact it is a variable instantiation in the enum. Because of this I need to insert a
                // bizarre 'if block = null'
                constructor2.setCallTreeString(UMLMessageGenerationUtils.convertSrcMLCallTreeToString(callTreeSrcML));
            }
            constructors.add(constructor2);
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
        String fullName[] = residingPackage.get(0).split("\\.");
        List<String> packageAsList = new ArrayList<>();
        for (int i = 0; i < fullName.length; i++){
            packageAsList.add(i, fullName[i]);
        }
        return packageAsList;
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
        assignOperationOwners(operations, umlClass);
        assignOperationOwners(constructors, umlClass);
        assignAttributeOwners(attributes, umlClass);

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
        UMLInterface umlInterface = new UMLInterface(srcMLInterface.getName(), residingPackage, imports, operations, extendsParents, implementsParents, "interface");
        List<UMLAttribute> attributes = UMLGenerationUtils.getUMLAttributes(block);
        if (!attributes.isEmpty()){
            umlInterface.setAttributes(attributes);
        }
        assignOperationOwners(operations, umlInterface);
        return umlInterface;
    }
    public static UMLClass getUMLEnum(SrcMLEnum srcMLEnum, List<String> residingPackage, List<List<String>> imports){
        SrcMLBlock block = srcMLEnum.getBlock();
        List<UMLAttribute> attributes = UMLGenerationUtils.getUMLAttributes(block);
        List<UMLOperation> operations = UMLGenerationUtils.getUMLOperations(block.getFunctions(), block.getFunctionDecls());
        List<UMLOperation> constructors = UMLGenerationUtils.getUMLConstructors(block.getConstructors(), block.getConstructors_decl());

        //null for extends parents because enums can't have extends parents.. while then can have implements (i think), srcml doesn't support
        //that as far as I can tell.
        UMLClass umlEnum = new UMLClass(srcMLEnum.getName(), residingPackage, imports, attributes, operations, constructors, false, new ArrayList<>(), new ArrayList<>(),"enum");
        assignOperationOwners(operations, umlEnum);
        assignOperationOwners(constructors, umlEnum);
        assignAttributeOwners(attributes, umlEnum);
        return umlEnum;
    }

    /***
     * method responsible for assigning an owning classifier to each method within itself.
     * This is purely to help with my code later on, and is not needed in the uml metamodel. However, its a pain to track uml operations without knowledge of their owning class
     * @param operations
     * @param umlClassifier
     */
    private static void assignOperationOwners(List<UMLOperation> operations, UMLClassifier umlClassifier){
        for (UMLOperation umlOperation : operations){
            umlOperation.setOwningClassifier(umlClassifier);
        }
    }

    /***
     * Same as abocve, but for attributes.
     * @param attributes
     * @param umlClassifier
     */
    private static void assignAttributeOwners(List<UMLAttribute> attributes, UMLClassifier umlClassifier){
        for (UMLAttribute umlAttribute : attributes){
            umlAttribute.setOwningClassifier(umlClassifier);
        }
    }



}
