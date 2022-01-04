package com.sbm4j.hearthstone.myhearthstone.model;

import javax.persistence.*;

@org.hibernate.annotations.NamedQuery(
        name="rarity_from_code",
        query="select r from Rarity r where r.code = :code"
)
@Entity
@Table(name = "Rarity")
public class Rarity implements CodedEntity{

    @Id
    @Column(nullable = false)
    @GeneratedValue
    private int id;

    @Column(nullable = false, unique = true)
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

    public Rarity(){}

    public Rarity(int id, String code, String name){
        this.id = id;
        this.code = code;
        this.name = name;
    }

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
