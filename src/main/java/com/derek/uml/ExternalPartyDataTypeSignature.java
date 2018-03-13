package com.derek.uml;

import lombok.Getter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

@Getter
public class ExternalPartyDataTypeSignature {

    private PackageTree packageTree;
    private List<UMLClassifier> dataTypes;

    public ExternalPartyDataTypeSignature(String file){
        //these files are stored externally for ease of use. So the constructor kicks off parse of that file, which will fill the attributes in this file.
        dataTypes = new ArrayList<>();
        parse(file);
    }
    private void parse(String file){
        try{
            Scanner in = new Scanner(new File(file));
            String classifierTag = "";
            while (in.hasNext()) {
                String line = in.nextLine();
                List<String> residingPackage = new ArrayList<>();
                switch(line){
                    case "<I>":
                        classifierTag = "interface";
                        break;
                    case "<C>":
                        classifierTag = "class";
                        break;
                    case "<Enum>":
                        classifierTag = "enum";
                        break;
                    case "<Exception>":
                        classifierTag = "exception";
                        break;
                    case "<Error>":
                        classifierTag = "error";
                        break;
                    default:
                        //every other case that isn't strictly a type classification
                        switch (classifierTag){
                            case "interface":
                                //i (might) need to implement this fully.. I might need to make use of javap to do this.
                                dataTypes.add(new UMLInterface(line, residingPackage, null, new ArrayList<>(), new ArrayList<>(), new ArrayList<>()));
                                break;
                            case "class":
                            case "exception":
                            case "error":
                                dataTypes.add(new UMLClass(line, residingPackage, null, new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), false,
                                        new ArrayList<>(), new ArrayList<>(), "class"));
                                break;
                            case "enum":
                                dataTypes.add(new UMLClass(line, residingPackage, null, new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), false,
                                        new ArrayList<>(), new ArrayList<>(), "enum"));
                                break;
                        }
                }
            }
            in.close();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    private List<String> getPackageFromDataTypeLine(String packageListing){
        //todo.. for this I need to add something to teh file format I have already started with..
        //its tricky because some types, such as Character.UnicodeBlock, are types and not packages.
        //but the typical package structure is 'foo.bar.bar2'
        return null;
    }


}
