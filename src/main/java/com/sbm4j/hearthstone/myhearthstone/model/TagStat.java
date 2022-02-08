package com.sbm4j.hearthstone.myhearthstone.model;

public class TagStat {

    protected String tag;

    protected int value;

    public TagStat(String tag, long value){
        this.tag = tag;
        this.value = (int) value;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return this.tag + " => " + this.value;
    }
}
