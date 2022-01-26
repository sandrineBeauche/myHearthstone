package com.sbm4j.hearthstone.myhearthstone.services.db;

import com.sbm4j.hearthstone.myhearthstone.model.*;
import com.sbm4j.hearthstone.myhearthstone.views.ManaOption;
import org.hibernate.Session;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

public class CriteriaQueryBuilder {

    public CriteriaQuery<CardCatalogItem> buildCatalogQuery(Session session, CatalogCriteria criteria){
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
        return cq;
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

}
