package com.sbm4j.hearthstone.myhearthstone.model;

public class CardSetRarityCountStat {

    protected CardSet cardSet;

    protected Rarity rarity;

    protected int countStat;

    protected int nbCards;

    public CardSetRarityCountStat(CardSet cardSet, Rarity rarity, int countStat, long nbCards) {
        this.cardSet = cardSet;
        this.rarity = rarity;
        this.nbCards = (int) nbCards;
        this.countStat = countStat;
    }



    public Rarity getRarity() {
        return rarity;
    }

    public void setRarity(Rarity rarity) {
        this.rarity = rarity;
    }

    public int getNbCards() {
        return nbCards;
    }

    public void setNbCards(int nbCards) {
        this.nbCards = nbCards;
    }

    public int getCountStat() {
        return countStat;
    }

    public void setCountStat(int countStat) {
        this.countStat = countStat;
    }

    public CardSet getCardSet() {
        return cardSet;
    }

    public void setCardSet(CardSet cardSet) {
        this.cardSet = cardSet;
    }
}
