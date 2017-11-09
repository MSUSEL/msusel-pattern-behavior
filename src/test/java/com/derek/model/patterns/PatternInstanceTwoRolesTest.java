package com.derek.model.patterns;

import com.derek.model.PatternType;
import com.derek.model.SoftwareVersion;
import javafx.util.Pair;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class PatternInstanceTwoRolesTest {

    private PatternInstance _this1;
    private PatternInstance _other1;
    private PatternInstance _other2;
    private PatternInstance _other3;

    @Before
    public void buildMockObjects(){
        Pair<String, String> _thisP1 = new Pair<>("Adaptee", "com.google.common.util.concurrent.ExecutionList");
        Pair<String, String> _thisP2 = new Pair<>("Adapter", "com.google.common.util.concurrent.JdkFutureAdapters$ListenableFutureAdapter");
        Pair<String, String> _thisP3 = new Pair<>("Request()", "com.google.common.util.concurrent.JdkFutureAdapters$ListenableFutureAdapter::executionList:com.google.common.util.concurrent.ExecutionList");

        //happy path
        Pair<String, String> _other1P1 = new Pair<>("Adaptee", "com.google.common.util.concurrent.ExecutionList");
        Pair<String, String> _other1P2 = new Pair<>("Adapter", "com.google.common.util.concurrent.JdkFutureAdapters$ListenableFutureAdapter");
        Pair<String, String> _other1P3 = new Pair<>("Request()", "com.google.common.util.concurrent.JdkFutureAdapters$ListenableFutureAdapter::executionList:com.google.common.util.concurrent.ExecutionList");
        Pair<String, String> _other1P4 = new Pair<>("Request()", "com.google.common.util.concurrent.JdkFutureAdapters$ListenableFutureAdapter::executionList:com.google.common.util.concurrent.ExecutionList2");

        //first major role doesn't match
        Pair<String, String> _other2P1 = new Pair<>("Adaptee", "com.google.common.util.concurrent.ExecutionList2");
        Pair<String, String> _other2P2 = new Pair<>("Adapter", "com.google.common.util.concurrent.JdkFutureAdapters$ListenableFutureAdapter");
        Pair<String, String> _other2P3 = new Pair<>("Request()", "com.google.common.util.concurrent.JdkFutureAdapters$ListenableFutureAdapter::executionList:com.google.common.util.concurrent.ExecutionList");

        //second major role doesn't match
        Pair<String, String> _other3P1 = new Pair<>("Adaptee", "com.google.common.util.concurrent.ExecutionList");
        Pair<String, String> _other3P2 = new Pair<>("Adapter", "com.google.common.util.concurrent.JdkFutureAdapters$ListenableFutureAdapter2");
        Pair<String, String> _other3P3 = new Pair<>("Request()", "com.google.common.util.concurrent.JdkFutureAdapters$ListenableFutureAdapter::executionList:com.google.common.util.concurrent.ExecutionList");

        //version doesn't matter
        SoftwareVersion version = new SoftwareVersion(1);

        List<Pair<String, String>> _this1ListOfPatternRoles = new ArrayList<>();
        _this1ListOfPatternRoles.add(_thisP1);
        _this1ListOfPatternRoles.add(_thisP2);
        _this1ListOfPatternRoles.add(_thisP3);

        List<Pair<String, String>> _other1ListOfPatternRoles = new ArrayList<>();
        _other1ListOfPatternRoles.add(_other1P1);
        _other1ListOfPatternRoles.add(_other1P2);
        _other1ListOfPatternRoles.add(_other1P3);
        _other1ListOfPatternRoles.add(_other1P4);

        List<Pair<String, String>> _other2ListOfPatternRoles = new ArrayList<>();
        _other2ListOfPatternRoles.add(_other2P1);
        _other2ListOfPatternRoles.add(_other2P2);
        _other2ListOfPatternRoles.add(_other2P3);

        List<Pair<String, String>> _other3ListOfPatternRoles = new ArrayList<>();
        _other3ListOfPatternRoles.add(_other3P1);
        _other3ListOfPatternRoles.add(_other3P2);
        _other3ListOfPatternRoles.add(_other3P3);


        _this1 = new PatternInstance(_this1ListOfPatternRoles, PatternType.OBJECT_ADAPTER, version);
        _other1 = new PatternInstance(_other1ListOfPatternRoles, PatternType.OBJECT_ADAPTER, version);
        _other2 = new PatternInstance(_other2ListOfPatternRoles, PatternType.OBJECT_ADAPTER, version);
        _other3 = new PatternInstance(_other3ListOfPatternRoles, PatternType.OBJECT_ADAPTER, version);
    }


    @Test
    public void isInstanceEqual() throws Exception {
        assertEquals(_this1.isInstanceEqual(_other1), true);
        assertEquals(_this1.isInstanceEqual(_other2), false);
        assertEquals(_this1.isInstanceEqual(_other3), false);
    }

    @Test
    public void getValueOfMajorRole() throws Exception{
        assertEquals(_this1.getValueOfMajorRole(_this1), "com.google.common.util.concurrent.ExecutionList");
        assertEquals(_other1.getValueOfMajorRole(_other1), "com.google.common.util.concurrent.ExecutionList");
        assertEquals(_other2.getValueOfMajorRole(_other2), "com.google.common.util.concurrent.ExecutionList2");
        assertEquals(_other3.getValueOfMajorRole(_other3), "com.google.common.util.concurrent.ExecutionList");
    }

    @Test
    public void getValueOfSecondMajorRole() throws Exception {
        assertEquals(_this1.getValueOfSecondMajorRole(_this1), "com.google.common.util.concurrent.JdkFutureAdapters$ListenableFutureAdapter");
        assertEquals(_other1.getValueOfSecondMajorRole(_other1), "com.google.common.util.concurrent.JdkFutureAdapters$ListenableFutureAdapter");
        assertEquals(_other2.getValueOfSecondMajorRole(_other2), "com.google.common.util.concurrent.JdkFutureAdapters$ListenableFutureAdapter");
        assertEquals(_other3.getValueOfSecondMajorRole(_other3), "com.google.common.util.concurrent.JdkFutureAdapters$ListenableFutureAdapter2");
    }

}