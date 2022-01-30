package com.sbm4j.hearthstone.myhearthstone.model;

public class DeckListItem {

    private int deckId;

    private String name;

    private String summary;

    private String heroCode;

    private int nbCards;

    private int nbCardsInCollection;

    private int nbStandardCards;

    private String tags;

    public DeckListItem(int deckId, String name, String summary, String heroCode,
                        Long nbCards, Long nbCardsInCollection, Long nbStandardCards,
                        String tags){
        this.deckId = deckId;
        this.name = name;
        this.summary = summary;
        this.heroCode = heroCode;
        this.nbCards = nbCards.intValue();
        this.nbCardsInCollection = nbCardsInCollection.intValue();
        this.nbStandardCards = nbStandardCards.intValue();
        this.tags = tags;
    }

    public int getDeckId() {
        return deckId;
    }

    public void setDeckId(int deckId) {
        this.deckId = deckId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getHeroCode() {
        return heroCode;
    }

    public void setHeroCode(String heroCode) {
        this.heroCode = heroCode;
    }

    public long getNbCards() {
        return nbCards;
    }

    public void setNbCards(int nbCards) {
        this.nbCards = nbCards;
    }

    public int getNbCardsInCollection() {
        return nbCardsInCollection;
    }

    public void setNbCardsInCollection(int nbCardsInCollection) {
        this.nbCardsInCollection = nbCardsInCollection;
    }

    public int getNbStandardCards() {
        return nbStandardCards;
    }

    public void setNbStandardCards(int nbStandardCards) {
        this.nbStandardCards = nbStandardCards;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }
}
