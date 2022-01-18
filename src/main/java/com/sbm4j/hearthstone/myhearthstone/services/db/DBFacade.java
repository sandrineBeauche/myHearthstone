package com.sbm4j.hearthstone.myhearthstone.services.db;

import com.sbm4j.hearthstone.myhearthstone.model.CardClass;
import com.sbm4j.hearthstone.myhearthstone.model.CardSet;
import com.sbm4j.hearthstone.myhearthstone.model.CardTag;
import com.sbm4j.hearthstone.myhearthstone.model.Rarity;
import com.sbm4j.hearthstone.myhearthstone.model.CardCatalogItem;

import javax.persistence.NoResultException;
import java.io.FileNotFoundException;
import java.net.URISyntaxException;
import java.util.List;

public interface DBFacade {
    void initDB() throws FileNotFoundException, URISyntaxException;

    Rarity getRarity(String code) throws NoResultException;

    CardClass getClasse(String code) throws NoResultException;

    CardSet getSet(String code) throws NoResultException;

    CardTag getTag(String code);

    List<CardClass> getClasses(boolean includeAll);

    List<CardSet> getSets(boolean includeWild);

    List<CardCatalogItem> getCatalog(CatalogCriteria criteria);

    List<Rarity> getRarities(boolean includeAll);

    List<CardTag> getTags();
}
