package com.sbm4j.hearthstone.myhearthstone.model;

import javax.persistence.*;

@NamedQueries({
        @NamedQuery(
                name = "DeckList.getCardList",
                query = "select new com.sbm4j.hearthstone.myhearthstone.model.DeckCardListItem(" +
                            "ca.dbfId, ca.id, ca.name, a.nbCards, ca.userData.nbTotalCards," +
                            "ca.cardSet.code, cl.code, ca.rarity.code, ca.cost, ca.cardSet.isStandard, " +
                            "group_concat(t.name)" +
                        ") " +
                        "from DeckAssociation a join a.card ca join ca.cardClass cl join ca.userData.tags t " +
                        "where a.deck = :deck and (cl.code = a.deck.hero.classe.code or cl.code = 'NEUTRAL') " +
                        "group by ca.dbfId, ca.id, ca.name, a.nbCards, ca.userData.nbTotalCards, " +
                                "ca.cardSet.code, cl.code, ca.rarity.code, ca.cost, ca.cardSet.isStandard " +
                        "order by ca.cost, ca.name"
        ),
        @NamedQuery(
                name = "DeckList.manaCurveInf7",
                query = "select a.card.cost, sum(a.nbCards) " +
                        "from DeckAssociation a " +
                        "where a.deck = :deck and a.card.cost < 7 " +
                        "group by a.card.cost " +
                        "order by a.card.cost"
        ),
        @NamedQuery(
                name = "DeckList.manaCurveSup7",
                query = "select sum(a.nbCards) " +
                        "from DeckAssociation a " +
                        "where a.deck = :deck and a.card.cost >= 7 "
        )
})
@Entity
@Table(name = "deckAssociation")
public class DeckAssociation {

    @Id
    @GeneratedValue
    @Column(nullable = false)
    private int id;

    @Column
    private int nbCards;

    @ManyToOne
    @JoinColumn(name = "card_id", foreignKey = @ForeignKey(name = "CARD_ID_ASS_FK"))
    private CardDetail card;

    @ManyToOne
    private Deck deck;

    public DeckAssociation(){}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getNbCards() {
        return nbCards;
    }

    public void setNbCards(int nbCards) {
        this.nbCards = nbCards;
    }

    public CardDetail getCard() {
        return card;
    }

    public void setCard(CardDetail card) {
        this.card = card;
    }

    public Deck getDeck() {
        return deck;
    }

    public void setDeck(Deck deck) {
        this.deck = deck;
    }
}
