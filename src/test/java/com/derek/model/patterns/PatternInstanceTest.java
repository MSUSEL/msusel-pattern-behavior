package com.derek.model.patterns;

import com.derek.model.PatternType;
import com.derek.model.SoftwareVersion;
import javafx.util.Pair;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class PatternInstanceTest {

    private PatternInstance _this1;
    private PatternInstance _other1;
    private PatternInstance _other2;

    @Before
    public void buildMockObjects(){

        Pair<String, String> _thisP1 = new Pair<>("Creator", "com.google.common.base.CommonPattern");
        Pair<String, String> _thisP2 = new Pair<>("FactoryMethod()", "com.google.common.base.CommonPattern::matcher(java.lang.CharSequence):com.google.common.base.CommonMatcher");

        Pair<String, String> _other1P1 = new Pair<>("Creator", "com.google.common.base.CommonPattern");
        Pair<String, String> _other1P2 = new Pair<>("FactoryMethod()", "com.google.common.base.CommonPattern::matcher(java.lang.CharSequence):com.google.common.base.CommonMatcher");
        Pair<String, String> _other1P3 = new Pair<>("FactoryMethod()", "com.google.common.base.Optional::transform():com.google.common.base.Optional");

        Pair<String, String> _other2P1 = new Pair<>("Creator", "com.google.common.base.PatternCompiler");
        Pair<String, String> _other2P2 = new Pair<>("FactoryMethod()", "com.google.common.base.CommonPattern::matcher(java.lang.CharSequence):com.google.common.base.CommonMatcher");

        //version doesn't matter
        SoftwareVersion version = new SoftwareVersion(1);

        List<Pair<String, String>> _this1ListOfPatternRoles = new ArrayList<>();
        _this1ListOfPatternRoles.add(_thisP1);
        _this1ListOfPatternRoles.add(_thisP2);

        List<Pair<String, String>> _other1ListOfPatternRoles = new ArrayList<>();
        _other1ListOfPatternRoles.add(_other1P1);
        _other1ListOfPatternRoles.add(_other1P2);
        _other1ListOfPatternRoles.add(_other1P3);

        List<Pair<String, String>> _other2ListOfPatternRoles = new ArrayList<>();
        _other2ListOfPatternRoles.add(_other2P1);
        _other2ListOfPatternRoles.add(_other2P2);

        _this1 = new PatternInstance(_this1ListOfPatternRoles, PatternType.FACTORY_METHOD, version);
        _other1 = new PatternInstance(_other1ListOfPatternRoles, PatternType.FACTORY_METHOD, version);
        _other2 = new PatternInstance(_other2ListOfPatternRoles, PatternType.FACTORY_METHOD, version);
    }


    @Test
    public void isInstanceEqual() throws Exception {
        assertEquals(_this1.isInstanceEqual(_other1), true);
        assertEquals(_this1.isInstanceEqual(_other2), false);
    }

    @Test
    public void getValueOfMajorRole() throws Exception{
        assertEquals(_this1.getValueOfMajorRole(_this1), "com.google.common.base.CommonPattern");
        assertEquals(_other1.getValueOfMajorRole(_other1), "com.google.common.base.CommonPattern");
        assertEquals(_other2.getValueOfMajorRole(_other2), "com.google.common.base.PatternCompiler");
    }

    @Test
    public void getMajorRole() throws Exception {

    }

}