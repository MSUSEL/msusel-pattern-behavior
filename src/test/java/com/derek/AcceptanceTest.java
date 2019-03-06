package com.derek;

import jdk.nashorn.internal.ir.annotations.Ignore;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.Reader;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.assertEquals;


/***
 * Class to handle acceptance testing of my research tool. The idea is buildConfigs() reads in the configuration data to initialize the internal data model,
 * and each test method will look at one version of the project to identify if a type of grime is added in that version. I will start with modular grime, adding
 * each type of grime and asserting that I am identifying they correctly. Once I hit behavioral grime, it might be impossible to NOT add other structural grime types
 * as I add behavioral, but I will try to keep it independent.
 */
public class AcceptanceTest {
    private static Main main;
    private static Iterable<CSVRecord> outputRecords;

    @BeforeAll
    public static void buildConfigs(){
        main = new Main();
        try {
            Properties seleniumProperties = new Properties();
            FileInputStream fileInputStream = new FileInputStream("src/test/resources/AcceptanceTestConfig.properties");
            seleniumProperties.load(fileInputStream);

            main.projectID = seleniumProperties.getProperty("projectID");
            main.projectLanguage = seleniumProperties.getProperty("projectLanguage");
            main.workingDirectory = seleniumProperties.getProperty("workingDirectory");
            main.versions = seleniumProperties.getProperty("versions");
            main.patternDetectionOutput = seleniumProperties.getProperty("patternDetectionOutput");
            main.interVersionKey = seleniumProperties.getProperty("interVersionKey");
            main.interProjectKey = seleniumProperties.getProperty("interProjectKey");
            main.consecutivePatterns = seleniumProperties.getProperty("consecutivePatterns");
            main.outputFileName = seleniumProperties.getProperty("outputFileName");
            main.printIndividualRoles = seleniumProperties.getProperty("printIndividualRoles");
            main.patternCommonNames = seleniumProperties.getProperty("patternCommonNames");
            main.clientClassAllowances = Integer.parseInt(seleniumProperties.getProperty("clientClassAllowances"));
            main.clientUsageAllowances = Integer.parseInt(seleniumProperties.getProperty("clientUsageAllowances"));

            fileInputStream.close();
            //clear old output file names. I should consider doing this to srcml files too.
            File output = new File(main.outputFileName);
            if (output.delete()){
                System.out.println("Deleted old output file");
            }


        }catch (Exception e){
            e.printStackTrace();
        }finally {
            main.doAnalysis();
            loadOutputFile();
        }
    }

