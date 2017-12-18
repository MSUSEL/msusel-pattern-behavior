package com.derek.uml.srcML;

public class SrcMLDataType {
    private String name;

    //this variable represents nested types, which are usually generics. Bear in mind this field can also be null.
    //not sure how I am going to access types n deep yet.. such as List<List<List<List<List<....String>>>>...>> (how do I get the string?
    private SrcMLDataType nestedType;



}
