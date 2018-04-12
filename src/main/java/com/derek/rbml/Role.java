package com.derek.rbml;

public abstract class Role {
    private String roleType;

    public Role(String lineDescription){

    }

    protected abstract void parseLineDescription(String lineDescription);
}
