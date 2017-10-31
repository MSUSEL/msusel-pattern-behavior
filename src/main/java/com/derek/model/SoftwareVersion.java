package com.derek.model;

public class SoftwareVersion implements Comparable<SoftwareVersion>{

    private int versionNum;

    public SoftwareVersion(int versionNum){
        this.versionNum = versionNum;
    }

    public int getVersionNum() {
        return versionNum;
    }

    public void setVersionNum(int versionNum) {
        this.versionNum = versionNum;
    }

    @Override
    public String toString() {
        return "Version: " + versionNum;
    }

    @Override
    public int compareTo(SoftwareVersion otherVersion) {
        return this.versionNum - otherVersion.versionNum;
    }
}
