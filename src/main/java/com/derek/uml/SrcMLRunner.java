package com.derek.uml;

import com.derek.Main;
import javafx.util.Pair;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

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
        //buildClassDiagram(new File("srcMLOutput/selenium36/Duration.xml"));

        //guava test
        buildClassDiagram(new File("srcMLOutput/guava13/Files.xml"));
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

                    List<UMLAttribute> attributes = getAttributesFromXml(className);
                    List<UMLOperation> operations = getOperationsFromXml(className);
                    List<Constructor> constructors = getConstructorsFromXml(className);

                    NodeList specifiers = className.getElementsByTagName(xmlSpecifier);
                    String classNameVal = getClassNameFromXml(className);

                    //is abstract is false for now. will change in future.
                    UMLClass classToAdd = new UMLClass(classNameVal, attributes, operations, constructors, false);

                    umlClassDiagram.addClassToDiagram(classToAdd);
                }
            }
            //TODO - interfaces
            NodeList srcInterfaces = doc.getElementsByTagName("interface");

        }catch(Exception e){
            e.printStackTrace();
        }
    }

    private String getClassNameFromXml(Element className){
        NodeList names = className.getElementsByTagName(xmlName);

        for (int i = 0; i < names.getLength(); i++){
            Node name = names.item(i);
            if (nameIsClassName(name)){
                //name is a class name
                return ((Element)name).getTextContent();
            }
        }
        return "not found";
    }

    //utility class to get the string value from an xml element. Supply the parent element, the name of the element you are looking for, and the index for
    //which numbered child you want
    private String getTextFromXmlElement(Element element, String childName, int index){
        return element.getElementsByTagName(childName).item(index).getTextContent();
    }

    //special and most common case for getting text from xml elements.
    private String getFirstTextFromXmlElement(Element element, String childName){
        return getTextFromXmlElement(element, childName, 0);
    }

    private List<Constructor> getConstructorsFromXml(Element className){
        List<Constructor> toRet = new ArrayList<>();
        NodeList constructorNodes = className.getElementsByTagName("constructor");

        for (int i = 0; i < constructorNodes.getLength(); i++){
            Node constructorNode = constructorNodes.item(i);
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
                toRet.add(new Constructor(name, params, vis));
            }
        }

        return toRet;
    }

    private List<UMLOperation> getOperationsFromXml(Element className){
        List<UMLOperation> toRet = new ArrayList<>();

        NodeList operationsNodes = className.getElementsByTagName("function");

        for (int i = 0; i < operationsNodes.getLength(); i++){
            Node operationNode = operationsNodes.item(i);
            if (operationNode.getNodeType() == Node.ELEMENT_NODE){
                //always should happen, conditional for safety
                Element operation = (Element)operationNode;
                //functions/methods start with a visibility
                Visibility vis = getVisibilityFromSpecifier(getFirstTextFromXmlElement(operation, xmlSpecifier));
                //first 'name' is return type
                String retType = getFirstTextFromXmlElement(operation, xmlName);
                //second 'name' is actual method name
                String name = getTextFromXmlElement(operation, xmlName, 1);
                List<Pair<String, String>> params = getParamsFromXml(operation);

                System.out.println("Adding uml operations, " + vis + "    " + retType + "    " + name);
                for (Pair<String, String> p : params){
                    System.out.println("\tparams: " + p.getKey() + "   " + p.getValue());
                }
                toRet.add(new UMLOperation(name, params, retType, vis));
            }


        }


        return toRet;
    }

    private List<Pair<String, String>> getParamsFromXml(Element operation){
        //pair being a param, key being data type value being variable name
        List<Pair<String, String>> toRet = new ArrayList<>();

        NodeList paramNodeList = operation.getElementsByTagName("parameter_list");
        for (int i = 0; i < paramNodeList.getLength(); i++){
            Node paramNode = paramNodeList.item(i);
            if (!paramNode.hasAttributes() || (paramNode.hasAttributes() && !paramNode.getAttributes().item(0).getTextContent().equals("generic"))){
                if (paramNode.getNodeType() == Node.ELEMENT_NODE) {
                    //should always happen
                    Element param = (Element) paramNode;

                    if (param.getElementsByTagName(xmlName).item(0) == null) {
                        //params can be empty '()'
                        toRet.add(new Pair<>("", ""));
                    } else {
                        Pair<String, String> p = new Pair<>(getFirstTextFromXmlElement(param, xmlName), getTextFromXmlElement(param, xmlName, 1));
                        toRet.add(p);
                    }
                }
            }
        }
        return toRet;
    }

    private List<UMLAttribute> getAttributesFromXml(Element className){
        List<UMLAttribute> toRet = new ArrayList<>();

        //variables are declared using the xml element 'decl_stmt'
        NodeList declaredStatements = className.getElementsByTagName("decl_stmt");
        for (int i = 0; i < declaredStatements.getLength(); i++){

            Node statementNode = declaredStatements.item(i);
            if (notConstructorOrFunction(statementNode)){
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
    //the value of name as well.
    private boolean nameIsClassName(Node classNode){
        if (classNode.getNodeName().equals("annotation")){
            return false;
        }else if (classNode.getNodeName().equals("class")){
            return true;
        }else{
            return nameIsClassName(classNode.getParentNode());
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
            File directory = new File("srcMLOutput\\" + Main.projectID + "13\\");
            if (!directory.exists()) {
                Files.createDirectory(Paths.get("srcMLOutput\\"+ Main.projectID + "13\\"));
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
