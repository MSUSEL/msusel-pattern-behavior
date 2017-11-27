package com.derek;

import com.derek.model.Model;
import com.derek.uml.SrcMLRunner;
import com.derek.view.View;

public class Main {

    public static String workingDirectory = "C://Users/Derek Reimanis/Documents/research/behavior/projects/selenium/";


    public static String interVersionKey = "-src/selenium-";
    public static String interProjectKey = "org/";
    public static String projectLanguage = ".java";


    //guava configs
    //public static String workingDirectory = "C://Users/Derek Reimanis/Documents/research/behavior/projects/guava/";
    //public static String interVersionKey = "guava-";
    //public static String interProjectKey = "guava/src/";

    public static void main(String[] args) {

        //new View();
        SrcMLRunner runner = new SrcMLRunner(workingDirectory + "36" + interVersionKey + "3.6/" + interProjectKey + "openqa");
        //view builds the model too. This might change as this project matures.


        //new Model();
    }
}
