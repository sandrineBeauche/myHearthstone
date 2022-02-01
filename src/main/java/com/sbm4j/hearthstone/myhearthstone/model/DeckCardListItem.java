package com.sbm4j.hearthstone.myhearthstone.model;

public class DeckCardListItem {

    private int dbfId;

    private String id;

    private String name;

    private int nbCards;

    private int nbCardsInCollection;

    private String setCode;

    private String classCode;

    private String rarityCode;

    private int mana;

    private boolean standard;

    private String tags;

    public DeckCardListItem(int dbfId, String id, String name, int nbCards, int nbCardsInCollection,
                            String setCode, String classCode, String rarityCode, int mana, boolean standard,
                            String tags){
        this.dbfId = dbfId;
        this.id = id;
        this.name = name;
        this.nbCards = nbCards;
        this.nbCardsInCollection = nbCardsInCollection;
        this.setCode = setCode;
        this.classCode = classCode;
        this.rarityCode = rarityCode;
        this.mana = mana;
        this.standard = standard;
        this.tags = tags;
    }

    public int getDbfId() {
        return dbfId;
    }

    public void setDbfId(int dbfId) {
        this.dbfId = dbfId;
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

    public int getNbCards() {
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

    public String getSetCode() {
        return setCode;
    }

    public void setSetCode(String setCode) {
        this.setCode = setCode;
    }

    public String getClassCode() {
        return classCode;
    }

    public void setClassCode(String classCode) {
        this.classCode = classCode;
    }

    public String getRarityCode() {
        return rarityCode;
    }

    public void setRarityCode(String rarityCode) {
        this.rarityCode = rarityCode;
    }

    public int getMana() {
        return mana;
    }

    public void setMana(int mana) {
        this.mana = mana;
    }

    public boolean isStandard() {
        return standard;
    }

    public void setStandard(boolean value) {
        standard = value;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }
}
