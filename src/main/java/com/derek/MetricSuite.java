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
package com.derek;

import com.derek.rbml.*;
import com.derek.uml.*;
import lombok.Getter;
import org.apache.commons.lang3.tuple.Pair;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

@Getter
public class MetricSuite {

    private PatternMapper patternMapper;
    private List<RBMLMapping> rbmlStructuralMappings;
    private SPS sps;
    private List<Pair<UMLOperation, BehaviorConformance>> rbmlBehavioralMappings;
    private IPS ips;

    //number of participating classes
    private int numParticipatingClasses = 0;

    //number of conforming structural roles, and nonconforming structural roles, respectively
    private int numConformingStructuralRoles = 0;
    private int numNonConformingStructuralRoles = 0;

    //number of conforming and nonconforming behavioral roles.
    private int numConformingBehavioralRoles = 0;
    private int numNonConformingBehavioralRoles = 0;

    private int numConformingRolesTotal = 0;
    private int numNonConformingRolesTotal = 0;

    //ssize2 metric for pattern instance (sum of fields and methods for all classes in a pattern)
    private int ssize2 = 0;

    //afferent (incoming) coupling to pattern as a whole.
    private int afferentCoupling = 0;

    //efferent (outgoing) coupling from pattern to anything not pattern-related
    private int efferentCoupling = 0;

    //coupling between pattern objects.
    private int couplingBetweenPatternClasses = 0;

    //pattern integrity, defined as (numConformingStructuralRoles)/(numConformingStructuralRoles + numNonConformingStructuralRoles)
    private String patternStructuralIntegrity = "";
    private String patternBehavioralIntegrity = "";
    private String patternIntegrity = "";

    //pattern instability, defined as (efferentCoupling) / (afferentCoupling + efferentCoupling)
    private String patternInstability = "";


    private GrimeSuite grimeSuite;


    public MetricSuite(List<RBMLMapping> rbmlStructuralMappings, PatternMapper patternMapper, SPS sps, List<Pair<UMLOperation, BehaviorConformance>> rbmlBehavioralMappings, IPS ips){
        this.patternMapper = patternMapper;
        this.rbmlStructuralMappings = rbmlStructuralMappings;
        this.sps = sps;
        this.rbmlBehavioralMappings = rbmlBehavioralMappings;
        this.ips = ips;
        grimeSuite = new GrimeSuite(patternMapper, rbmlStructuralMappings, sps, rbmlBehavioralMappings, ips);
        calculate();
    }

    private void calculate(){
        calcNumParticipatingClasses();
        calcNumConformingStructuralRoles();
        calcNumNonConformingStructuralRoles();
        calcNumConformingBehavioralRoles();
        calcNumNonConformingBehavioralRoles();
        calcNumConformingRolesTotal();
        calcNumNonConformingRolesTotal();
        calcSSize2();
        calcAfferentCoupling();
        calcEfferentCoupling();
        calcCouplingPatternClasses();
        calcPatternStructuralIntegrity();
        calcPatternBehavioralIntegrity();
        calcPatternIntegrity();
        calcPatternInstability();
    }

    private void calcNumParticipatingClasses(){
        numParticipatingClasses = patternMapper.getAllParticipatingClasses().size();
    }

