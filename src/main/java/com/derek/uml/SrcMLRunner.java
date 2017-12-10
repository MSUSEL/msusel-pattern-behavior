package com.derek.uml;

import com.derek.Main;
import com.derek.uml.plantUml.PlantUMLTransformer;
import javafx.util.Pair;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.management.relation.Relation;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * This class runs the srcML tool  (http://www.srcml.org/#home) on a software project.
 */
public class SrcMLRunner {

    private final boolean storeSrcML = true;
    private String projectWorkingDirectory;
    private final String xmlSpecifier = "specifier";
    private final String xmlName = "name";
    private UMLClassDiagram umlClassDiagram;

    public SrcMLRunner(String projectWorkingDirectory){
        this.projectWorkingDirectory = projectWorkingDirectory;
        generateSrcML();

        umlClassDiagram = new UMLClassDiagram();

        //selenium test
        buildAllClasses();
        buildAllRelationships();

        //guava test
        //buildClassDiagram(new File("srcMLOutput/guava13/Files.xml"));

        PlantUMLTransformer pltTransformer = new PlantUMLTransformer(umlClassDiagram);
        //used to print plantuml
        //pltTransformer.generateClassDiagram();
    }

    private void buildAllRelationships(){
        for (UMLClassifier umlClassifier : umlClassDiagram.getClassDiagram().nodes()){
            for (UMLAttribute umlAttribute : umlClassifier.getAttributes()){
                String type = umlAttribute.getDataType();
                if (isTypePrimitive(type)){
                    //don't care; primitive type
                }else{
                    UMLClassifier dataType;
                    if ((dataType = isTypeIncludedInProject(type)) != null){
                        //the type is a class within the project.
                        System.out.println("type was found!: " + type);
                        Relationship relationship = getRelationshipFromUMLClassifiers(umlClassifier, dataType);
                        umlClassDiagram.addRelationshipToDiagram(umlClassifier, dataType, relationship);
                    }else{
                        //the type is not a class in the project. Such as 'Integer', coming from a java lib
                        //could also be a 3rd party library I don't have the source for.
                        System.out.println("type not found: " + type);
                    }
                }
            }
            for (UMLOperation umlOperation : umlClassifier.getOperations()){
                //TODO
            }
        }
    }

    private Relationship getRelationshipFromUMLClassifiers(UMLClassifier umlClassifier, UMLClassifier dataType){
        Relationship r = Relationship.UNSPECIFIED;

        return r;
    }

    //method returns a umlclassifier object in the uml class diagram graph if the type is found to match it.
    //if a match is not found return null.
    private UMLClassifier isTypeIncludedInProject(String type){
        for (UMLClassifier umlClassifier : umlClassDiagram.getClassDiagram().nodes()){
            if (umlClassifier.getName().equals(type)){
                //found a match!
                return umlClassifier;
            }
        }
        return null;
    }

    //returns true if the type is a primitive type (including Strring)
    //flase otherwise
    private boolean isTypePrimitive(String type){
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

    private void buildAllClasses(){
        //https://stackoverflow.com/questions/5694385/getting-the-filenames-of-all-files-in-a-folder
        File f = new File("srcMLOutput/selenium36/");
        File[] listOfFiles = f.listFiles();
        for (int i = 0; i < listOfFiles.length; i++) {
            buildClassDiagram(listOfFiles[i]);
        }
    }

    //this method runs through the raw srcMLOutput, which is an xml file, and builds each class using uml
    private void buildClassDiagram(File xmlFile){
        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(xmlFile);
            //http://www.mkyong.com/java/how-to-read-xml-file-in-java-dom-parser/

            //get all classes (note this includes abstract classes, but not interfaces
            NodeList srcClasses = doc.getElementsByTagName("class");
            for (int i = 0; i < srcClasses.getLength(); i++) {
                Node classIter = srcClasses.item(i);
                if (classIter.getNodeType() == Node.ELEMENT_NODE){
                    //this should always happen
                    Element className = (Element) classIter;

                    String classNameVal = getClassifierNameFromXml(className, ClassifierName.CLASS);
                    List<UMLAttribute> attributes = getAttributesFromXml(className);
                    boolean isClassAbstract = isClassAbstract(className);
                    List<UMLOperation> operations = getOperationsFromXml(className, "function");
                    if (isClassAbstract){
                        //abstract classes can have both fucntion declarations and functions - so I need to add the function_decls
                        for (UMLOperation abstractFunc : getOperationsFromXml(className, "function_decl")){
                            operations.add(abstractFunc);
                        }
                    }
                    List<UMLOperation> constructors = getConstructorsFromXml(className);

                    //is abstract is false for now. will change in future.
                    UMLClass classToAdd = new UMLClass(classNameVal, attributes, operations, constructors, isClassAbstract);

                    umlClassDiagram.addClassToDiagram(classToAdd);
                }
            }
            NodeList srcInterfaces = doc.getElementsByTagName("interface");
            for (int i = 0; i < srcInterfaces.getLength(); i++){
                Node interfaceIter = srcInterfaces.item(i);
                if (interfaceIter.getNodeType() == Node.ELEMENT_NODE){
                    //should always happen
                    Element interfaceEle = (Element) interfaceIter;
                    String interfaceName = getClassifierNameFromXml(interfaceEle, ClassifierName.INTERFACE);

                    List<UMLOperation> operations = getOperationsFromXml(interfaceEle, "function_decl");

                    UMLInterface interfaceToAdd = new UMLInterface(interfaceName, operations);
                    umlClassDiagram.addClassToDiagram(interfaceToAdd);
                }
            }

        }catch(Exception e){
            System.out.println("CAUGHT ERROR IN BUILDXML   " + xmlFile.getName());
            e.printStackTrace();
            System.exit(0);
        }
    }
    private boolean isClassAbstract(Element classEle){
        boolean toRet = false;
        NodeList classSpecifiers = classEle.getElementsByTagName(xmlSpecifier);
        for (int i = 0; i < classSpecifiers.getLength(); i++){
            Node classSpecifierNode = classSpecifiers.item(i);
            if (classSpecifierNode.getNodeType() == Node.ELEMENT_NODE){
                //should always happen
                Element classSpecEle = (Element) classSpecifierNode;
                if (classSpecEle.getTextContent().equals("abstract")){
                    //found an abstract class
                    toRet = true;
                    return toRet;
                }
            }
        }
        return toRet;
    }

    //method that iterates through all 'name' elements and returns the name of a classifier - must provide classifier.
    private String getClassifierNameFromXml(Element className, ClassifierName classifierName){
        NodeList names = className.getElementsByTagName(xmlName);

        for (int i = 0; i < names.getLength(); i++){
            Node name = names.item(i);
            switch (classifierName){
                case CLASS:
                    if (nameIsClassName(name, classifierName)){
                        //name is a class name
                        return ((Element)name).getTextContent();
                    }
                case INTERFACE:
                    if (nameIsClassName(name, classifierName)){
                        return ((Element)name).getTextContent();
                    }
            }

        }
        return "class name not found";
    }

    //utility class to get the string value from an xml element. Supply the parent element, the name of the element you are looking for, and the index for
    //which numbered child you want
    private String getTextFromXmlElement(Element element, String childName, int index){
        Node toRet = element.getElementsByTagName(childName).item(index);
        if (toRet == null){
            //node did not exist
            return "";
        }else {
            return toRet.getTextContent();
        }
    }


    //special and most common case for getting text from xml elements.
    private String getFirstTextFromXmlElement(Element element, String childName){
        return getTextFromXmlElement(element, childName, 0);
    }

    private List<UMLOperation> getConstructorsFromXml(Element className){
        List<UMLOperation> toRet = new ArrayList<>();
        NodeList constructorNodes = className.getElementsByTagName("constructor");

        for (int i = 0; i < constructorNodes.getLength(); i++){
            Node constructorNode = constructorNodes.item(i);
            if (notNestedClass(className, constructorNode)){
                if (constructorNode.getNodeType() == Node.ELEMENT_NODE){
                    //always happens, conditional is here for safety
                    Element constructor = (Element) constructorNode;
                    Visibility vis = getVisibilityFromSpecifier(getFirstTextFromXmlElement(constructor, xmlSpecifier));
                    String name = getFirstTextFromXmlElement(constructor, xmlName);
                    List<Pair<String, String>> params = getParamsFromXml(constructor);
                    System.out.println("Adding constructor:" + vis + "    " + name);
                    for (Pair<String, String> p : params){
                        System.out.println("params: " + p.getKey() + "   " + p.getValue());
                    }
                    toRet.add(new UMLOperation(name, params, "constructor", vis));
                }
            }
        }
        return toRet;
    }

    //method to get the list of functions from a class or interface
    //search string shoudl be 'function' in the case of classes or
    //'function_decl' in the case of interfaces
    private List<UMLOperation> getOperationsFromXml(Element className, String searchString){
        List<UMLOperation> toRet = new ArrayList<>();
        //classes have functions, interfaces have function_decl
        NodeList operationsNodes = className.getElementsByTagName(searchString);

        for (int i = 0; i < operationsNodes.getLength(); i++){
            Node operationNode = operationsNodes.item(i);
            if (notNestedClass(className, operationNode)){
                if (operationNode.getNodeType() == Node.ELEMENT_NODE){
                    //always should happen, conditional for safety
                    Element operation = (Element)operationNode;
                    //functions/methods start with a visibility

                    //the bug is that methods do not need a visibility.
                    Visibility vis = getVisibilityFromSpecifier(getFirstTextFromXmlElement(operation, xmlSpecifier));
                    int indexerForAnnotations = 0;
                    while (operation.getElementsByTagName(xmlName).item(indexerForAnnotations).getParentNode().getNodeName().equals("annotation")){
                        indexerForAnnotations++;
                        //found at least one annotation - the 'name' field is being picked up by the annotation element. Need to skip.
                        //see below comment for explanation
                        //bug here with override annotation - name is picking up 'Override'. To fix I can check to see if
                        //the name's parent node in the xml is an annotation or not
                    }
                    //first 'name' is return type
                    String retType = getTextFromXmlElement(operation, xmlName, indexerForAnnotations);

                    while (!operation.getElementsByTagName(xmlName).item(indexerForAnnotations).getParentNode().getNodeName().equals(searchString)){
                        indexerForAnnotations++;
                        //found at least one type - the 'name' field is being picked up by the type element. Need to skip.
                        //same issue as annotations, listed above... Although because the xml is poorly formed, and there are two name elements
                        //stacked, this time I do it until the immediate parent is 'function'
                    }

                    //second 'name' is actual method name
                    String name = getTextFromXmlElement(operation, xmlName, indexerForAnnotations);

                    List<Pair<String, String>> params = getParamsFromXml(operation);

                    System.out.println("Adding uml operations, " + vis + "    " + retType + "    " + name);
                    for (Pair<String, String> p : params){
                        System.out.println("\tparams: " + p.getKey() + "   " + p.getValue());
                    }
                    toRet.add(new UMLOperation(name, params, retType, vis));
                }
            }
        }
        return toRet;
    }

    private List<Pair<String, String>> getParamsFromXml(Element operation){
        try {
            //pair being a param, key being data type value being variable name
            List<Pair<String, String>> toRet = new ArrayList<>();

            NodeList paramNodeList = operation.getElementsByTagName("parameter_list");
            for (int i = 0; i < paramNodeList.getLength(); i++) {
                Node paramListNode = paramNodeList.item(i);
                //check to see if we are dealing with a real param or a special case (such as lambda expression or try/catch)
                if (paramCatchCases(paramListNode)){
                    if (paramListNode.getNodeType() == Node.ELEMENT_NODE) {
                        //should always happen
                        Element paramListEle = (Element) paramListNode;

                        NodeList params = paramListEle.getElementsByTagName("parameter");
                        for (int j = 0; j < params.getLength(); j++){
                            Node paramNode = params.item(j);
                            if (paramNode.getNodeType() == Node.ELEMENT_NODE){
                                //once again, should always happen
                                 Element paramEle = (Element) paramNode;
                                if (paramEle.getElementsByTagName(xmlName).item(0) == null) {
                                    //params can be empty '()'
                                    toRet.add(new Pair<>("", ""));
                                } else {
                                    Pair<String, String> p = new Pair<>(getFirstTextFromXmlElement(paramEle, xmlName), getTextFromXmlElement(paramEle, xmlName, 1));
                                    toRet.add(p);
                                }
                            }
                        }

                    }
                }
            }
            return toRet;
        }catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }


    //this method lists a number of cases that params might appear with.
    //if a case is found, we return false.
    private boolean paramCatchCases(Node paramNode){
        boolean paramIsGood = true;
        if (paramNode.hasAttributes()){
            if (paramNode.getAttributes().item(0).getTextContent().equals("generic")){
                //variable declaration with generics
                paramIsGood = false;
            }
            if (paramNode.getAttributes().item(0).getTextContent().equals("pseudo")){
                //lambda expression
                paramIsGood = false;
            }
        }
        if (paramNode.getParentNode().getNodeName().equals("catch")){
            //found a catch block
            paramIsGood = false;
        }
        if (paramNode.getParentNode().getNodeName().equals("lambda")){
            paramIsGood = false;
        }
        return paramIsGood;
    }

    //checker method to see if a given statementNode has class name of className.
    //otherwise the statementNode is a nested under a different className and should not be considered for this className.
    private boolean notNestedClass(Element classElement, Node statementNode){
        //assume it is not a nested class, and prove otherwise
        boolean isNotNestedClass = false;

        //walk up statement till i hit the first class
        Node myParent = getImmediateParentNode(statementNode);
        if (myParent.getNodeType() == Node.ELEMENT_NODE) {
            //this shoudl always happen
            Element parent = (Element)myParent;
            //check if class name = className
            String parentName = getClassifierNameFromXml(parent, ClassifierName.CLASS);
            String className = getClassifierNameFromXml(classElement, ClassifierName.CLASS);
            if (parentName.equals(className)){
                isNotNestedClass = true;
            }
        }
        return isNotNestedClass;
    }

    //recursive method to walk up dom tree until the first class node is reached. Should alwasy eventually hit class.
    //if I get an error here, put a condition making sure walker doesn't go outside the '#document' element.
    private Node getImmediateParentNode(Node walker){
        if (walker.getNodeName().equals("class") || walker.getNodeName().equals("interface")){
            return walker;
        }else{
            return getImmediateParentNode(walker.getParentNode());
        }
    }

    private List<UMLAttribute> getAttributesFromXml(Element className){
        List<UMLAttribute> toRet = new ArrayList<>();

        //variables are declared using the xml element 'decl_stmt'
        NodeList declaredStatements = className.getElementsByTagName("decl_stmt");
        for (int i = 0; i < declaredStatements.getLength(); i++){
            Node statementNode = declaredStatements.item(i);

            if (notConstructorOrFunction(statementNode) && notNestedClass(className, statementNode)){
                if (statementNode.getNodeType() == Node.ELEMENT_NODE){
                    //this shoudl always happen
                    Element statement = (Element)statementNode;
                    Visibility vis = getVisibilityFromSpecifier(getFirstTextFromXmlElement(statement, xmlSpecifier));

                    //I believe there should only ever be one typeDec, so I am just using the first item in the nodelist
                    Node typeDec = statement.getElementsByTagName("type").item(0);
                    String dataType = "";
                    if (typeDec.getNodeType() == Node.ELEMENT_NODE){
                        //should always be, but putting the conditional for safety
                        Element type = (Element) typeDec;
                        //should only ever be one name.
                        dataType = getFirstTextFromXmlElement(type, xmlName);
                    }

                    //should always be last item in nodelist of names..
                    String varName = getTextFromXmlElement(statement, xmlName, 1);
                    System.out.println("Adding uml attribute, " + vis + "    " + dataType + "    " + varName);

                    toRet.add(new UMLAttribute(varName, dataType, vis));
                }
            }
        }
        return toRet;
    }

    //recursive function that walks up the dom for a given node, for the goal of finding a 'name' element that
    //corresponds to the class (or interface) name. Because of the poorly-formed xml structure, annotations are given
    //the value of name as well
    private boolean nameIsClassName(Node classNode, ClassifierName classifierName){
        if (classNode.getNodeName().equals("annotation")){
            //base case, name refers to an annotation
            return false;
        }else
            //not base case (obv)
            switch(classifierName){
                case CLASS:
                    if (classNode.getNodeName().equals("class")){
                        return true;
                    }
                case INTERFACE:
                    if (classNode.getNodeName().equals("interface")){
                        return true;
                    }
                default: return nameIsClassName(classNode.getParentNode(), classifierName);
        }
    }

    //recursive function that walks up the dom for a given node in the xml, eventually returning false if a
    //function or constructor was found, otherwise returning true when the document root is found.
   private boolean notConstructorOrFunction(Node statementNode){
        if (statementNode.getNodeName().equals("function") || statementNode.getNodeName().equals("constructor")){
            //hit a function or constructor in the dom tree
            return false;
        }else if (statementNode.getNodeName().equals("#document")){
            //hit parent of doc without finding function or constructor.
            return true;
        }else{
            return notConstructorOrFunction(statementNode.getParentNode());
        }
    }

    private Visibility getVisibilityFromSpecifier(String specifier){
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

    private boolean isClassInterfaceSpecifier(String s){
        //this method returns true if the string coming in is a class or interface keyword.
        switch(s){
            case "class":
            case "interface":
                return true;
        }
        return false;
    }

    public List<Path> getSourceCodeListFromProject(){
        try {
            //https://stackoverflow.com/questions/2534632/list-all-files-from-a-directory-recursively-with-java
            List<Path> toRet = Files.find(Paths.get(projectWorkingDirectory), 999, (p, bfa) -> bfa.isRegularFile() && p.getFileName().toString().matches(".*\\.java")).collect(Collectors.toList());
            return toRet;
        }catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public void generateSrcML(){
        try{
            System.out.println("Starting srcML generation");
            //https://stackoverflow.com/questions/5604698/java-programming-call-an-exe-from-java-and-passing-parameters

            List<Path> pathsAsPath = getSourceCodeListFromProject();
            File mainDirectory = new File("srcMLOutput\\");
            if (!mainDirectory.exists()){
                Files.createDirectory(Paths.get("srcMLOutput\\"));
            }

            //change directory in future.
            File directory = new File("srcMLOutput\\" + Main.projectID + Main.testProject + "\\");
            if (!directory.exists()) {
                Files.createDirectory(Paths.get("srcMLOutput\\"+ Main.projectID + Main.testProject + "\\"));
            }

            for (Path p : pathsAsPath) {
                String current = quotify(p.toString());

                Runtime rt = Runtime.getRuntime();
                String[] commands = {"srcML ", current};
                Process proc = rt.exec(commands);
                BufferedReader stdInput = new BufferedReader(new InputStreamReader(proc.getInputStream()));

                String srcMLOut = "";
                String s = "";
                if (storeSrcML) {
                    //take out .java extension and add .xml extension

                    File fout = new File("srcMLOutput\\" + directory.getName() + "\\" + p.getFileName().toString().split(".java")[0] + ".xml");
                    if (!fout.exists()) {
                        BufferedWriter bf = new BufferedWriter(new FileWriter(fout));
                        while ((s = stdInput.readLine()) != null) {
                            srcMLOut += s;
                            bf.write(s);
                        }
                        bf.close();
                    }
                }
                proc.destroy();
            }
            System.out.println("Ending srcML generation");
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    private String quotify(String s){
        s = "\"" + s + "\"";
        s = s.replace("/", "\\") ;
        return s;
    }
}
