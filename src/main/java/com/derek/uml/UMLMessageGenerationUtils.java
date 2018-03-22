package com.derek.uml;

import com.derek.uml.srcML.*;
import com.google.common.graph.MutableGraph;

import java.util.ArrayList;
import java.util.List;

public class UMLMessageGenerationUtils {

    public static CallTreeNode<String> convertSrcMLCallTreeToString(CallTreeNode<SrcMLNode> callTree){
        CallTreeNode<String> asString = new CallTreeNode<>(callTree.getName().toString(), callTree.getTagName());
        for (CallTreeNode<SrcMLNode> child : callTree.getChildren()){
            asString.addChild(convertSrcMLCallTreeToString(child));
        }
        return asString;
    }

    public static UMLClassifier getUMLClassifierFromStringType(UMLClassDiagram umlClassDiagram, UMLClassifier initialScope, String searchString) {
        if (searchString.contains("<")) {
            //generic
            searchString = getLastType(searchString);
        }
        if (searchString.contains(".")) {
            //type is directly linked to a package
            //this is an easy one
            List<String> searchStrings = listify(searchString);
            UMLClassifier importer = umlClassDiagram.getPackageTree().getClassifier(searchStrings, 0, umlClassDiagram.getPackageTree().getRoot());
            if (importer != null) {
                //will be null if package points to a 3rd party lib.
                if (searchStrings.get(searchStrings.size() - 1).equals(importer.getName())) {
                    //finally found match.
                    return importer;
                }
            } else {
                //third part type.
                return null;
            }
        }
        //check classifier's vars
        if (searchString.equals(initialScope.getName())) {
            //var is of this class type
            return initialScope;
        }

        for (UMLClassifier parent : initialScope.getExtendsParents()) {
            //var is from a parent class
            if (searchString.equals(parent.getName())) {
                return parent;
            }
        }
        for (UMLClassifier parent : initialScope.getImplementsParents()) {
            //var is from a parent implementing interface
            if (searchString.equals(parent.getName())) {
                return parent;
            }
        }
        for (UMLClassifier langType : UMLGenerator.languageTypes.getDataTypes()) {
            if (searchString.equals(langType.getName())) {
                return langType;
            }
        }
        for (UMLClassifier sibling : umlClassDiagram.getPackageTree().getLevelOfClassifier(initialScope).getClassifiers()) {
            //check siblings at same package level for type match

            if (searchString.equals(sibling.getName())) {
                return sibling;
            }
        }


        //look through imports now
        for (List<String> singleImport : initialScope.getImports()) {
            if (singleImport.get(singleImport.size() - 1).equals("*")) {
                //catch-all for imports, will return a list of umlClassifiers.
                List<UMLClassifier> packageClassifiers = umlClassDiagram.getPackageTree().getClassifiersFromImport(listify(singleImport.get(0)), 0, umlClassDiagram.getPackageTree().getRoot());
                for (UMLClassifier packageClassifier : packageClassifiers) {
                    if (searchString.equals(packageClassifier.getName())) {
                        //finally found match.
                        return packageClassifier;
                    }
                }
            } else {
                UMLClassifier importer = umlClassDiagram.getPackageTree().getClassifier(listify(singleImport.get(0)), 0, umlClassDiagram.getPackageTree().getRoot());
                if (importer != null) {
                    //will be null if package points to a 3rd party lib.
                    if (searchString.equals(importer.getName())) {
                        //finally found match.
                        return importer;
                    }
                }
            }
        }
        //do a direct match on imports to see if we can at least find the same type.
        for (List<String> singleImport : initialScope.getImports()) {
            List<String> realImport = listify(singleImport.get(0));
            if (realImport.get(realImport.size() - 1).equals(searchString)) {
                UMLClassifier thirdPartyClassifier = new UMLClass(searchString, realImport, null, null, null, null, false, null, null, "thirdPartyClass");
                return thirdPartyClassifier;
            }
        }

        //if we get here we really fucked up.
        UMLClassifier fuckUp = new UMLClass(searchString, null, null, null, null, null, false, null, null, "unknown");
        return fuckUp;
    }

    public static List<String> typeSplitter(String type){
        //because I have generics (foo<bar>), combo generics (foo<bar1,bar2>), and ownership (foo.bar)
        List<String> types = new ArrayList<>();

        //initial case
        String[] splitter = type.split("<");
        types.add(splitter[0]);
        for (int i = 1; i < splitter.length; i++){
            String segment = splitter[i].replaceAll(">", "");
            String[] segmentSplitter = segment.split(",");
            types.add(segmentSplitter[0]);
            for (int j = 1; j < segmentSplitter.length; j++){
                types.add(segmentSplitter[j]);
            }
        }
        return types;
    }

    public static String getLastType(String type){
        List<String> types = typeSplitter(type);
        return types.get(types.size()-1);
    }

    public static List<String> listify(String stringToList){
        String[] splitter = stringToList.split("\\.");
        List<String> toRet = new ArrayList<>();
        for (String s : splitter){
            toRet.add(s);
        }
        return toRet;
    }

    //returns true if the type is a primitive type
    //flase otherwise
    public static boolean isPrimitiveType(String type){
        switch(type){
            case "boolean":
            case "byte":
            case "char":
            case "short":
            case "int":
            case "long":
            case "float":
            case "double":
                return true;
        }
        return false;
    }

}
