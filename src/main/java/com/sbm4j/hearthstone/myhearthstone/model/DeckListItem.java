package com.sbm4j.hearthstone.myhearthstone.model;

public class DeckListItem {

    private int deckId;

    private String name;

    private String summary;

    private String heroCode;

    private long nbCards;

    private long nbCardsInCollection;

    private long nbStandardCards;

    private String tags;

    public DeckListItem(int deckId, String name, String summary, String heroCode,
                        long nbCards, long nbCardsInCollection, long nbStandardCards,
                        String tags){
        this.deckId = deckId;
        this.name = name;
        this.summary = summary;
        this.heroCode = heroCode;
        this.nbCards = nbCards;
        this.nbCardsInCollection = nbCardsInCollection;
        this.nbStandardCards = nbStandardCards;
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

    public void setNbCards(long nbCards) {
        this.nbCards = nbCards;
    }

    public long getNbCardsInCollection() {
        return nbCardsInCollection;
    }

    public void setNbCardsInCollection(long nbCardsInCollection) {
        this.nbCardsInCollection = nbCardsInCollection;
    }

    public long getNbStandardCards() {
        return nbStandardCards;
    }

    public void setNbStandardCards(long nbStandardCards) {
        this.nbStandardCards = nbStandardCards;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }
}
