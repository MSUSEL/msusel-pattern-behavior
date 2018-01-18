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
package com.derek.uml.srcML;

import com.derek.Main;
import lombok.Getter;
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
@Getter
public class SrcMLRunner {

    private final boolean storeSrcML = true;
    private String projectWorkingDirectory;
    private List<SrcMLBlock> rootBlocks;

    public SrcMLRunner(String projectWorkingDirectory){
        this.projectWorkingDirectory = projectWorkingDirectory;
        rootBlocks = new ArrayList<>();
        generateSrcML();
        //parseSpecificFiles();
        parseAllFiles();

        //guava test
        //buildClassDiagram(new File("srcMLOutput/guava13/Files.xml"));
    }

    private SrcMLBlock parseSrcMLFile(File file){
        try{
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(file);
            NodeList roots = doc.getElementsByTagName("unit");
            SrcMLBlock block = null;
            for (int i = 0; i < roots.getLength(); i++) {
                Node root = roots.item(i);
                //kick off parsing.
                block = new SrcMLBlock(XmlUtils.elementify(root));
            }
            return block;
        }catch(Exception e){
            e.printStackTrace();
        }
        System.out.println("Error in parseSrcMLFile");
        return null;
    }

    private void parseAllFiles(){
        //https://stackoverflow.com/questions/5694385/getting-the-filenames-of-all-files-in-a-folder
        File f = new File("srcMLOutput/selenium36/");
        File[] listOfFiles = f.listFiles();
        for (int i = 0; i < listOfFiles.length; i++) {
            rootBlocks.add(parseSrcMLFile(listOfFiles[i]));
        }
    }
    private void parseSpecificFiles(){
        rootBlocks.add(parseSrcMLFile(new File("srcMLOutput/selenium36/Actions.xml")));
        rootBlocks.add(parseSrcMLFile(new File("srcMLOutput/selenium36/RemoteWebDriver.xml")));
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
