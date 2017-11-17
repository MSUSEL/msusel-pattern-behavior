package com.derek;

import com.derek.model.Model;
import com.derek.view.View;

public class Main {

    public static String workingDirectory = "C://Users/Derek Reimanis/Documents/research/behavior/projects/guava/";
    public static String interVersionKey = "guava-";
    public static String interProjectKey = "guava/src/";
    public static String projectLanguage = ".java";

    public static void main(String[] args) {

        new View();
        //view builds the model too. This might change as this project matures.


        //new Model();
    }
}
