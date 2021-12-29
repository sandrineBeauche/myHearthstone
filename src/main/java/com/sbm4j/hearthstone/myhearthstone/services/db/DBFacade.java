package com.sbm4j.hearthstone.myhearthstone.services.db;

import com.sbm4j.hearthstone.myhearthstone.model.CardClass;
import com.sbm4j.hearthstone.myhearthstone.model.CardSet;
import com.sbm4j.hearthstone.myhearthstone.model.CardTag;
import com.sbm4j.hearthstone.myhearthstone.model.Rarity;

import javax.persistence.NoResultException;
import java.io.FileNotFoundException;
import java.net.URISyntaxException;

public interface DBFacade {
    void initDB() throws FileNotFoundException, URISyntaxException;

    Rarity getRarity(String code) throws NoResultException;

    CardClass getClasse(String code) throws NoResultException;

    CardSet getSet(String code) throws NoResultException;

    CardTag getTag(String code);
}