package com.sbm4j.hearthstone.myhearthstone.model;

import javax.persistence.*;

@Entity
@Table(name = "cardDetail")
public class CardDetail {
    @Id
    @Column(nullable = false)
    private int dbfId;

    @Column
    private String artist;

    @Column
    private int attack;

    @Column
    private int cost;

    @Column
    private String flavor;

    @Column
    private String id;

    @Column
    private String name;

    @Column
    private String text;

    @Column
    private int durability;

    @ManyToOne
    @JoinColumn(name = "cardSet_id", foreignKey = @ForeignKey(name = "CARDSET_ID_FK"))
    private CardSet cardSet;

    public int getDbfId() {
        return dbfId;
    }

    public void setDbfId(int dbfId) {
        this.dbfId = dbfId;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public int getAttack() {
        return attack;
    }

    public void setAttack(int attack) {
        this.attack = attack;
    }

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    public String getFlavor() {
        return flavor;
    }

    public void setFlavor(String flavor) {
        this.flavor = flavor;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getDurability() {
        return durability;
    }

    public void setDurability(int durability) {
        this.durability = durability;
    }
}
