package com.derek.uml;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import sun.reflect.annotation.ExceptionProxy;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * This class runs the srcML tool  (http://www.srcml.org/#home) on a software project.
 */
public class SrcMLRunner {

    private final boolean storeSrcML = false;
    private String projectDirectory;
    private List<String> srcMLOutput;

    public SrcMLRunner(String projectDirectory){
        this.projectDirectory = projectDirectory;
        splitSrcML(generateSrcML());
    }

    //this method runs through the raw srcMLOutput, which is a huge xml file, and pulls out each class (<unit></unit> tag)
    private void splitSrcML(String fullOutput){
        try {
            srcMLOutput = new ArrayList<>();
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(fullOutput);

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

    public String generateSrcML(){
        try{
            System.out.println("Starting srcML generation");
            //https://stackoverflow.com/questions/5604698/java-programming-call-an-exe-from-java-and-passing-parameters
            quotify();
            Runtime rt = Runtime.getRuntime();
            String[] commands = {"srcML", projectDirectory};
            Process proc = rt.exec(commands);
            BufferedReader stdInput = new BufferedReader(new InputStreamReader(proc.getInputStream()));

            String srcMLOut = "";
            String s = null;
            if (storeSrcML) {
                File fout = new File("resources/output.xml");
                BufferedWriter bf = new BufferedWriter(new FileWriter(fout));
                while ((s = stdInput.readLine()) != null) {
                    srcMLOut += s;
                    bf.write(s);
                }
                bf.close();
            }

            System.out.println("Ending srcML generation");
            return srcMLOut;
        }catch(Exception e){
            e.printStackTrace();
        }
        return "Error in SrcML generation";
    }

    private void quotify(){
        projectDirectory = "\"" + projectDirectory + "\"";
        projectDirectory = projectDirectory.replace("/", "\\") ;
    }




}
