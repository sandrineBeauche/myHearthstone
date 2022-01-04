package com.sbm4j.hearthstone.myhearthstone.views;

import com.sbm4j.hearthstone.myhearthstone.model.CodedEntity;

public class ManaOption implements CodedEntity {

    protected String code;

    protected String name;

    protected int value;

    public ManaOption(String code, String name, int value){
        this.code = code;
        this.name = name;
        this.value = value;
    }

    @Override
    public String getCode() {
        return this.code;
    }

    @Override
    public String getName() {
        return this.name;
    }

    public int getValue(){return this.value;}
}
