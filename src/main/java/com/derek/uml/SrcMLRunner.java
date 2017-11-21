package com.derek.uml;

/**
 * This class runs the srcML tool  (http://www.srcml.org/#home) on a software project.
 */
public class SrcMLRunner {

    private String projectDirectory;

    public SrcMLRunner(String projectDirectory){
        this.projectDirectory = projectDirectory;
        generateSrcML();
    }

    public void generateSrcML(){
        try{

            //https://stackoverflow.com/questions/5604698/java-programming-call-an-exe-from-java-and-passing-parameters
            quotify();
            System.out.println(projectDirectory);
            Process process = new ProcessBuilder("srcML", projectDirectory, "-o", "selenium36.xml").start();
            process.destroy();

        }catch(Exception e){
            e.printStackTrace();
        }
    }

    private void quotify(){
        projectDirectory = "\"" + projectDirectory + "\"";
        projectDirectory = projectDirectory.replace("/", "\\") ;
    }




}
