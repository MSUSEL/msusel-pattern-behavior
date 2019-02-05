/**
 * MIT License
 *
 * Copyright (c) 2017 Montana State University, Gianforte School of Computing
 * Software Engineering Laboratory
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
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
                                dataTypes.add(new UMLInterface(line, residingPackage, null, new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), "language_interface"));
                                break;
                            case "class":
                            case "exception":
                            case "error":
                                dataTypes.add(new UMLClass(line, residingPackage, null, new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), false,
                                        new ArrayList<>(), new ArrayList<>(), "language_class"));
                                break;
                            case "enum":
                                dataTypes.add(new UMLClass(line, residingPackage, null, new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), false,
                                        new ArrayList<>(), new ArrayList<>(), "language_enum"));
                                break;
                        }
                }
            }
            in.close();
        }catch(Exception e){
            e.printStackTrace();
        }
    }


}