    private static void loadOutputFile(){
        try {
            Reader in = new FileReader(main.outputFileName);
            outputRecords = CSVFormat.TDF.withFirstRecordAsHeader().parse(in);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Test
    public void testVersion0(){
        CSVRecord thisVersion = outputRecords.iterator().next();
        assertEquals("0", thisVersion.get("Software_Version"));
        testModularGrimeType(thisVersion, "MG-PEA", 0, 0 , 0);
        testModularGrimeType(thisVersion, "MG-PEE", 0, 0 , 0);
        testModularGrimeType(thisVersion, "MG-PI", 0, 0 , 0);
        testModularGrimeType(thisVersion, "MG-TEA", 0, 0 , 0);
        testModularGrimeType(thisVersion, "MG-TEE", 0, 0 , 0);
        testModularGrimeType(thisVersion, "MG-TI", 0, 0 , 0);
    }

    @Test
    public void testVersion1(){
        CSVRecord thisVersion = outputRecords.iterator().next();
        assertEquals("1", thisVersion.get("Software_Version"));
        testModularGrimeType(thisVersion, "MG-PEA", 1, 1 , 0);
        testModularGrimeType(thisVersion, "MG-PEE", 0, 0 , 0);
        testModularGrimeType(thisVersion, "MG-PI", 0, 0 , 0);
        testModularGrimeType(thisVersion, "MG-TEA", 0, 0 , 0);
        testModularGrimeType(thisVersion, "MG-TEE", 0, 0 , 0);
        testModularGrimeType(thisVersion, "MG-TI", 0, 0 , 0);
    }

    @Test
    public void testVersion2(){
        CSVRecord thisVersion = outputRecords.iterator().next();
        assertEquals("2", thisVersion.get("Software_Version"));
        testModularGrimeType(thisVersion, "MG-PEA", 0, 0, 1);
        testModularGrimeType(thisVersion, "MG-PEE", 1, 1, 0);
        testModularGrimeType(thisVersion, "MG-PI", 0, 0 , 0);
        testModularGrimeType(thisVersion, "MG-TEA", 0, 0 , 0);
        testModularGrimeType(thisVersion, "MG-TEE", 0, 0 , 0);
        testModularGrimeType(thisVersion, "MG-TI", 0, 0 , 0);
    }

    @Test
    public void testVersion3(){
        CSVRecord thisVersion = outputRecords.iterator().next();
        assertEquals("3", thisVersion.get("Software_Version"));
        testModularGrimeType(thisVersion, "MG-PEA", 0, 0 , 0);
        testModularGrimeType(thisVersion, "MG-PEE", 0, 0 , 1);
        testModularGrimeType(thisVersion, "MG-PI", 1, 1 , 0);
        testModularGrimeType(thisVersion, "MG-TEA", 0, 0 , 0);
        testModularGrimeType(thisVersion, "MG-TEE", 0, 0 , 0);
        testModularGrimeType(thisVersion, "MG-TI", 0, 0 , 0);
    }

    @Test
    public void testVersion4(){
        CSVRecord thisVersion = outputRecords.iterator().next();
        assertEquals("4", thisVersion.get("Software_Version"));
        testModularGrimeType(thisVersion, "MG-PEA", 0, 0 , 0);
        testModularGrimeType(thisVersion, "MG-PEE", 0, 0 , 0);
        testModularGrimeType(thisVersion, "MG-PI", 0, 0 , 1);
        testModularGrimeType(thisVersion, "MG-TEA", 1, 1, 0);
        testModularGrimeType(thisVersion, "MG-TEE", 0, 0 , 0);
        testModularGrimeType(thisVersion, "MG-TI", 0, 0 , 0);
    }

    @Test
    public void testVersion5(){
        CSVRecord thisVersion = outputRecords.iterator().next();
        assertEquals("5", thisVersion.get("Software_Version"));
        testModularGrimeType(thisVersion, "MG-PEA", 0, 0 , 0);
        testModularGrimeType(thisVersion, "MG-PEE", 0, 0 , 0);
        testModularGrimeType(thisVersion, "MG-PI", 0, 0 , 0);
        testModularGrimeType(thisVersion, "MG-TEA", 0, 0 , 1);
        testModularGrimeType(thisVersion, "MG-TEE", 1, 1 , 0);
        testModularGrimeType(thisVersion, "MG-TI", 0, 0 , 0);
    }

    @Test
    public void testVersion6(){
        CSVRecord thisVersion = outputRecords.iterator().next();
        assertEquals("6", thisVersion.get("Software_Version"));
        testModularGrimeType(thisVersion, "MG-PEA", 0, 0 , 0);
        testModularGrimeType(thisVersion, "MG-PEE", 0, 0 , 0);
        testModularGrimeType(thisVersion, "MG-PI", 0, 0 , 0);
        testModularGrimeType(thisVersion, "MG-TEA", 0, 0 , 0);
        testModularGrimeType(thisVersion, "MG-TEE", 0, 0, 1);
        testModularGrimeType(thisVersion, "MG-TI", 1, 1, 0);
    }

    @Test
    public void testVersion7(){
        CSVRecord thisVersion = outputRecords.iterator().next();
        assertEquals("7", thisVersion.get("Software_Version"));
        //pea is 2 because of how I have set up peao. (3 associations from external classes -> pattern classes.)
        testModularGrimeType(thisVersion, "MG-PEA", 2, 2 , 0);
        testModularGrimeType(thisVersion, "MG-PEE", 0, 0 , 0);
        testModularGrimeType(thisVersion, "MG-PI", 0, 0, 0);
        testModularGrimeType(thisVersion, "MG-TEA", 0, 0 , 0);
        testModularGrimeType(thisVersion, "MG-TEE", 0, 0, 0);
        //removing 1 TI grime because that was from the modular grime test (version 6)
        testModularGrimeType(thisVersion, "MG-TI", 0, 0, 1);
        testModularGrimeType(thisVersion, "OG-PEA", 1, 1, 0);
        testModularGrimeType(thisVersion, "OG-PI", 0, 0, 0);
        testModularGrimeType(thisVersion, "OG-TEA", 0, 0, 0);
        testModularGrimeType(thisVersion, "OG-TI", 0, 0, 0);
    }

    @Test
    public void testVersion8(){
        CSVRecord thisVersion = outputRecords.iterator().next();
        assertEquals("8", thisVersion.get("Software_Version"));
        //pea is 2 because of how I have set up peao. (3 associations from external classes -> pattern classes.)
        testModularGrimeType(thisVersion, "MG-PEA", 0, 0 , 2);
        testModularGrimeType(thisVersion, "MG-PEE", 0, 0 , 0);
        testModularGrimeType(thisVersion, "MG-PI", 0, 0, 0);
        testModularGrimeType(thisVersion, "MG-TEA", 0, 0 , 0);
        testModularGrimeType(thisVersion, "MG-TEE", 0, 0, 0);
        //removing 1 TI grime because that was from the modular grime test (version 6)
        testModularGrimeType(thisVersion, "MG-TI", 0, 0, 0);
        testModularGrimeType(thisVersion, "OG-PEA", 0, 0, 1);
        testModularGrimeType(thisVersion, "OG-PI", 1, 1, 0);
        testModularGrimeType(thisVersion, "OG-TEA", 0, 0, 0);
        testModularGrimeType(thisVersion, "OG-TI", 0, 0, 0);
    }

    @Test
    public void testVersion9(){
        CSVRecord thisVersion = outputRecords.iterator().next();
        assertEquals("9", thisVersion.get("Software_Version"));
        testModularGrimeType(thisVersion, "MG-PEA", 0, 0 , 0);
        testModularGrimeType(thisVersion, "MG-PEE", 0, 0 , 0);
        testModularGrimeType(thisVersion, "MG-PI", 0, 0, 0);
        testModularGrimeType(thisVersion, "MG-TEA", 1, 1 , 0);
        testModularGrimeType(thisVersion, "MG-TEE", 0, 0, 0);
        //removing 1 TI grime because that was from the modular grime test (version 6)
        testModularGrimeType(thisVersion, "MG-TI", 0, 0, 0);
        testModularGrimeType(thisVersion, "OG-PEA", 0, 0, 0);
        testModularGrimeType(thisVersion, "OG-PI", 0, 0, 1);
        testModularGrimeType(thisVersion, "OG-TEA", 1, 1, 0);
        testModularGrimeType(thisVersion, "OG-TI", 0, 0, 0);
    }

    @Test
    public void testVersion10(){
        CSVRecord thisVersion = outputRecords.iterator().next();
        assertEquals("10", thisVersion.get("Software_Version"));
        testModularGrimeType(thisVersion, "MG-PEA", 0, 0 , 0);
        testModularGrimeType(thisVersion, "MG-PEE", 0, 0 , 0);
        testModularGrimeType(thisVersion, "MG-PI", 0, 0, 0);
        testModularGrimeType(thisVersion, "MG-TEA", 0, 0 , 1);
        testModularGrimeType(thisVersion, "MG-TEE", 0, 0, 0);
        //removing 1 TI grime because that was from the modular grime test (version 6)
        testModularGrimeType(thisVersion, "MG-TI", 0, 0, 0);
        testModularGrimeType(thisVersion, "OG-PEA", 0, 0, 0);
        testModularGrimeType(thisVersion, "OG-PI", 0, 0, 0);
        testModularGrimeType(thisVersion, "OG-TEA", 0, 0, 1);
        testModularGrimeType(thisVersion, "OG-TI", 1, 1, 0);
    }
    @Test
    public void testVersion11(){
        CSVRecord thisVersion = outputRecords.iterator().next();
        assertEquals("11", thisVersion.get("Software_Version"));
        //pea is 2 because of how I have set up peao. (3 associations from external classes -> pattern classes.)
        testModularGrimeType(thisVersion, "MG-PEA", 2, 2 , 0);
        testModularGrimeType(thisVersion, "MG-PEE", 0, 0 , 0);
        testModularGrimeType(thisVersion, "MG-PI", 0, 0, 0);
        testModularGrimeType(thisVersion, "MG-TEA", 0, 0 , 0);
        testModularGrimeType(thisVersion, "MG-TEE", 0, 0, 0);
        //removing 1 TI grime because that was from the modular grime test (version 6)
        testModularGrimeType(thisVersion, "MG-TI", 0, 0, 0);
        testModularGrimeType(thisVersion, "OG-PEA", 0, 0, 0);
        testModularGrimeType(thisVersion, "OG-PI", 0, 0, 0);
        testModularGrimeType(thisVersion, "OG-TEA", 0, 0, 0);
        testModularGrimeType(thisVersion, "OG-TI", 0, 0, 1);
        //so for the one below, its +1 grime addition only because technically speaking, the previous versions all have
        //3 instances of repetition (based on the code being a demo of the observer pattern. The subject.setSTate()
        //shoudl technically not exist 3 times in Client, as it does, but it does so for demo purposes. However, I would not expect
        //this to occur in real world systems, unless this is in fact grime.
        testModularGrimeType(thisVersion, "RG-PEA", 4, 1, 0);
        testModularGrimeType(thisVersion, "RG-PEE", 0, 0, 0);
        testModularGrimeType(thisVersion, "RG-PI", 0, 0, 0);
        testModularGrimeType(thisVersion, "RG-TEA", 0, 0, 0);
        testModularGrimeType(thisVersion, "RG-TEE", 0, 0, 0);
        testModularGrimeType(thisVersion, "RG-TI", 0, 0, 0);
    }

    @Ignore
    @Test
    public void testVersion14(){
        CSVRecord thisVersion = outputRecords.iterator().next();
        assertEquals("14", thisVersion.get("Software_Version"));
        //pea is 2 because of how I have set up peao. (3 associations from external classes -> pattern classes.)
        testModularGrimeType(thisVersion, "MG-PEA", 0, 0 , 0);
        testModularGrimeType(thisVersion, "MG-PEE", 0, 0 , 0);
        testModularGrimeType(thisVersion, "MG-PI", 0, 0, 0);
        testModularGrimeType(thisVersion, "MG-TEA", 1, 1 , 0);
        testModularGrimeType(thisVersion, "MG-TEE", 0, 0, 0);
        //removing 1 TI grime because that was from the modular grime test (version 6)
        testModularGrimeType(thisVersion, "MG-TI", 0, 0, 0);
        testModularGrimeType(thisVersion, "OG-PEA", 0, 0, 0);
        testModularGrimeType(thisVersion, "OG-PI", 0, 0, 0);
        testModularGrimeType(thisVersion, "OG-TEA", 0, 0, 0);
        testModularGrimeType(thisVersion, "OG-TI", 0, 0, 1);
        //so for the one below, its +1 grime addition only because technically speaking, the previous versions all have
        //3 instances of repetition (based on the code being a demo of the observer pattern. The subject.setSTate()
        //shoudl technically not exist 3 times in Client, as it does, but it does so for demo purposes. However, I would not expect
        //this to occur in real world systems, unless this is in fact grime.
        testModularGrimeType(thisVersion, "RG-PEA", 3, 0, 0);
        testModularGrimeType(thisVersion, "RG-PEE", 0, 0, 0);
        testModularGrimeType(thisVersion, "RG-PI", 0, 0, 0);
        testModularGrimeType(thisVersion, "RG-TEA", 2, 2, 0);
        testModularGrimeType(thisVersion, "RG-TEE", 0, 0, 0);
        testModularGrimeType(thisVersion, "RG-TI", 0, 0, 0);
    }


    /***
     * shortcut utility method to speed up the process of testing modular grime types. supply this method with a csvrecord,
     * grime name that corresponds with the name in the tab delimited output, and expected counts of grime.
     * @param csvRecord
     * @param grimeName
     * @param grimeCount
     * @param grimeAdditions
     * @param grimeRemovals
     */
    public void testModularGrimeType(CSVRecord csvRecord, String grimeName, int grimeCount, int grimeAdditions, int grimeRemovals){
        assertEquals( grimeCount + "", csvRecord.get(grimeName + " grime count"));
        assertEquals(grimeAdditions + "", csvRecord.get(grimeName + " grime additions"));
        assertEquals(grimeRemovals + "", csvRecord.get(grimeName + " grime removals"));
    }


}
