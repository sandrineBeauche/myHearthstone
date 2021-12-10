package com.sbm4j.hearthstone.myhearthstone.model;

import javax.persistence.*;

@Entity
@Table(name = "cardSet")
public class CardSet {

    @Id
    @Column(nullable = false)
    @GeneratedValue
    private int id;

    @Column(nullable = false)
    private String code;

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

    @Column(nullable = false)
    private String name;


}
