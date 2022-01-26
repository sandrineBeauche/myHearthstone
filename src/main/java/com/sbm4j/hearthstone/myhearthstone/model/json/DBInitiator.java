package com.sbm4j.hearthstone.myhearthstone.model.json;

import com.sbm4j.hearthstone.myhearthstone.model.CardClass;
import com.sbm4j.hearthstone.myhearthstone.model.CardSet;
import com.sbm4j.hearthstone.myhearthstone.model.CardTag;
import com.sbm4j.hearthstone.myhearthstone.model.Rarity;

import java.util.ArrayList;

public class DBInitiator {

    private ArrayList<Rarity> rarity;

    private ArrayList<CardClass> classes;

    private ArrayList<CardSet> extensions;

    private ArrayList<CardTag> tags;

    private ArrayList<JsonHero> heros;

    public static String filename = "gameData.json";

    public ArrayList<Rarity> getRarity() {
        return rarity;
    }

    public void setRarity(ArrayList<Rarity> rarity) {
        this.rarity = rarity;
    }

    public ArrayList<CardClass> getClasses() {
        return classes;
    }

    public void setClasses(ArrayList<CardClass> classes) {
        this.classes = classes;
    }

    public ArrayList<CardSet> getExtensions() {
        return extensions;
    }

    public void setExtensions(ArrayList<CardSet> extensions) {
        this.extensions = extensions;
    }

    public ArrayList<CardTag> getTags() {
        return tags;
    }

    public void setTags(ArrayList<CardTag> tags) {
        this.tags = tags;
    }

    public ArrayList<JsonHero> getHeros() {
        return heros;
    }

    public void setHeros(ArrayList<JsonHero> heros) {
        this.heros = heros;
    }
}
