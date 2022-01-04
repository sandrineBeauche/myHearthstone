package com.sbm4j.hearthstone.myhearthstone.services.db;

import com.google.gson.Gson;
import com.google.inject.Inject;
import com.sbm4j.hearthstone.myhearthstone.model.CardClass;
import com.sbm4j.hearthstone.myhearthstone.model.CardSet;
import com.sbm4j.hearthstone.myhearthstone.model.CardTag;
import com.sbm4j.hearthstone.myhearthstone.model.Rarity;
import com.sbm4j.hearthstone.myhearthstone.model.json.DBInitiator;
import com.sbm4j.hearthstone.myhearthstone.services.config.ConfigManager;
import com.sbm4j.hearthstone.myhearthstone.views.CardCatalogItem;
import org.hibernate.Session;

import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

public class DBFacadeImpl implements DBFacade {

    @Inject
    protected DBManager db;

    @Inject
    protected ConfigManager config;

    @Inject
    public void init() throws FileNotFoundException, URISyntaxException {
        if(this.config.getInitDB()){
            this.initDB();
        }
    }

    @Override
    public Rarity getRarity(String code) throws NoResultException {
        Session session = this.db.getSession();
        TypedQuery<Rarity> typedQuery = session.createNamedQuery("rarity_from_code", Rarity.class);
        return typedQuery.setParameter("code", code).getSingleResult();
    }

    @Override
    public CardClass getClasse(String code) throws NoResultException {
        Session session = this.db.getSession();
        TypedQuery<CardClass> typedQuery = session.createNamedQuery("class_from_code", CardClass.class);
        return typedQuery.setParameter("code", code).getSingleResult();
    }

    @Override
    public CardSet getSet(String code) throws NoResultException{
        Session session = this.db.getSession();
        TypedQuery<CardSet> typedQuery = session.createNamedQuery("cardSet_from_code", CardSet.class);
        return typedQuery.setParameter("code", code).getSingleResult();
    }

    @Override
    public CardTag getTag(String code){
        Session session = this.db.getSession();
        TypedQuery<CardTag> typedQuery = session.createNamedQuery("tag_from_code", CardTag.class);
        return typedQuery.setParameter("code", code).getSingleResult();
    }

    @Override
    public void initDB() throws FileNotFoundException, URISyntaxException {
        File file = this.config.getJsonGameData();
        if(file.exists()){
            FileReader reader = new FileReader(file);

            Gson gson = new Gson();
            DBInitiator initiator = gson.fromJson(reader, DBInitiator.class);
            this.initRarity(initiator.getRarity());
            this.initCardClass(initiator.getClasses());
            this.initCardSet(initiator.getExtensions());
            this.initCardTags(initiator.getTags());
        }
    }

    protected void initRarity(ArrayList<Rarity> rarity){
        Session session = this.db.getSession();
        session.beginTransaction();
        for(Rarity current: rarity){
            try {
                Rarity r = this.getRarity(current.getCode());
            }
            catch (NoResultException ex){
                session.save(current);
            }
        }
        session.getTransaction().commit();
        this.db.closeSession();
    }


    protected void initCardClass(ArrayList<CardClass> classes){
        Session session = this.db.getSession();
        session.beginTransaction();
        for(CardClass current: classes){
            try {
                CardClass c = this.getClasse(current.getCode());
            }
            catch (NoResultException ex){
                session.save(current);
            }
        }
        session.getTransaction().commit();
        this.db.closeSession();
    }

    protected void initCardSet(ArrayList<CardSet> sets){
        Session session = this.db.getSession();
        session.beginTransaction();
        for(CardSet current: sets){
            try {
                CardSet c = this.getSet(current.getCode());
            }
            catch (NoResultException ex){
                session.save(current);
            }
        }
        session.getTransaction().commit();
        this.db.closeSession();
    }

    @Override
    public List<CardClass> getClasses() {
        CardClass [] classes = {
                new CardClass(4, "ALL", "Toutes les classes"),
                new CardClass(1, "SHAMAN", "Chaman"),
                new CardClass(2, "PALADIN", "Paladin"),
                new CardClass(3, "MAGE", "Mage")
        };
        return List.of(classes);
    }

    @Override
    public List<CardSet> getSets() {
        CardSet [] sets = {
                new CardSet(1, "STANDARD", "Standard"),
                new CardSet(2, "ALTERAC_VALLEY", "Divisés dans la vallée d'Alterac"),
                new CardSet(3, "STORMWIND", "Unis à Hurlevent"),
                new CardSet(4, "THE_BARRENS", "Forgés dans les tarides")
        };
        return List.of(sets);
    }

    @Override
    public List<CardCatalogItem> getCatalog() {
        CardCatalogItem [] items = {
                new CardCatalogItem(1, "AT_001", "carte1", 2),
                new CardCatalogItem(1, "AT_002", "carte2", 2),
                new CardCatalogItem(1, "AT_003", "carte3", 2),
                new CardCatalogItem(1, "AT_045", "carte4", 2),
                new CardCatalogItem(1, "AT_050", "carte5", 2),
                new CardCatalogItem(1, "AV_101", "carte6", 2),
                new CardCatalogItem(1, "AV_102", "carte7", 2),
        };
        return List.of(items);
    }

    @Override
    public List<Rarity> getRarities() {
        Rarity [] rarities = {
                new Rarity(-1, "ALL", "Toutes les raretés"),
          new Rarity(1, "COMMON", "Commune"),
          new Rarity(2, "RARE", "Rare"),
          new Rarity(3, "EPIC", "Epique")
        };
        return List.of(rarities);
    }

    protected void initCardTags(ArrayList<CardTag> tags){
        Session session = this.db.getSession();
        session.beginTransaction();
        for(CardTag current: tags){
            try {
                CardTag c = this.getTag(current.getCode());
            }
            catch (NoResultException ex){
                session.save(current);
            }
        }
        session.getTransaction().commit();
        this.db.closeSession();
    }


}
