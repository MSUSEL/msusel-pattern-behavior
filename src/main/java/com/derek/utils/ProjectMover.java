package com.derek.utils;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

import java.io.File;

public class ProjectMover {

    public final String projectBase = "C:\\Users\\Derek Reimanis\\Documents\\research\\quality\\";

    public static void main(String[] args){
        new ProjectMover();
    }

    public ProjectMover(){
        //String projectName = "hystrix";
        //String projectName = "rxjava";
        //String projectName = "selenium";
        String projectName = "springBoot";
        moveDevaResults(projectName);
    }

    /***
     * deva results go in the top level folders of the respective version number.
     * @param projectName
     */
    public void moveDevaResults(String projectName){
        try{
            //just moving shit, no file io
            File projectDirectory = new File("devaResults\\" + projectName);
            for (File versionResult : projectDirectory.listFiles()){
                String versionNum = FilenameUtils.removeExtension(versionResult.getName()).substring(11);
                File moveLocation = new File(projectBase + projectName + "\\version" + versionNum);
                //moveLocation should be a dir.
                if (moveLocation.isDirectory()){
                    FileUtils.copyFileToDirectory(versionResult, moveLocation);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void moveProjectFile(String projectName){
        try{

        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
