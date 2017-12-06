package com.derek;

import com.derek.model.Model;
import com.derek.uml.SrcMLRunner;
import com.derek.view.View;

public class Main {

    //guava configs
    public static String workingDirectory;
    public static String projectID;
    public static String interVersionKey;
    public static String interProjectKey;
    public static String projectLanguage;
    public static SrcMLRunner runner;
    public static int testProject;

    public Main(){

        buildSeleniumConfigs();
        //buildGuavaConfigs();
        //new View();
        //view builds the model too. This might change as this project matures.

        //new Model();
    }

    private void buildSeleniumConfigs(){
        projectID = "selenium";
        workingDirectory = "C://Users/Derek Reimanis/Documents/research/behavior/projects/selenium/";
        interVersionKey = "-src/selenium-";
        interProjectKey = "org/";
        projectLanguage = ".java";
        testProject = 36;
        runner = new SrcMLRunner(workingDirectory + "36" + interVersionKey + "3.6/" + interProjectKey + "openqa");
    }

    public void buildGuavaConfigs(){
        projectID = "guava";
        workingDirectory = "C://Users/Derek Reimanis/Documents/research/behavior/projects/guava/";
        interVersionKey = "guava-";
        interProjectKey = "guava/src/";
        projectLanguage = ".java";
        testProject = 13;
        //manually entering 1 project now. Once the runner is set up I will extend to allow for batch-style runs
        runner = new SrcMLRunner(workingDirectory + "13-src/" + interVersionKey + "13.0/" + interProjectKey + "com");
    }


    public static void main(String[] args) {
        new Main();
    }
}