    private void calcNumConformingStructuralRoles(){
        for (RBMLMapping rbmlMapping : rbmlStructuralMappings){
            for (Pair<String, UMLClassifier> classifier : patternMapper.getClassifierModelBlocks()){
                if (rbmlMapping.getUmlArtifact().equals(classifier.getValue())){
                    //found conforming classifier role
                    numConformingStructuralRoles++;
                }
            }
            for (Pair<String, UMLOperation> operation : patternMapper.getOperationModelBlocks()){
                if (rbmlMapping.getUmlArtifact().equals(operation.getValue())){
                    //found conforming operation role
                    numConformingStructuralRoles++;
                }
            }
            for (Pair<String, UMLAttribute> attribute : patternMapper.getAttributeModelBlocks()){
                if (rbmlMapping.getUmlArtifact().equals(attribute.getValue())){
                    //found conforming attribute role
                    numConformingStructuralRoles++;
                }
            }
            for (Pair<UMLClassifier, UMLClassifier> association : patternMapper.getRelationships(Relationship.ASSOCIATION)){
                if (rbmlMapping.getUmlArtifact().equals(association)){
                    //will be a pair.
                    numConformingStructuralRoles++;
                }
            }
            for (Pair<UMLClassifier, UMLClassifier> generalization : patternMapper.getRelationships(Relationship.GENERALIZATION)){
                if (rbmlMapping.getUmlArtifact().equals(generalization)){
                    //will be a pair.
                    numConformingStructuralRoles++;
                }
            }
            for (Pair<UMLClassifier, UMLClassifier> dependency : patternMapper.getRelationships(Relationship.DEPENDENCY)){
                if (rbmlMapping.getUmlArtifact().equals(dependency)){
                    //will be a pair.
                    numConformingStructuralRoles++;
                }
            }
            for (Pair<UMLClassifier, UMLClassifier> realization : patternMapper.getRelationships(Relationship.REALIZATION)){
                if (rbmlMapping.getUmlArtifact().equals(realization)){
                    //will be a pair.
                    numConformingStructuralRoles++;
                }
            }
        }
    }

    private void calcNumNonConformingStructuralRoles(){
        for (Role role : sps.getAllRoles()){
            boolean roleHasBeenMapped = false;
            for (RBMLMapping rbmlMapping : rbmlStructuralMappings){
                if (role.equals(rbmlMapping.getRole())){
                    roleHasBeenMapped = true;
                }
            }
            if (!roleHasBeenMapped){
                numNonConformingStructuralRoles++;
            }
        }
    }

    private void calcNumConformingBehavioralRoles(){
        List<InteractionRole> seenAlready = new ArrayList<>();
        for (Pair<UMLOperation, BehaviorConformance> mapping : rbmlBehavioralMappings){
            for (Pair<CallTreeNode, InteractionRole> pair : mapping.getRight().getRoleMap()){
                if (!seenAlready.contains(pair.getRight())){
                    seenAlready.add(pair.getRight());
                }
            }
        }
        numConformingBehavioralRoles = seenAlready.size();
    }

    private void calcNumNonConformingBehavioralRoles(){
        List<InteractionRole> seenAlready = new ArrayList<>();
        for (Pair<UMLOperation, BehaviorConformance> mapping : rbmlBehavioralMappings){
            for (InteractionRole interactionRole : mapping.getRight().getBehavioralViolations()){
                if (!seenAlready.contains(interactionRole)){
                    seenAlready.add(interactionRole);
                }
            }
        }
        numNonConformingBehavioralRoles = seenAlready.size();
    }
    
    private void calcNumConformingRolesTotal(){
        numConformingRolesTotal = numConformingBehavioralRoles + numConformingStructuralRoles;

    }

    private void calcNumNonConformingRolesTotal(){
        numNonConformingRolesTotal = numNonConformingBehavioralRoles + numNonConformingStructuralRoles;
    }

    private void calcSSize2(){
        for (Pair<String, UMLClassifier> umlClassifierPair : patternMapper.getClassifierModelBlocks()){
            UMLClassifier umlClassifier = umlClassifierPair.getValue();
            ssize2 += umlClassifier.getAttributes().size();
            ssize2 += umlClassifier.getOperations().size();
        }
    }

    private void calcAfferentCoupling(){
        //afferent means the value of the pair is a pattern class. (nonpattern_class -> pattern_class)
        for (Pair<UMLClassifier, UMLClassifier> classifierPair : patternMapper.getRelationships(Relationship.ASSOCIATION)){
            if (!isClassifierInPatternInstance(classifierPair.getKey())){
                afferentCoupling++;
            }
        }
    }

    private void calcEfferentCoupling(){
        //efferent means the key of the pair is a pattern class. (pattern_class -> nonpattern_class)
        for (Pair<UMLClassifier, UMLClassifier> classifierPair : patternMapper.getRelationships(Relationship.ASSOCIATION)){
            if (!isClassifierInPatternInstance(classifierPair.getValue())){
                efferentCoupling++;
            }
        }
    }

