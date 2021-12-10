package com.sbm4j.hearthstone.myhearthstone.model;

import javax.persistence.*;

@Entity
@Table(name = "Rarity")
public class Rarity {

    @Id
    @Column(nullable = false)
    @GeneratedValue
    private int id;

    @Column(nullable = false)
    private String code;

    @Column(nullable = false)
    private String name;

    @Column
    private int cost;

    @Column
    private int costGold;

    @Column
    private int gain;

    @Column
    private int gainGold;

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

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    public int getCostGold() {
        return costGold;
    }

    public void setCostGold(int costGold) {
        this.costGold = costGold;
    }

    public int getGain() {
        return gain;
    }

    public void setGain(int gain) {
        this.gain = gain;
    }

    public int getGainGold() {
        return gainGold;
    }

    public void setGainGold(int gainGold) {
        this.gainGold = gainGold;
    }
}
