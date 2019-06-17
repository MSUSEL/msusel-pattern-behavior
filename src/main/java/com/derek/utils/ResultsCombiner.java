package com.derek.utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.Scanner;

public class ResultsCombiner {

    public static void main(String[] args){
        new ResultsCombiner();
    }

    public ResultsCombiner(){
        //combine("guava", "C:/Users/Derek Reimanis/Documents/research/quality/guava/guava_results");
        //combine("hystrix", "C:/Users/Derek Reimanis/Documents/research/quality/hystrix/hystrix_results");
        //combine("rxjava", "C:/Users/Derek Reimanis/Documents/research/quality/rxjava/rxjava_results");
        //combine("selenium", "C:/Users/Derek Reimanis/Documents/research/quality/selenium/selenium_results");
        //combine("springBoot", "C:/Users/Derek Reimanis/Documents/research/quality/springBoot/springBoot_results");
        //combine("elasticsearch", "C:/Users/Derek Reimanis/Documents/research/quality/elasticsearch/elasticsearch_results");
        //combine("commons-lang", "C:/Users/Derek Reimanis/Documents/research/quality/commons-lang/commons-lang_results");
        //combine("netty", "C:/Users/Derek Reimanis/Documents/research/quality/netty/netty_results");
        //combine("glide", "C:/Users/Derek Reimanis/Documents/research/quality/glide/glide_results");
        combine("mockito", "C:/Users/Derek Reimanis/Documents/research/quality/mockito/mockito_results");

    }

    public void combine(String projectName, String dirPath){
        try{
            File dir = new File(dirPath);
            File[] files = dir.listFiles();
            StringBuilder outputter = new StringBuilder();
            outputter.append("Project\tVersion\tTQI\tFunctional_Suitability\tPerformance_Efficiency\tCompatibility\tUsability\tReliability\tSecurity\tMaintainability\tPortability\tBad_Function\tComprehensibility\tRedundancy\tStructurdness\tAssignment\tResource_Handling\tCohesion\tCoupling\tComplexity\tMessaging\tEncapsulation\tPattern_Structural_Integrity\tPattern_Behavioral_Integrity\tPattern_Instability\tPattern_Structural_Aberrations\tPattern_Behavioral_Aberrations\t\n");
            for (File f : files){
                //7 for 'version', we want to remove version.
                String name = f.getName().split("_")[0].substring(7);
                outputter.append(projectName + "\t" + name + "\t");
                Scanner in = new Scanner(f);
                //first line is header.
                in.nextLine();
                outputter.append(in.nextLine() + "\n");
            }

            System.out.println(outputter.toString());

            File outFile = new File(dirPath + "/combined.tab");
            PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(outFile, false)));
            out.println(outputter.toString());
            out.close();

        }catch(Exception e){
            e.printStackTrace();
        }
    }


}
