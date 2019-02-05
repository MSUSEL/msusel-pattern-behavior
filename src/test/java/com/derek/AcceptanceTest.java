package com.derek;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

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
    public void testVertsion1(){
        CSVRecord thisVersion = outputRecords.iterator().next();
        assertEquals(thisVersion.get("Software_Version"), "1");
    }

    /***
     * shortcut utliity method to speed up the process of testing modular grime types. supply this method with a csvrecord,
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
