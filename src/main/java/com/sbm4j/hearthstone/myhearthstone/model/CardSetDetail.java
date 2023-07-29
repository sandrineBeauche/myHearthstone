package com.sbm4j.hearthstone.myhearthstone.model;

import com.sbm4j.hearthstone.myhearthstone.services.db.DBFacade;

import java.util.HashMap;

public class CardSetDetail {

    protected CardSet cardSet;

    protected int nbTotal;

    protected int nbDistinctTotal;

    protected int nbCollectionTotal;

    protected int nbDisctinctCollectionTotal;

    protected HashMap<String, CardSetRarityStat> stats;

    public CardSetDetail(CardSet cardSet){
        this.cardSet = cardSet;

        this.stats = new HashMap<>();
    }


    public int getNbTotal() {
        return nbTotal;
    }

    public int getNbDistinctTotal() {
        return nbDistinctTotal;
    }

    public int getNbCollectionTotal() {
        return nbCollectionTotal;
    }

    public int getNbDisctinctCollectionTotal() {
        return nbDisctinctCollectionTotal;
    }

    public HashMap<String, CardSetRarityStat> getStats() {
        return stats;
    }

    public void addCountStat(CardSetRarityCountStat stat){
        String code = stat.getRarity().getCode();
        if(!this.stats.containsKey(code)){
            this.stats.put(code, new CardSetRarityStat(stat.getRarity()));
        }
        CardSetRarityStat rarityStat = this.stats.get(code);
        rarityStat.addCountStat(stat);

        this.nbDistinctTotal += stat.getNbCards();
        if(stat.getCountStat() > 0){
            this.nbDisctinctCollectionTotal += stat.getNbCards();
        }

        this.nbCollectionTotal += stat.getCountStat() * stat.getNbCards();

        if(stat.getRarity().getCode() == "LEGENDARY"){
            this.nbTotal += stat.getNbCards();
        }
        else{
            this.nbTotal += stat.getNbCards() * 2;
        }
    }


    public void completeStats(DBFacade dbFacade){
        if(!this.stats.containsKey("COMMON")) this.stats.put("COMMON", new CardSetRarityStat(dbFacade.getRarity("COMMON")));
        if(!this.stats.containsKey("RARE")) this.stats.put("RARE", new CardSetRarityStat(dbFacade.getRarity("RARE")));
        if(!this.stats.containsKey("EPIC")) this.stats.put("EPIC", new CardSetRarityStat(dbFacade.getRarity("EPIC")));
        if(!this.stats.containsKey("LEGENDARY")) this.stats.put("LEGENDARY", new CardSetRarityStat(dbFacade.getRarity("LEGENDARY")));
    }

    public String getCode(){
        return this.cardSet.getCode();
    }

    public String getName(){
        return this.cardSet.getName();
    }

    public Boolean getStandard(){
        return this.cardSet.isStandard();
    }

    public String getCommon(){
        return this.getStats("COMMON");
    }

    public String getRare(){
        return this.getStats("RARE");
    }

    public String getEpic(){
        return this.getStats("EPIC");
    }

    public String getLegendary(){
        return this.getStats("LEGENDARY");
    }

    protected String getStats(String rarityCode){
        CardSetRarityStat currentStat = this.stats.get(rarityCode);
        String distinct = currentStat.getNbCollectionDistinct() + " / " + currentStat.getNbTotalDistinct();
        String total = "(" + currentStat.getNbCollection() + " / " + currentStat.getNbTotal() + ")";
        return distinct + "\n" + total;
    }

    public String getTotal(){
        String distinct = this.nbDisctinctCollectionTotal + " / " + this.nbDistinctTotal;
        String total = "(" + this.nbCollectionTotal + " / " + this.nbTotal + ")";
        return distinct + "\n" + total;
    }
}
