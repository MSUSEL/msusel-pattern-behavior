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
import com.derek.view.View;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main {

    public static int debug = 0;

    //configs
    public static String workingDirectory;
    public static String projectID;
    public static String interVersionKey;
    public static String interProjectKey;
    public static String projectLanguage;
    private static Map<SoftwareVersion, SrcMLRunner> runner;
    public static List<SoftwareVersion> projectVersions;
    public static int currentVersion;

    public Main(){

        //buildSeleniumConfigs();
        buildJHotDrawConfigs();
        //buildGuavaConfigs();


        //not working right now.

        //new View(new Model(projectVersions));
        //view builds the model too. This might change as this project matures.
    }

    private void buildSeleniumConfigs(){
        projectID = "selenium";
        workingDirectory = "C://Users/Derek Reimanis/Documents/research/behavior/projects/selenium/";
        interVersionKey = "-src/selenium-";
        interProjectKey = "org/";
        projectLanguage = ".java";
        projectVersions = new ArrayList<>();
        projectVersions.add(new SoftwareVersion(36));
        //projectVersions.add(new SoftwareVersion(38));
        runner = new HashMap<>();
        for (SoftwareVersion version : projectVersions) {
            ///selenium/36-src/selenium-36/org
            currentVersion = version.getVersionNum();
            String pwd = workingDirectory + version.getVersionNum() + interVersionKey + version.getVersionNum() + "/" + interProjectKey + "openqa/";
            runner.put(version, new SrcMLRunner(pwd, version.getVersionNum()));
            UMLGenerator umlGenerator = new UMLGenerator(runner.get(version).getRootBlocks());
            Model m = new Model(projectVersions);
            Comparatizer cpt = new Comparatizer(m, umlGenerator.getUmlClassDiagram());
            cpt.testComparisons();
        }
    }

    private void buildJHotDrawConfigs(){
        projectID = "jhotDraw";
        workingDirectory = "C://Users/Derek Reimanis/Documents/research/behavior/projects/jhotdraw/";
        interVersionKey = "-src/sources";
        interProjectKey = "CH/";
        projectLanguage = ".java";
        projectVersions = new ArrayList<>();
        projectVersions.add(new SoftwareVersion(52));
        //projectVersions.add(new SoftwareVersion(38));
        runner = new HashMap<>();
        for (SoftwareVersion version : projectVersions) {
            currentVersion = version.getVersionNum();
            String pwd = workingDirectory + version.getVersionNum() + interVersionKey + "/" + interProjectKey + "ifa/";
            runner.put(version, new SrcMLRunner(pwd, version.getVersionNum()));
            UMLGenerator umlGenerator = new UMLGenerator(runner.get(version).getRootBlocks());
            Model m = new Model(projectVersions);
            Comparatizer cpt = new Comparatizer(m, umlGenerator.getUmlClassDiagram());
            cpt.testComparisons();
        }
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
