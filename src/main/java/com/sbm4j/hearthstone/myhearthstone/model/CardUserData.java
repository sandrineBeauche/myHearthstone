package com.sbm4j.hearthstone.myhearthstone.model;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

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
}
