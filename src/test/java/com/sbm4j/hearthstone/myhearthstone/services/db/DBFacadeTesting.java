package com.sbm4j.hearthstone.myhearthstone.services.db;

import com.sbm4j.hearthstone.myhearthstone.model.*;
import javafx.util.Pair;

import javax.persistence.NoResultException;
import java.io.FileNotFoundException;
import java.net.URISyntaxException;
import java.util.List;

public class DBFacadeTesting implements DBFacade{

    @Override
    public Rarity getRarity(String code) throws NoResultException {
        return null;
    }

    @Override
    public CardClass getClasse(String code) throws NoResultException {
        return null;
    }

    @Override
    public CardSet getSet(String code) throws NoResultException {
        return null;
    }

    @Override
    public CardTag getTag(String code) {
        return null;
    }

    @Override
    public Hero getHero(String code) throws NoResultException {
        return null;
    }

    @Override
    public List<CardClass> getClasses(boolean includeAll) {
        CardClass [] classes = {
                new CardClass(4, "ALL", "Toutes les classes"),
                new CardClass(1, "SHAMAN", "Chaman"),
                new CardClass(2, "PALADIN", "Paladin"),
                new CardClass(3, "MAGE", "Mage")
        };
        return List.of(classes);
    }

    @Override
    public List<CardSet> getSets(boolean includeWild) {
        CardSet [] sets = {
                new CardSet(1, "STANDARD", "Standard", false, -1),
                new CardSet(2, "ALTERAC_VALLEY", "Divisés dans la vallée d'Alterac", true, 3),
                new CardSet(3, "STORMWIND", "Unis à Hurlevent", true, 2),
                new CardSet(4, "THE_BARRENS", "Forgés dans les tarides", true, 1)
        };
        return List.of(sets);

    }

    @Override
    public List<Rarity> getRarities(boolean includeAll) {
        Rarity [] rarities = {
                new Rarity(-1, "ALL", "Toutes les raretés"),
                new Rarity(1, "COMMON", "Commune"),
                new Rarity(2, "RARE", "Rare"),
                new Rarity(3, "EPIC", "Epique")
        };
        return List.of(rarities);
    }

    @Override
    public List<CardTag> getTags() {
        return null;
    }

    @Override
    public List<Hero> getHeros() {
        return null;
    }

    @Override
    public Deck createDeck(String name, Hero hero) {
        return null;
    }

    @Override
    public boolean deleteDeck(int id) {
        return false;
    }

    @Override
    public Deck duplicateDeck(int id, String name) {
        return null;
    }

    @Override
    public boolean addCardToDeck(int dbfId, Deck deck) {
        return false;
    }

    @Override
    public boolean removeCardFromDeck(int dbfId, Deck deck, boolean all) {
        return false;
    }

    @Override
    public List<DeckListItem> getDeckList() {
        return null;
    }

    @Override
    public DeckListItem getDeckListItem(int deckId) {
        return null;
    }

    @Override
    public List<DeckCardListItem> getDeckCardList(Deck deck) {
        return null;
    }

    @Override
    public DeckCardListItem getDeckCardListItem(Deck deck, int dbfId) {
        return null;
    }

    @Override
    public Integer[] getManaCurveStats(Deck deck) {
        return null;
    }

    @Override
    public List<Pair<String, Integer>> getTagsStats(Deck deck) {
        return null;
    }

    @Override
    public List<CardCatalogItem> getCatalog(CatalogCriteria criteria) {
        return List.of(
                new CardCatalogItem(1, "AT_001", "Lance de flammes", 2, 1),
                new CardCatalogItem(2, "AT_002", "Effigie", 2, 2)
        );
    }


}
