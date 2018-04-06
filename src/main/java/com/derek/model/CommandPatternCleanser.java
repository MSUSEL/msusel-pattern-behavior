package com.derek.model;

import com.derek.model.patterns.PatternInstance;

import java.util.ArrayList;
import java.util.List;

/***
 * This class is reponsible for cleansing the command pattern.. will be mostly a utility class
 * The issue with the tool (dp detection tool) is that it reports each fulfilling role of a pattern as a separate pattern instance.
 * This is not true.
 *
 */
public class CommandPatternCleanser {

    public List<PatternInstance> cleansePattern(List<PatternInstance> uncleansedPatternInstance){
        List<PatternInstance> cleanPatterns = new ArrayList<>();

        for (PatternInstance uncleansed : uncleansedPatternInstance){
            if (cleanPatterns.isEmpty()) {
                //add first pattern seen. order doesn't matter here.
                cleanPatterns.add(uncleansed);
            } else {
                //Receiver is same, but the ConcreteCommand is different. Although it will be from the same inheritance hierarchy.
            }
        }
        return cleanPatterns;
    }
    /**
     * example data:
     * 		<instance>
     * 			<role name="Receiver" element="CH.ifa.draw.framework.DrawingView" />
     * 			<role name="ConcreteCommand" element="CH.ifa.draw.figures.GroupCommand" />
     * 			<role name="receiver" element="CH.ifa.draw.figures.GroupCommand::fView:CH.ifa.draw.framework.DrawingView" />
     * 			<role name="Execute()" element="CH.ifa.draw.figures.GroupCommand::execute():void" />
     * 		</instance>
     * 		<instance>
     * 			<role name="Receiver" element="CH.ifa.draw.framework.DrawingView" />
     * 			<role name="ConcreteCommand" element="CH.ifa.draw.figures.InsertImageCommand" />
     * 			<role name="receiver" element="CH.ifa.draw.figures.InsertImageCommand::fView:CH.ifa.draw.framework.DrawingView" />
     * 			<role name="Execute()" element="CH.ifa.draw.figures.InsertImageCommand::execute():void" />
     * 		</instance>
     * 		<instance>
     * 			<role name="Receiver" element="CH.ifa.draw.framework.DrawingView" />
     * 			<role name="ConcreteCommand" element="CH.ifa.draw.figures.UngroupCommand" />
     * 			<role name="receiver" element="CH.ifa.draw.figures.UngroupCommand::fView:CH.ifa.draw.framework.DrawingView" />
     * 			<role name="Execute()" element="CH.ifa.draw.figures.UngroupCommand::execute():void" />
     * 		</instance>
     */
}
