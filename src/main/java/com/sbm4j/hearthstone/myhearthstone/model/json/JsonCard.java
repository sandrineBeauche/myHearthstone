package com.sbm4j.hearthstone.myhearthstone.model.json;

import java.util.ArrayList;

public class JsonCard {

    private int dbfId;

    private String artist;

    private int attack;

    private int cost;

    private String flavor;

    private String id;

    private String name;

    private String text;

    private int durability;

    private int health;

    private Boolean collectible = true;

    private String cardClass;

    private ArrayList<String> classes;

    private String set;

    private String rarity;

    private String spellSchool;

    private String type;

    private ArrayList<String> mechanics;

    private String race;

    private String howToEarn;

    private String howToEarnGolden;

    private ArrayList<String> referencedTags;

    private String jsonDesc;

    private Boolean elite = false;

    private String questReward;

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

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public Boolean getCollectible() {
        return collectible;
    }

    public void setCollectible(Boolean collectible) {
        this.collectible = collectible;
    }

    public String getCardClass() {
        return cardClass;
    }

    public void setCardClass(String cardClass) {
        this.cardClass = cardClass;
    }

    public ArrayList<String> getClasses() {
        return classes;
    }

    public void setClasses(ArrayList<String> classes) {
        this.classes = classes;
    }

    public String getSet() {
        return set;
    }

    public void setSet(String set) {
        this.set = set;
    }

    public String getRarity() {
        return rarity;
    }

    public void setRarity(String rarity) {
        this.rarity = rarity;
    }

    public String getSpellSchool() {
        return spellSchool;
    }

    public void setSpellSchool(String spellSchool) {
        this.spellSchool = spellSchool;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public ArrayList<String> getMechanics() {
        return mechanics;
    }

    public void setMechanics(ArrayList<String> mechanics) {
        this.mechanics = mechanics;
    }

    public String getRace() {
        return race;
    }

    public void setRace(String race) {
        this.race = race;
    }

    public String getHowToEarn() {
        return howToEarn;
    }

    public void setHowToEarn(String howToEarn) {
        this.howToEarn = howToEarn;
    }

    public String getHowToEarnGolden() {
        return howToEarnGolden;
    }

    public void setHowToEarnGolden(String howToEarnGolden) {
        this.howToEarnGolden = howToEarnGolden;
    }

    public ArrayList<String> getReferencedTags() {
        return referencedTags;
    }

    public void setReferencedTags(ArrayList<String> referencedTags) {
        this.referencedTags = referencedTags;
    }

    public String getJsonDesc() {
        return jsonDesc;
    }

    public void setJsonDesc(String jsonDesc) {
        this.jsonDesc = jsonDesc;
    }

    public Boolean getElite() {
        return elite;
    }

    public void setElite(Boolean elite) {
        this.elite = elite;
    }

    public String getQuestReward() {
        return questReward;
    }

    public void setQuestReward(String questReward) {
        this.questReward = questReward;
    }
}
