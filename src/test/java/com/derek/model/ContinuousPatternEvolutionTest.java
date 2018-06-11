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
package com.derek.model;

import com.derek.model.patterns.PatternInstance;
import com.google.common.collect.Table;
import com.google.common.collect.TreeBasedTable;
import com.google.common.graph.GraphBuilder;
import com.google.common.graph.MutableGraph;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.*;

public class ContinuousPatternEvolutionTest {

    private Model model;

    private PatternInstance firstContinuous;
    private PatternInstance secondContinuous;
    private PatternInstance thirdContinuous;
    private PatternInstance fourthContinuous;
    private MutableGraph<PatternInstance> continuousMutableGraph;


    @Before
    public void buildContinuousMockObjects(){

        Pair<String, String> _firstR1 = new ImmutablePair<>("Creator", "com.google.common.base.CommonPattern");
        Pair<String, String> _firstR2 = new ImmutablePair<>("FactoryMethod()", "com.google.common.base.CommonPattern::matcher(java.lang.CharSequence):com.google.common.base.CommonMatcher");

        Pair<String, String> _secondR1 = new ImmutablePair<>("Creator", "com.google.common.base.CommonPattern");
        Pair<String, String> _secondR2 = new ImmutablePair<>("FactoryMethod()", "com.google.common.base.CommonPattern::matcher(java.lang.CharSequence):com.google.common.base.CommonMatcher");
        Pair<String, String> _secondR3 = new ImmutablePair<>("FactoryMethod()", "com.google.common.base.CommonPattern::matcher(java.lang.CharSequence):com.google.common.base.CommonMatcher2");

        Pair<String, String> _thirdR1 = new ImmutablePair<>("Creator", "com.google.common.base.CommonPattern");
        Pair<String, String> _thirdR2 = new ImmutablePair<>("FactoryMethod()", "com.google.common.base.CommonPattern::matcher(java.lang.CharSequence):com.google.common.base.CommonMatcher");
        Pair<String, String> _thirdR3 = new ImmutablePair<>("FactoryMethod()", "com.google.common.base.CommonPattern::matcher(java.lang.CharSequence):com.google.common.base.CommonMatcher2");
        Pair<String, String> _thirdR4 = new ImmutablePair<>("FactoryMethod()", "com.google.common.base.CommonPattern::matcher(java.lang.CharSequence):com.google.common.base.CommonMatcher3");

        Pair<String, String> _fourthR1 = new ImmutablePair<>("Creator", "com.google.common.base.CommonPattern");
        Pair<String, String> _fourthR2 = new ImmutablePair<>("FactoryMethod()", "com.google.common.base.CommonPattern::matcher(java.lang.CharSequence):com.google.common.base.CommonMatcher");
        Pair<String, String> _fourthR3 = new ImmutablePair<>("FactoryMethod()", "com.google.common.base.CommonPattern::matcher(java.lang.CharSequence):com.google.common.base.CommonMatcher2");
        Pair<String, String> _fourthR4 = new ImmutablePair<>("FactoryMethod()", "com.google.common.base.CommonPattern::matcher(java.lang.CharSequence):com.google.common.base.CommonMatcher3");
        Pair<String, String> _fourthR5 = new ImmutablePair<>("FactoryMethod()", "com.google.common.base.CommonPattern::matcher(java.lang.CharSequence):com.google.common.base.CommonMatcher4");

        List<Pair<String, String>> _firstListOfPatternRoles = new ArrayList<>();
        _firstListOfPatternRoles.add(_firstR1);
        _firstListOfPatternRoles.add(_firstR2);

        List<Pair<String, String>> _secondListOfPatternRoles = new ArrayList<>();
        _secondListOfPatternRoles.add(_secondR1);
        _secondListOfPatternRoles.add(_secondR2);
        _secondListOfPatternRoles.add(_secondR3);

        List<Pair<String, String>> _thirdListOfPatternRoles = new ArrayList<>();
        _thirdListOfPatternRoles.add(_thirdR1);
        _thirdListOfPatternRoles.add(_thirdR2);
        _thirdListOfPatternRoles.add(_thirdR3);
        _thirdListOfPatternRoles.add(_thirdR4);

        List<Pair<String, String>> _fourthListOfPatternRoles = new ArrayList<>();
        _fourthListOfPatternRoles.add(_fourthR1);
        _fourthListOfPatternRoles.add(_fourthR2);
        _fourthListOfPatternRoles.add(_fourthR3);
        _fourthListOfPatternRoles.add(_fourthR4);
        _fourthListOfPatternRoles.add(_fourthR5);

        SoftwareVersion v1 = new SoftwareVersion(1);
        SoftwareVersion v2 = new SoftwareVersion(2);
        SoftwareVersion v3 = new SoftwareVersion(3);
        SoftwareVersion v4 = new SoftwareVersion(4);
        List<SoftwareVersion> versions = new ArrayList<>();
        versions.add(v1);
        versions.add(v2);
        versions.add(v3);
        versions.add(v4);
        model = new Model(versions);

        firstContinuous = new PatternInstance(_firstListOfPatternRoles, PatternType.FACTORY_METHOD, v1);
        secondContinuous = new PatternInstance(_secondListOfPatternRoles, PatternType.FACTORY_METHOD, v2);
        thirdContinuous = new PatternInstance(_thirdListOfPatternRoles, PatternType.FACTORY_METHOD, v3);
        fourthContinuous = new PatternInstance(_fourthListOfPatternRoles, PatternType.FACTORY_METHOD, v4);

        ArrayList<PatternInstance> mockFirst = new ArrayList<>();
        mockFirst.add(firstContinuous);
        ArrayList<PatternInstance> mockSecond= new ArrayList<>();
        mockSecond.add(secondContinuous);
        ArrayList<PatternInstance> mockThird = new ArrayList<>();
        mockThird.add(thirdContinuous);
        ArrayList<PatternInstance> mockFourth= new ArrayList<>();
        mockFourth.add(fourthContinuous);

        Table<SoftwareVersion, PatternType, List<PatternInstance>> continuousPatternSummaryTable = TreeBasedTable.create();
        continuousPatternSummaryTable.put(v1, PatternType.FACTORY_METHOD, mockFirst);
        continuousPatternSummaryTable.put(v2, PatternType.FACTORY_METHOD, mockSecond);
        continuousPatternSummaryTable.put(v3, PatternType.FACTORY_METHOD, mockThird);
        continuousPatternSummaryTable.put(v4, PatternType.FACTORY_METHOD, mockFourth);
        model.setPatternSummaryTable(continuousPatternSummaryTable);

        continuousMutableGraph =  GraphBuilder.directed().allowsSelfLoops(false).build();;

        continuousMutableGraph.addNode(firstContinuous);
        continuousMutableGraph.addNode(secondContinuous);
        continuousMutableGraph.addNode(thirdContinuous);
        continuousMutableGraph.addNode(fourthContinuous);
        continuousMutableGraph.putEdge(firstContinuous, secondContinuous);
        continuousMutableGraph.putEdge(secondContinuous, thirdContinuous);
        continuousMutableGraph.putEdge(thirdContinuous, fourthContinuous);
    }

    //same comment as tests in 'skipped pattern evolution' test
    @Ignore
    @Test
    public void testContinuousPatternEvolution() throws Exception {
        //test for a single pattern instance being updated in all versions, where the software versions are 'continuous'
        MutableGraph<PatternInstance> patternEvolution = model.buildPatternEvolution(firstContinuous);

        assertEquals(patternEvolution.hasEdgeConnecting(firstContinuous, secondContinuous), true);
        assertEquals(patternEvolution.hasEdgeConnecting(secondContinuous, thirdContinuous), true);
        assertEquals(patternEvolution.hasEdgeConnecting(thirdContinuous, fourthContinuous), true);
        assertEquals(patternEvolution.hasEdgeConnecting(firstContinuous, thirdContinuous), false);
        assertEquals(patternEvolution.hasEdgeConnecting(firstContinuous, fourthContinuous), false);
        assertEquals(patternEvolution.hasEdgeConnecting(secondContinuous, fourthContinuous), false);
    }


}