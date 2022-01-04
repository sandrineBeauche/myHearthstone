package com.sbm4j.hearthstone.myhearthstone.views;

public class CardCatalogItem {

    protected int dbfId;

    protected String id;

    protected String name;

    protected int nbCardsInCollection;

    public CardCatalogItem(){

    }

    public CardCatalogItem(int dbfId, String id, String name, int nbCards){
        this.dbfId = dbfId;
        this.id = id;
        this.name = name;
        this.nbCardsInCollection = nbCards;
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

    public int getNbCardsInCollection() {
        return nbCardsInCollection;
    }

    public void setNbCardsInCollection(int nbCardsInCollection) {
        this.nbCardsInCollection = nbCardsInCollection;
    }
}
