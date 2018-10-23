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
package com.derek;

import com.derek.model.Model;
import com.derek.model.SoftwareVersion;
import com.derek.uml.UMLGenerator;
import com.derek.uml.srcML.SrcMLRunner;
import lombok.Setter;

import java.io.FileInputStream;
import java.util.*;

public class Main {

    public static boolean verboseLog = false;

    @Setter
    public static int counter = 0;


    //configs
    public static String projectID;
    public static String projectLanguage;
    public static String workingDirectory;
    public static String versions;
    public static String patternDetectionOutput;
    public static String interVersionKey;
    public static String interProjectKey;
    public static String consecutivePatterns;
    public static String outputFileName;
    public static String printIndividualRoles;
    public static String patternCommonNames;

    private static Map<SoftwareVersion, SrcMLRunner> runner;
    public static List<SoftwareVersion> projectVersions;
    public static int currentVersion;

    public Main(){
        try {
            Properties seleniumProperties = new Properties();
            FileInputStream fileInputStream = new FileInputStream("configs/config.properties");
            seleniumProperties.load(fileInputStream);

            this.projectID = seleniumProperties.getProperty("projectID");
            this.projectLanguage = seleniumProperties.getProperty("projectLanguage");
            this.workingDirectory = seleniumProperties.getProperty("workingDirectory");
            this.versions = seleniumProperties.getProperty("versions");
            this.patternDetectionOutput = seleniumProperties.getProperty("patternDetectionOutput");
            this.interVersionKey = seleniumProperties.getProperty("interVersionKey");
            this.interProjectKey = seleniumProperties.getProperty("interProjectKey");
            this.consecutivePatterns = seleniumProperties.getProperty("consecutivePatterns");
            this.outputFileName = seleniumProperties.getProperty("outputFileName");
            this.printIndividualRoles = seleniumProperties.getProperty("printIndividualRoles");
            this.patternCommonNames = seleniumProperties.getProperty("patternCommonNames");


            //new View(new Model(projectVersions));
            //view builds the model too. This might change as this project matures.

            fileInputStream.close();
            doAnalysis();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void doAnalysis(){
        projectVersions = new ArrayList<>();
        for (int i = 0; i < Integer.parseInt(versions); i++){
            projectVersions.add(new SoftwareVersion(i));
        }
        runner = new HashMap<>();
        Model m = new Model(projectVersions);
        for (SoftwareVersion version : projectVersions){
            currentVersion = version.getVersionNum();
            String pwd = workingDirectory + version.getVersionNum() + interVersionKey + interProjectKey;
            runner.put(version, new SrcMLRunner(pwd, version.getVersionNum()));
            System.out.println("Generating UML for version: " + version.getVersionNum());
            UMLGenerator umlGenerator = new UMLGenerator(runner.get(version).getRootBlocks());
            m.addClassDiagram(version, umlGenerator.getUmlClassDiagram());
        }
        Comparatizer cpt = new Comparatizer(m);
        //cpt.runAnalysis();
    }

    public static void main(String[] args) {
        new Main();
    }
}
