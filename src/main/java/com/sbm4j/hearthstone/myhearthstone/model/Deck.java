package com.sbm4j.hearthstone.myhearthstone.model;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@NamedQueries({
        @NamedQuery(
                name = "Deck.deleteByIdEquals",
                query = "delete from Deck d where d.id = :id"
        ),
        @NamedQuery(
                name = "Deck.getDecksList",
                query = "select new com.sbm4j.hearthstone.myhearthstone.model.DeckListItem(" +
                                    "d.id, d.name, d.summary, d.hero.code, sum(a.nbCards), " +
                                    "sum(least(a.card.userData.nbTotalCards, a.nbCards)), " +
                                    "sum(" +
                                        "(case when a.card.cardSet.isStandard = TRUE then a.nbCards else 0 end)" +
                                    "), " +
                                    "(select group_concat_distinct(t.name)" +
                                        "from DeckAssociation a join a.card.userData.tags t " +
                                        "where a.deck = d and (t.exclusiveGroup = 0 or mod(t.exclusiveGroup, 100) > 0))" +
                        ") " +
                        "from Deck d join d.cards a " +
                        "group by d.id, d.name, d.summary, d.hero.code " +
                        "order by d.hero.code, d.name"
        ),
        @NamedQuery(
                name = "Deck.getDecksListItem",
                query = "select new com.sbm4j.hearthstone.myhearthstone.model.DeckListItem(" +
                            "d.id, d.name, d.summary, d.hero.code, sum(a.nbCards), " +
                            "sum(least(a.card.userData.nbTotalCards, a.nbCards)), " +
                            "sum(" +
                                "(case when a.card.cardSet.isStandard = TRUE then a.nbCards else 0 end)" +
                            "), " +
                            "(select group_concat_distinct(t.name)" +
                                "from DeckAssociation a join a.card.userData.tags t " +
                                "where a.deck = d and (t.exclusiveGroup = 0 or mod(t.exclusiveGroup, 100) > 0))" +
                            ") " +
                        "from Deck d join d.cards a " +
                        "where d.id = :deckId " +
                        "group by d.id, d.name, d.summary, d.hero.code"
        ),
        @NamedQuery(
                name = "Deck.getEmptyDecksList",
                query = "select new com.sbm4j.hearthstone.myhearthstone.model.DeckListItem(" +
                        "d.id, d.name, d.summary, d.hero.code, 0L, 0L, 0L, '') " +
                        "from Deck d " +
                        "where d.cards is empty"
        )
})
@Entity
@Table(name = "deck")
public class Deck {

    @Id
    @GeneratedValue
    @Column(nullable = false)
    private int id;

    @Column(unique = true)
    private String name;

    @Column
    private String summary;

    @OneToMany(mappedBy = "deck", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private List<DeckAssociation> cards = new ArrayList<DeckAssociation>();

    @ManyToOne
    @JoinColumn(name = "hero_id", foreignKey = @ForeignKey(name = "HERO_ID_DECK_FK"))
    private Hero hero;

    @Column(length = 5000)
    private String notes;

    public Deck(){}

    public Deck(String name, Hero hero){
        this.name = name;
        this.hero = hero;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public List<DeckAssociation> getCards() {
        return cards;
    }

    public void setCards(List<DeckAssociation> cards) {
        this.cards = cards;
    }

    public Hero getHero() {
        return hero;
    }

    public void setHero(Hero hero) {
        this.hero = hero;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    @Override
    public String toString() {
        return this.name + "(" + this.hero.getName() + ")";
    }


}
