package com.derek.uml;

import com.derek.Main;
import org.w3c.dom.Document;
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

    public SrcMLRunner(String projectWorkingDirectory){
        this.projectWorkingDirectory = projectWorkingDirectory;
        generateSrcML();
    }

    //this method runs through the raw srcMLOutput, which is an xml file, and build each class using uml
    private void buildClassDiagram(){
        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(fileName);

            NodeList patterns = doc.getElementsByTagName("unit");
            //iterate through pattern types within the xml
            for (int i = 0; i < patterns.getLength(); i++) {
                Node patternIter = patterns.item(i);
                System.out.println(patternIter);
            }

        }catch(Exception e){
            e.printStackTrace();
        }
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

            //change directory in future.
            File directory = new File("srcMLOutput\\" + Main.projectID + "13\\");
            if (!directory.exists()) {
                Files.createDirectory(Paths.get("srcMLOutput\\"+ Main.projectID + "13\\"));
            }

            for (Path p : pathsAsPath) {
                String current = quotify(p.toString());

                System.out.println("Starting srcml generation for " + current);
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
