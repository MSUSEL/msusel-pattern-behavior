package com.derek;

import com.derek.rbml.RBMLMapping;
import com.derek.rbml.Role;
import com.derek.rbml.SPS;
import com.derek.uml.Relationship;
import com.derek.uml.UMLAttribute;
import com.derek.uml.UMLClassifier;
import com.derek.uml.UMLOperation;
import javafx.util.Pair;
import lombok.Getter;

import java.text.DecimalFormat;
import java.util.List;

import static java.lang.Math.round;

@Getter
public class MetricSuite {

    private PatternMapper patternMapper;
    private List<RBMLMapping> rbmlMappings;
    private SPS sps;

    //number of participating classes
    private int numParticipatingClasses = 0;

    //number of conforming roles, and nonconforming roles, respectively
    private int numConformingRoles = 0;
    private int numNonConformingRoles = 0;

    //ssize2 metric for pattern instance (sum of fields and methods for all classes in a pattern)
    private int ssize2 = 0;

    //afferent (incoming) coupling to pattern as a whole.
    private int afferentCoupling = 0;

    //efferent (outgoing) coupling from pattern to anything not pattern-related
    private int efferentCoupling = 0;

    //coupling between pattern objects.
    private int couplingBetweenPatternClasses = 0;

    //pattern integrity, defined as (numConformingRoles)/(numConformingRoles + numNonConformingRoles)
    private String patternIntegrity = "";

    //pattern instability, defined as (efferentCoupling) / (afferentCoupling + efferentCoupling)
    private String patternInstability = "";

    public MetricSuite(List<RBMLMapping> rbmlMappings, PatternMapper patternMapper, SPS sps){
        this.patternMapper = patternMapper;
        this.rbmlMappings = rbmlMappings;
        this.sps = sps;
        calculate();
    }


    private void calculate(){
        calcNumParticipatingClasses();
        calcNumConformingRoles();
        calcNumNonConformingRoles();
        calcSSize2();
        calcAfferentCoupling();
        calcEfferentCoupling();
        calcCouplingPatternClasses();
        calcPatternIntegrity();
        calcPatternInstability();
    }

    private void calcNumParticipatingClasses(){
        numParticipatingClasses = patternMapper.getClassifierModelBlocks().size();
    }

    private void calcNumConformingRoles(){
        for (RBMLMapping rbmlMapping : rbmlMappings){
            for (Pair<String, UMLClassifier> classifier : patternMapper.getClassifierModelBlocks()){
                if (rbmlMapping.getUmlArtifact().equals(classifier.getValue())){
                    //found conforming classifier role
                    numConformingRoles++;
                    System.out.println(classifier.getValue().getName() + " has a mapping to " + rbmlMapping.getRole().getName());
                }
            }
            for (Pair<String, UMLOperation> operation : patternMapper.getOperationModelBlocks()){
                if (rbmlMapping.getUmlArtifact().equals(operation.getValue())){
                    //found conforming operation role
                    numConformingRoles++;
                    System.out.println(operation.getValue().getName() + " has a mapping to " + rbmlMapping.getRole().getName());
                }
            }
            for (Pair<String, UMLAttribute> attribute : patternMapper.getAttributeModelBlocks()){
                if (rbmlMapping.getUmlArtifact().equals(attribute.getValue())){
                    //found conforming attribute role
                    numConformingRoles++;
                    System.out.println(attribute.getValue().getName() + " has a mapping to " + rbmlMapping.getRole().getName());
                }
            }
            for (Pair<UMLClassifier, UMLClassifier> association : patternMapper.getRelationships(Relationship.ASSOCIATION)){
                if (rbmlMapping.getUmlArtifact().equals(association)){
                    //will be a pair.
                    numConformingRoles++;
                    System.out.println("Association pair " + association.getKey() + " to " + association.getValue() + " has a mapping to " + rbmlMapping.getUmlArtifact());
                }
            }
            for (Pair<UMLClassifier, UMLClassifier> generalization : patternMapper.getRelationships(Relationship.GENERALIZATION)){
                if (rbmlMapping.getUmlArtifact().equals(generalization)){
                    //will be a pair.
                    numConformingRoles++;
                    System.out.println("Generalization pair " + generalization.getKey() + " to " + generalization.getValue() + " has a mapping to " + rbmlMapping.getUmlArtifact());
                }
            }
            for (Pair<UMLClassifier, UMLClassifier> dependency : patternMapper.getRelationships(Relationship.DEPENDENCY)){
                if (rbmlMapping.getUmlArtifact().equals(dependency)){
                    //will be a pair.
                    numConformingRoles++;
                    System.out.println("Dependency pair " + dependency.getKey() + " to " + dependency.getValue() + " has a mapping to " + rbmlMapping.getUmlArtifact());
                }
            }
            for (Pair<UMLClassifier, UMLClassifier> realization : patternMapper.getRelationships(Relationship.REALIZATION)){
                if (rbmlMapping.getUmlArtifact().equals(realization)){
                    //will be a pair.
                    numConformingRoles++;
                    System.out.println("Realization pair " + realization.getKey() + " to " + realization.getValue() + " has a mapping to " + rbmlMapping.getUmlArtifact());
                }
            }
        }
    }

