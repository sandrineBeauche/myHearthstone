package com.sbm4j.hearthstone.myhearthstone.model;


import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "cardUserData")
public class CardUserData {

    @Id
    @Column(nullable = false)
    private int dbfId;

    @Column
    private int nbCards;

    @Column
    private int nbGolden;

    @Column
    private int nbTotalCards;

    @ManyToMany
    private List<CardTag> tags = new ArrayList<CardTag>();

    public int getDbfId() {
        return dbfId;
    }

    public void setDbfId(int dbfId) {
        this.dbfId = dbfId;
    }

    public int getNbCards() {
        return nbCards;
    }

    public void setNbCards(int nbCards) {
        this.nbCards = nbCards;
    }

    public int getNbGolden() {
        return nbGolden;
    }

    public void setNbGolden(int nbGolden) {
        this.nbGolden = nbGolden;
    }

    public int getNbTotalCards() {
        return nbTotalCards;
    }

    public void setNbTotalCards(int nbTotalCards) {
        this.nbTotalCards = nbTotalCards;
    }

    public List<CardTag> getTags() {
        return tags;
    }

    public void setTags(List<CardTag> tags) {
        this.tags = tags;
    }
}