    private void calcCouplingPatternClasses(){
        for (Pair<UMLClassifier, UMLClassifier> classifierPair : patternMapper.getRelationships(Relationship.ASSOCIATION)){
            if (isClassifierInPatternInstance(classifierPair.getKey()) && isClassifierInPatternInstance(classifierPair.getValue())){
                couplingBetweenPatternClasses++;
            }
        }
    }

    //defined as percentage of conforming pattern roles out of whole.
    private void calcPatternStructuralIntegrity(){
        DecimalFormat df2 = new DecimalFormat("#.##");
        double unformattedIntegrity = ((double) numConformingStructuralRoles) / (numConformingStructuralRoles + numNonConformingStructuralRoles);
        patternStructuralIntegrity = df2.format(unformattedIntegrity);
    }

    //defined as percentage of conforming pattern roles out of whole.
    private void calcPatternBehavioralIntegrity(){
        DecimalFormat df2 = new DecimalFormat("#.##");
        double unformattedIntegrity = ((double) numConformingBehavioralRoles) / (numConformingBehavioralRoles + numNonConformingBehavioralRoles);
        patternBehavioralIntegrity = df2.format(unformattedIntegrity);
    }

    //defined as percentage of conforming pattern roles out of whole.
    private void calcPatternIntegrity(){
        DecimalFormat df2 = new DecimalFormat("#.##");
        double unformattedIntegrity = ((double) numConformingStructuralRoles + (double) numConformingBehavioralRoles) / (numConformingStructuralRoles + numNonConformingStructuralRoles + numConformingBehavioralRoles + numNonConformingBehavioralRoles);
        patternIntegrity = df2.format(unformattedIntegrity);
    }


    private void calcPatternInstability(){
        DecimalFormat df2 = new DecimalFormat("#.##");
        if (afferentCoupling + efferentCoupling != 0){
            double unformattedInstability = ((double)efferentCoupling) / (afferentCoupling + efferentCoupling);
            patternInstability = df2.format(unformattedInstability);
        }else{
            //would get a divide by 0 error here
            //this happens when a pattern-class only has dependencies on 3rd party libs, and no class depends on this class...
            //happens a few times with Template method implementations
            patternInstability = "0";
        }

    }

    private boolean isClassifierInPatternInstance(UMLClassifier tester){
        for (Pair<String, UMLClassifier> umlClassifierPair: patternMapper.getClassifierModelBlocks()){
            if (umlClassifierPair.getValue().equals(tester)){
                return true;
            }
        }
        return false;
    }


    public String getSummary(){
        String separator = "\t";
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(Main.projectID + separator);
        stringBuilder.append(patternMapper.getPi().getSoftwareVersion().getVersionNum() + separator);
        stringBuilder.append(patternMapper.getPi().getPatternType() + separator);
        stringBuilder.append(patternMapper.getPi().getUniqueID() + separator);
        stringBuilder.append(this.getNumParticipatingClasses() + separator);
        stringBuilder.append(this.getNumConformingStructuralRoles() + separator);
        stringBuilder.append(this.getNumNonConformingStructuralRoles() + separator);
        stringBuilder.append(this.getNumConformingBehavioralRoles() + separator);
        stringBuilder.append(this.getNumNonConformingBehavioralRoles() + separator);
        stringBuilder.append(this.getNumConformingRolesTotal() + separator);
        stringBuilder.append(this.getNumNonConformingRolesTotal() + separator);
        stringBuilder.append(this.getSsize2() + separator);
        stringBuilder.append(this.getAfferentCoupling() + separator);
        stringBuilder.append(this.getEfferentCoupling() + separator);
        stringBuilder.append(this.getCouplingBetweenPatternClasses() + separator);
        stringBuilder.append(this.getPatternStructuralIntegrity() + separator);
        stringBuilder.append(this.getPatternBehavioralIntegrity() + separator);
        stringBuilder.append(this.getPatternIntegrity() + separator);
        stringBuilder.append(this.getPatternInstability());
        stringBuilder.append("\n");
        return stringBuilder.toString();
    }

}
