package com.derek;

import com.google.gson.Gson;
import miltos.diploma.characteristics.QualityModel;
import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigInteger;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class QualityParser {

    private Map<String, QualityModel> qualityModels;
    private List<String> sortedVersions;

    /***
     * Constructor
     * @param directory takes in the directory that contains the json output from qatch.
     */
    public QualityParser(String directory){
        qualityModels = new HashMap<>();
        input(directory);
        getEarlyOutput();
    }

    private void getEarlyOutput(){
        System.out.println("Version, Maintainability, Reliability, Security, Performance_Efficiency, Total_Quality_Index");

        Comparator<String> numberAwareStringComparator = new NumberAwareStringComparator();
        Object[] asObjects = sortedVersions.toArray();
        String[] asArray = new String[asObjects.length];
        for (int i = 0; i < asObjects.length; i++) {
            asArray[i] = (String)asObjects[i];
        }

        Arrays.sort(asArray, numberAwareStringComparator);

        for (String version : asArray){
            System.out.print(version + ", " + qualityModels.get(version).getCharacteristics().get(0).getEval() + ", ");
            System.out.print(qualityModels.get(version).getCharacteristics().get(1).getEval() + ", ");
            System.out.print(qualityModels.get(version).getCharacteristics().get(2).getEval() + ", ");
            System.out.print(qualityModels.get(version).getCharacteristics().get(3).getEval() + ", ");
            System.out.println(qualityModels.get(version).getTqi().getEval());

        }

    }

    private void input(String directory){
        try {
            Gson gson = new Gson();
            File dir = new File(directory);
            sortedVersions = new ArrayList<>();
            if (dir.isDirectory()){
                File[] files = dir.listFiles();
                for (File f : files){
                    if (FilenameUtils.getExtension(f.getName()).contains("json")) {
                        String versionName = f.getName().split("_")[0];
                        sortedVersions.add(versionName);
                        QualityModel model = gson.fromJson(new FileReader(f), QualityModel.class);
                        qualityModels.put(versionName, model);
                    }
                }
            }
        }catch(IOException e){
            e.printStackTrace();
            System.out.println("File IO issue - in quality parser.");
        }
    }

    //https://codereview.stackexchange.com/questions/37192/number-aware-string-sorting-with-comparator
    public class NumberAwareStringComparator implements Comparator<String> {

        private final Pattern PATTERN = Pattern.compile("(\\D*)(\\d*)");

        private NumberAwareStringComparator() {
        }

        public int compare(String s1, String s2) {
            Matcher m1 = PATTERN.matcher(s1);
            Matcher m2 = PATTERN.matcher(s2);

            // The only way find() could fail is at the end of a string
            while (m1.find() && m2.find()) {
                // matcher.group(1) fetches any non-digits captured by the
                // first parentheses in PATTERN.
                int nonDigitCompare = m1.group(1).compareTo(m2.group(1));
                if (0 != nonDigitCompare) {
                    return nonDigitCompare;
                }

                // matcher.group(2) fetches any digits captured by the
                // second parentheses in PATTERN.
                if (m1.group(2).isEmpty()) {
                    return m2.group(2).isEmpty() ? 0 : -1;
                } else if (m2.group(2).isEmpty()) {
                    return +1;
                }

                BigInteger n1 = new BigInteger(m1.group(2));
                BigInteger n2 = new BigInteger(m2.group(2));
                int numberCompare = n1.compareTo(n2);
                if (0 != numberCompare) {
                    return numberCompare;
                }
            }

            // Handle if one string is a prefix of the other.
            // Nothing comes before something.
            return m1.hitEnd() && m2.hitEnd() ? 0 :
                    m1.hitEnd()                ? -1 : +1;
        }
    }
}
