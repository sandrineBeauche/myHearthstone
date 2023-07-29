package com.sbm4j.hearthstone.myhearthstone.model;

public class CardSetRarityStat {

    protected Rarity rarity;

    protected int nbTotalDistinct = 0;

    protected int nbTotal = 0;

    protected int nbCollectionDistinct = 0;

    protected int nbCollection = 0;

    public CardSetRarityStat(Rarity rarity){
        this.rarity = rarity;
    }

    public Rarity getRarity() {
        return rarity;
    }

    public void setRarity(Rarity rarity) {
        this.rarity = rarity;
    }

    public int getNbTotalDistinct() {
        return nbTotalDistinct;
    }

    public void setNbTotalDistinct(int nbTotalDistinct) {
        this.nbTotalDistinct = nbTotalDistinct;
    }

    public int getNbTotal() {
        return nbTotal;
    }

    public void setNbTotal(int nbTotal) {
        this.nbTotal = nbTotal;
    }

    public int getNbCollectionDistinct() {
        return nbCollectionDistinct;
    }

    public void setNbCollectionDistinct(int nbCollectionDistinct) {
        this.nbCollectionDistinct = nbCollectionDistinct;
    }

    public int getNbCollection() {
        return nbCollection;
    }

    public void setNbCollection(int nbCollection) {
        this.nbCollection = nbCollection;
    }

    public void addCountStat(CardSetRarityCountStat stat){
        this.nbTotalDistinct += stat.getNbCards();
        if(stat.getCountStat() > 0){
            if(stat.getCountStat() > 2){
                stat.setCountStat(2);
            }
            this.nbCollectionDistinct += stat.getNbCards();
        }
        this.nbCollection += stat.getCountStat() * stat.getNbCards();

        if(this.rarity.getCode() == "LEGENDARY"){
            this.nbTotal = this.nbTotalDistinct;
        }
        else{
            this.nbTotal = this.nbTotalDistinct * 2;
        }
    }
}