    private void calcNumNonConformingRoles(){
        for (Role role : sps.getAllRoles()){
            boolean roleHasBeenMapped = false;
            for (RBMLMapping rbmlMapping : rbmlMappings){
                if (role.equals(rbmlMapping.getRole())){
                    roleHasBeenMapped = true;
                }
            }
            if (!roleHasBeenMapped){
                numNonConformingRoles++;
            }
        }
    }

    private void calcSSize2(){
        for (Pair<String, UMLClassifier> umlClassifierPair : patternMapper.getClassifierModelBlocks()){
            UMLClassifier umlClassifier = umlClassifierPair.getValue();
            for (UMLAttribute attribute : umlClassifier.getAttributes()){
                ssize2++;
            }
            for (UMLOperation operation : umlClassifier.getOperations()){
                ssize2++;
            }
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
    private void calcPatternIntegrity(){
        DecimalFormat df2 = new DecimalFormat("#.##");
        double unformattedIntegrity = ((double)numConformingRoles) / (numConformingRoles + numNonConformingRoles);
        patternIntegrity = df2.format(unformattedIntegrity);
    }

    private void calcPatternInstability(){
        DecimalFormat df2 = new DecimalFormat("#.##");
        double unformattedInstability = ((double)efferentCoupling) / (afferentCoupling + efferentCoupling);
        patternInstability = df2.format(unformattedInstability);
    }

    private boolean isClassifierInPatternInstance(UMLClassifier tester){
        for (Pair<String, UMLClassifier> umlClassifierPair: patternMapper.getClassifierModelBlocks()){
            if (umlClassifierPair.getValue().equals(tester)){
                return true;
            }
        }
        return false;
    }

    public void printSummary(){
        String separator = ", ";
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(Main.projectID + separator);
        stringBuilder.append(Main.currentVersion + separator);
        stringBuilder.append(patternMapper.getPi().getPatternType() + separator);
        stringBuilder.append(" Pattern instance ID : TODO" + separator);
        stringBuilder.append(this.getNumParticipatingClasses() + separator);
        stringBuilder.append(this.getNumConformingRoles() + separator);
        stringBuilder.append(this.getNumNonConformingRoles() + separator);
        stringBuilder.append(this.getSsize2() + separator);
        stringBuilder.append(this.getAfferentCoupling() + separator);
        stringBuilder.append(this.getEfferentCoupling() + separator);
        stringBuilder.append(this.getCouplingBetweenPatternClasses() + separator);
        stringBuilder.append(this.getPatternIntegrity() + separator);
        stringBuilder.append(this.getPatternInstability() + separator);
        stringBuilder.append("\n");
        System.out.println(stringBuilder.toString());
    }

}
