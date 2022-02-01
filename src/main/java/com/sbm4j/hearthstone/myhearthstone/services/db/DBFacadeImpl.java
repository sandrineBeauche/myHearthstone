package com.sbm4j.hearthstone.myhearthstone.services.db;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.sbm4j.hearthstone.myhearthstone.model.*;
import com.sbm4j.hearthstone.myhearthstone.services.config.ConfigManager;
import com.sbm4j.hearthstone.myhearthstone.model.CardCatalogItem;
import javafx.util.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.query.Query;

import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

public class DBFacadeImpl implements DBFacade {

    @Inject
    protected DBManager db;

    @Inject
    protected ConfigManager config;

    @Inject
    protected Injector injector;

    public DBFacadeImpl(){}

    protected static Logger logger = LogManager.getLogger();

    @Inject
    public void init() throws FileNotFoundException {
        if(this.config.getInitDB()){
            DBInitializer initializer = this.injector.getInstance(DBInitializer.class);
            initializer.initDB();
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
    public Hero getHero(String code) throws NoResultException {
        Session session = this.db.getSession();
        TypedQuery<Hero> typedQuery = session.createNamedQuery("hero_from_code", Hero.class);
        return typedQuery.setParameter("code", code).getSingleResult();
    }


    @Override
    public List<CardClass> getClasses(boolean includeAll) {
        Session session = this.db.getSession();
        TypedQuery<CardClass> typedQuery = session.createNamedQuery("available_classes", CardClass.class);
        List<CardClass> results = typedQuery.getResultList();
        if(includeAll){
            CardClass cardClassAll = new CardClass(-1, "ALL", "Toutes les classes");
            results.add(0, cardClassAll);
        }
        return results;
    }

    @Override
    public List<CardSet> getSets(boolean includeWild) {
        Session session = this.db.getSession();
        TypedQuery<CardSet> typedQuery = session.createNamedQuery("available_sets", CardSet.class);
        List<CardSet> results = typedQuery.getResultList();
        if(includeWild){
            CardSet cardSetWild = new CardSet(-1, "WILD", "Libre", false, -1);
            results.add(0, cardSetWild);
            CardSet cardSetStandard = new CardSet(-2, "STANDARD", "Standard", true, -1);
            results.add(0, cardSetStandard);
        }
        return results;
    }

    @Override
    public List<CardCatalogItem> getCatalog(CatalogCriteria criteria) {
        Session session = this.db.getSession();
        CriteriaQueryBuilder builder = new CriteriaQueryBuilder();
        CriteriaQuery<CardCatalogItem> cq = builder.buildCatalogQuery(session, criteria);

        List<CardCatalogItem> results = session.createQuery(cq).getResultList();
        return results;
    }


    @Override
    public List<Rarity> getRarities(boolean includeAll) {
        Session session = this.db.getSession();
        TypedQuery<Rarity> typedQuery = session.createNamedQuery("available_rarities", Rarity.class);
        List<Rarity> results = typedQuery.getResultList();
        if(includeAll){
            Rarity rarityAll = new Rarity(-1, "ALL", "Toutes les raretés");
            results.add(0, rarityAll);
        }
        return results;
    }

    @Override
    public List<CardTag> getTags() {
        Session session = this.db.getSession();
        TypedQuery<CardTag> typedQuery = session.createNamedQuery("available_tags", CardTag.class);
        List<CardTag> results = typedQuery.getResultList();
        return results;
    }


    @Override
    public List<Hero> getHeros() {
        Session session = this.db.getSession();
        TypedQuery<Hero> typedQuery = session.createNamedQuery("available_heros", Hero.class);
        List<Hero> results = typedQuery.getResultList();
        return results;
    }


    @Override
    public Deck createDeck(String name, Hero hero) {
        try {
            Session session = this.db.getSession();
            Deck newDeck = new Deck(name, hero);
            session.beginTransaction();
            session.save(newDeck);
            session.getTransaction().commit();
            this.db.closeSession();
            return newDeck;
        }
        catch(Exception ex){
            logger.error("There is already a deck with the name " + name);
            return null;
        }
    }

    @Override
    public boolean deleteDeck(int id) {
        try {
            Session session = this.db.getSession();
            session.beginTransaction();
            Query query = session.createNamedQuery("Deck.deleteByIdEquals");
            query.setParameter("id", id).executeUpdate();
            session.getTransaction().commit();
            return true;
        }
        catch(Exception ex){
            logger.error(ex.getMessage(), ex);
            return false;
        }
    }

    @Override
    public Deck duplicateDeck(int id, String name) {
        Session session = this.db.getSession();

        Deck oldDeck = session.get(Deck.class, id);
        try {
            Deck newDeck = new Deck(name, oldDeck.getHero());
            newDeck.setSummary(oldDeck.getSummary());

            List<DeckAssociation> cards = new ArrayList<DeckAssociation>();
            session.beginTransaction();
            session.save(newDeck);
            for (DeckAssociation current : oldDeck.getCards()) {
                DeckAssociation newAss = new DeckAssociation();
                newAss.setCard(current.getCard());
                newAss.setNbCards(current.getNbCards());
                newAss.setDeck(newDeck);
                session.save(newAss);
            }

            session.getTransaction().commit();
            this.db.closeSession();
            return newDeck;
        }
        catch(NullPointerException ex){
            logger.error("Deck with id " + id + " cannot be duplicated: deck not found!");
            this.db.closeSession();
            return null;
        }
        catch(Exception ex2){
            logger.error(ex2.getMessage(), ex2);
            this.db.closeSession();
            return null;
        }
    }


    protected DeckAssociation getAssociationFromDeck(int dbfId, Deck deck){
        return deck.getCards().stream().filter(c -> c.getCard().getDbfId() == dbfId)
                        .findAny()
                        .orElse(null);
    }

    @Override
    public boolean addCardToDeck(int dbfId, Deck deck) {
        Session session = this.db.getSession();

        CardDetail card = session.get(CardDetail.class, dbfId);
        if(card != null) {
            DeckAssociation ass = this.getAssociationFromDeck(dbfId, deck);

            session.beginTransaction();
            if (ass == null) {
                DeckAssociation newAss = new DeckAssociation();
                newAss.setNbCards(1);
                newAss.setCard(card);
                newAss.setDeck(deck);
                session.save(newAss);
                session.save(deck);
            } else {
                ass.setNbCards(ass.getNbCards() + 1);
                session.update(ass);
            }
            session.getTransaction().commit();
            this.db.closeSession();
            return true;
        }
        else{
            logger.warn("card " + dbfId + " cannot be added to the deck " + deck.getName() + ": card not found!");
            this.db.closeSession();
            return false;
        }

    }

    @Override
    public boolean removeCardFromDeck(int dbfId, Deck deck, boolean all) {
        DeckAssociation ass = this.getAssociationFromDeck(dbfId, deck);
        if(ass != null){
            Session session = this.db.getSession();
            session.beginTransaction();
            if(ass.getNbCards() == 1 || all){
                deck.getCards().remove(ass);
                session.delete(ass);
                session.update(deck);
            }
            else{
                ass.setNbCards(ass.getNbCards() - 1);
                session.update(ass);
            }
            session.getTransaction().commit();
            this.db.closeSession();
            return true;
        }
        else{
            logger.error("card " + dbfId + " cannot be removed from the deck " + deck.getName() + ": card not associated with this deck!");
            return false;
        }
    }

    @Override
    public List<DeckListItem> getDeckList() {
        Session session = this.db.getSession();
        TypedQuery<DeckListItem> query = session.createNamedQuery("Deck.getDecksList", DeckListItem.class);
        query.setHint( "org.hibernate.readOnly", true );
        List<DeckListItem> result1 =  query.getResultList();

        TypedQuery<DeckListItem> query2 = session.createNamedQuery("Deck.getEmptyDecksList", DeckListItem.class);
        query2.setHint( "org.hibernate.readOnly", true );
        List<DeckListItem> result2 =  query2.getResultList();

        result1.addAll(0, result2);
        return result1;
    }

    @Override
    public List<DeckCardListItem> getDeckCardList(Deck deck) {
        Session session = this.db.getSession();
        TypedQuery<DeckCardListItem> query = session.createNamedQuery("DeckList.getCardList", DeckCardListItem.class);
        query.setHint( "org.hibernate.readOnly", true );
        query.setParameter("deck", deck);
        List<DeckCardListItem> result =  query.getResultList();
        return result;
    }


    @Override
    public Integer[] getManaCurveStats(Deck deck) {
        Session session = this.db.getSession();
        Query query = session.createNamedQuery("DeckList.manaCurveInf7");
        query.setHint( "org.hibernate.readOnly", true );
        query.setParameter("deck", deck);
        List<Object[]> result1 =  query.getResultList();

        TypedQuery<Long> query2 = session.createNamedQuery("DeckList.manaCurveSup7", Long.class);
        query2.setHint( "org.hibernate.readOnly", true );
        query2.setParameter("deck", deck);
        Long result2 = query2.getSingleResult();

        Integer[] result = {0,0,0,0,0,0,0,result2.intValue()};

        for(Object [] current: result1){
            Integer index = (Integer) current[0];
            result[index] = ((Long) current[1]).intValue();
        }

        return result;
    }

    @Override
    public List<Pair<String, Integer>> getTagsStats(Deck deck) {
        Session session = this.db.getSession();
        TypedQuery query = session.createNamedQuery("tags_stats_from_deck", Pair.class);
        query.setHint( "org.hibernate.readOnly", true );
        query.setParameter("deck", deck);
        List<Pair<String, Integer>> result =  query.getResultList();
        return result;
    }


}
