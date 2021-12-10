package com.sbm4j.hearthstone.myhearthstone.model;

import javax.persistence.*;

@Entity
@Table(name = "cardTag")
public class CardTag {
    @Id
    @Column(nullable = false)
    @GeneratedValue
    private int id;

    @Column(nullable = false)
    private String code;

    @Column(nullable = false)
    private String name;

    @Column
    private Boolean isUser = true;

    @Column
    private int exclusiveGroup;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getUser() {
        return isUser;
    }

    public void setUser(Boolean user) {
        isUser = user;
    }

    public int getExclusiveGroup() {
        return exclusiveGroup;
    }

    public void setExclusiveGroup(int exclusiveGroup) {
        this.exclusiveGroup = exclusiveGroup;
    }
}
