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
import com.derek.rbml.InteractionRole;
import com.derek.uml.UMLGenerator;
import com.derek.uml.srcML.SrcMLRunner;
import com.derek.view.View;

import java.io.FileInputStream;
import java.util.*;

public class Main {

    public static boolean verboseLog = false;

    public static int recursiveDebugger = 0;

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

            //buildSeleniumConfigs();
            //buildJHotDrawConfigs();
            //buildGuavaConfigs();


            //not working right now.

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
            UMLGenerator umlGenerator = new UMLGenerator(runner.get(version).getRootBlocks());
            m.addClassDiagram(version, umlGenerator.getUmlClassDiagram());
        }
        Comparatizer cpt = new Comparatizer(m);
        cpt.runAnalysis();
    }

    private void buildSeleniumConfigs(){
        projectID = "selenium";
        workingDirectory = "C://Users/Derek Reimanis/Documents/research/behavior/projects/selenium/";
        interVersionKey = "-src/";
        interProjectKey = "org/";
        projectLanguage = ".java";
        projectVersions = new ArrayList<>();
        for (int i = 0; i < 20; i++){
            projectVersions.add(new SoftwareVersion(i));
        }
        runner = new HashMap<>();
        Model m = new Model(projectVersions);
        for (SoftwareVersion version : projectVersions) {
            ///selenium/36-src/org
            currentVersion = version.getVersionNum();
            String pwd = workingDirectory + version.getVersionNum() + interVersionKey + interProjectKey + "openqa/";
            runner.put(version, new SrcMLRunner(pwd, version.getVersionNum()));
            UMLGenerator umlGenerator = new UMLGenerator(runner.get(version).getRootBlocks());
            m.addClassDiagram(version, umlGenerator.getUmlClassDiagram());
            //i wonder if this should just take in the model. - as in new Comparatizer(m);
        }
        Comparatizer cpt = new Comparatizer(m);
        cpt.runAnalysis();
    }

    private void buildJHotDrawConfigs(){
        //will need to adjust project directory info here. - I made changes to selenium structure.
        projectID = "jhotDraw";
        workingDirectory = "C://Users/Derek Reimanis/Documents/research/behavior/projects/jhotdraw/";
        interVersionKey = "-src/sources";
        interProjectKey = "CH/";
        projectLanguage = ".java";
        projectVersions = new ArrayList<>();
        projectVersions.add(new SoftwareVersion(52));
        runner = new HashMap<>();
        Model m = new Model(projectVersions);
        for (SoftwareVersion version : projectVersions) {
            currentVersion = version.getVersionNum();
            String pwd = workingDirectory + version.getVersionNum() + interVersionKey + "/" + interProjectKey + "ifa/";
            runner.put(version, new SrcMLRunner(pwd, version.getVersionNum()));
            UMLGenerator umlGenerator = new UMLGenerator(runner.get(version).getRootBlocks());
            m.addClassDiagram(version, umlGenerator.getUmlClassDiagram());
        }
        Comparatizer cpt = new Comparatizer(m);
        cpt.runAnalysis();
    }

    public void buildGuavaConfigs(){
        projectID = "guava";
        workingDirectory = "C://Users/Derek Reimanis/Documents/research/behavior/projects/guava/";
        interVersionKey = "guava-";
        interProjectKey = "guava/src/";
        projectLanguage = ".java";
        projectVersions = new ArrayList<>();
        projectVersions.add(new SoftwareVersion(13));
        //manually entering 1 project now. Once the runner is set up I will extend to allow for batch-style runs
        for (SoftwareVersion version : projectVersions) {
            String pwd = "workingDirectory + \"" + version.getVersionNum() + "-src/\" + interVersionKey + \"" + version.getVersionNum() + ".0/\" + interProjectKey + \"com\"";
            runner.put(version, new SrcMLRunner(pwd, version.getVersionNum()));
        }
    }


    public static void main(String[] args) {
        new Main();
    }
}
