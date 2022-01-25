package com.sbm4j.hearthstone.myhearthstone.services.db;

import com.google.gson.Gson;
import com.google.inject.Inject;
import com.sbm4j.hearthstone.myhearthstone.model.*;
import com.sbm4j.hearthstone.myhearthstone.model.json.DBInitiator;
import com.sbm4j.hearthstone.myhearthstone.services.config.ConfigManager;
import com.sbm4j.hearthstone.myhearthstone.model.CardCatalogItem;
import com.sbm4j.hearthstone.myhearthstone.views.ManaOption;
import javafx.scene.chart.XYChart;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.exception.ConstraintViolationException;
import org.hibernate.query.Query;

import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
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

    public DBFacadeImpl(){}

    protected static Logger logger = LogManager.getLogger();

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
        logger.info("Initialize database...");
        File file = this.config.getJsonGameData();
        if(file.exists()){
            logger.info("... with file " + file.getAbsolutePath());
            FileReader reader = new FileReader(file);

            Gson gson = new Gson();
            DBInitiator initiator = gson.fromJson(reader, DBInitiator.class);
            this.initRarity(initiator.getRarity());
            this.initCardClass(initiator.getClasses());
            this.initCardSet(initiator.getExtensions());
            this.initCardTags(initiator.getTags());
        }
        else{
            logger.info(file.getAbsolutePath() + " does not exists. Database cannot be initialized");
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
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<CardCatalogItem> cq = cb.createQuery(CardCatalogItem.class);
        Root<CardDetail> cardDetailRoot = cq.from(CardDetail.class);

        this.buildSelect(cq, cb, cardDetailRoot);

        List<Predicate> restrictions = new ArrayList<Predicate>();
        this.buildCardClassPredicate(restrictions, criteria.cardClass(), cardDetailRoot);
        this.buildCardSetPredicate(restrictions, criteria.cardSet(), cb, cardDetailRoot);
        this.buildRarityPredicate(restrictions, criteria.rarity(), cb, cardDetailRoot);
        this.buildManaPredicate(restrictions, criteria.mana(), cb, cardDetailRoot);
        this.buildInCollectionPredicate(restrictions, criteria.inCollection(), cb, cardDetailRoot);
        this.buildTagsPredicate(restrictions, criteria.tags(), cardDetailRoot);

        if(restrictions.size() == 1){
            cq.where(restrictions.get(0));
        }
        else{
            Predicate[] preds = restrictions.toArray(new Predicate[0]);
            cq.where(cb.and(preds));
        }

        Path<Integer> costPath = cardDetailRoot.get("cost");
        Path<String> namePath = cardDetailRoot.get("name");
        List<Order> orderList = List.of(cb.asc(costPath), cb.asc(namePath));
        cq.orderBy(orderList);

        List<CardCatalogItem> results = session.createQuery(cq).getResultList();
        return results;
    }


    protected void buildSelect(CriteriaQuery<CardCatalogItem> query, CriteriaBuilder builder, Root<CardDetail> cardDetailRoot){
        Path<Integer> dbfIdPath = cardDetailRoot.get("dbfId");
        Path<String> idPath = cardDetailRoot.get("id");
        Path<String> namePath = cardDetailRoot.get("name");
        Path<Integer> nbCardsPath = cardDetailRoot.get("userData").get("nbTotalCards");
        Path<Integer> costPath = cardDetailRoot.get("cost");

        query.select(builder.construct(CardCatalogItem.class, dbfIdPath, idPath, namePath, nbCardsPath, costPath));
        query.distinct(true);
    }

    protected void buildCardClassPredicate(List<Predicate> restrictions,
                                                      CardClass cardClass,
                                                      Root<CardDetail> cardDetailRoot){
        if(cardClass != null) {
            Join<CardDetail, CardClass> join = cardDetailRoot.join("cardClass");
            restrictions.add(join.in(cardClass));
        }
    }

    protected void buildCardSetPredicate(List<Predicate> restrictions,
                                           CardSet cardSet,
                                           CriteriaBuilder builder,
                                           Root<CardDetail> cardDetailRoot){
        if(cardSet != null && !cardSet.getCode().equals("WILD")){
            Path<CardSet> cardSetPath = cardDetailRoot.get("cardSet");

            if(cardSet.getCode().equals("STANDARD")){
                Path<Boolean> cardSetStandardPath = cardSetPath.get("isStandard");
                restrictions.add(builder.equal(cardSetStandardPath, true));
            }
            else{
                restrictions.add(builder.equal(cardSetPath, cardSet));
            }
        }
    }

    protected void buildRarityPredicate(List<Predicate> restrictions,
                                         Rarity rarity,
                                         CriteriaBuilder builder,
                                         Root<CardDetail> cardDetailRoot){
        if(rarity != null && !rarity.getCode().equals("ALL")){
            Path<Rarity> rarityPath = cardDetailRoot.get("rarity");
            restrictions.add(builder.equal(rarityPath, rarity));
        }
    }

    protected void buildManaPredicate(List<Predicate> restrictions,
                                        ManaOption mana,
                                        CriteriaBuilder builder,
                                        Root<CardDetail> cardDetailRoot){
        if(mana != null && !mana.getCode().equals("ALL")){
            int manaValue = mana.getValue();
            Path<Integer> manaPath = cardDetailRoot.get("cost");
            if(manaValue < 7){
                restrictions.add(builder.equal(manaPath, manaValue));
            }
            else{
                restrictions.add(builder.ge(manaPath, manaValue));
            }
        }
    }

    protected void buildInCollectionPredicate(List<Predicate> restrictions,
                                              boolean inCollection,
                                              CriteriaBuilder builder,
                                              Root<CardDetail> cardDetailRoot){
        if(inCollection) {
            Path<Integer> nbCardsPath = cardDetailRoot.get("userData").get("nbTotalCards");
            restrictions.add(builder.gt(nbCardsPath, 0));
        }
    }


    protected void buildTagsPredicate(List<Predicate> restrictions,
                                      List<CardTag> tags,
                                      Root<CardDetail> cardDetailRoot){
        if(tags != null && tags.size() > 0) {
            Join<CardDetail, CardUserData> joinUserData = cardDetailRoot.join("userData");
            Join<CardUserData, CardTag> joinTags = joinUserData.join("tags");

            restrictions.add(joinTags.in(tags));
        }
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
            for (DeckAssociation current : oldDeck.getCards()) {
                DeckAssociation newAss = new DeckAssociation();
                newAss.setCard(current.getCard());
                newAss.setNbCards(current.getNbCards());
                newAss.setDeck(newDeck);
                session.save(newAss);
            }

            session.save(newDeck);
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
    public List<Object[]> getTagsStats(Deck deck) {
        Session session = this.db.getSession();
        Query query = session.createNamedQuery("tags_stats_from_deck");
        query.setHint( "org.hibernate.readOnly", true );
        query.setParameter("deck", deck);
        List<Object[]> result =  query.getResultList();
        return result;
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
