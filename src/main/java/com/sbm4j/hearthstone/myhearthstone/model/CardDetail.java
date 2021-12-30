package com.sbm4j.hearthstone.myhearthstone.model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import org.hibernate.annotations.NamedQuery;
import org.hibernate.annotations.NamedQueries;

@NamedQueries({
        @NamedQuery(
                name = "json_from_card_details",
                query = "select c.jsonDesc from CardDetail c where c.dbfId = :dbfId"
        ),
        @NamedQuery(
                name="card_from_dbfid",
                query="select t from CardTag t where t.code = :code"
        )
})
@Entity
@Table(name = "cardDetail")
public class CardDetail {
    @Id
    @Column(nullable = false)
    private int dbfId;

    @Column
    private String artist;

    @Column
    private int attack;

    @Column
    private int cost;

    @Column
    private String flavor;

    @Column
    private String id;

    @Column
    private String name;

    @Column
    private String text;

    @Column
    private int durability;

    @Column
    private int health;


    @Column
    private Boolean collectible = true;

    @Column
    private Boolean elite = false;

    @Column
    private String questReward;

    @Column
    private String jsonDesc;

    @Column
    private String howToEarn;

    @Column
    private String howToEarnGolden;

    @ManyToOne
    @JoinColumn(name = "cardSet_id", foreignKey = @ForeignKey(name = "CARDSET_ID_FK"))
    private CardSet cardSet;

    @ManyToMany
    private List<CardClass> cardClass = new ArrayList<CardClass>();

    @ManyToOne
    @JoinColumn(name = "rarity_id", foreignKey = @ForeignKey(name = "RARITY_ID_FK"))
    private Rarity rarity;




    @OneToOne
    @PrimaryKeyJoinColumn
    private CardUserData userData;


    public int getDbfId() {
        return dbfId;
    }

    public void setDbfId(int dbfId) {
        this.dbfId = dbfId;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public int getAttack() {
        return attack;
    }

    public void setAttack(int attack) {
        this.attack = attack;
    }

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    public String getFlavor() {
        return flavor;
    }

    public void setFlavor(String flavor) {
        this.flavor = flavor;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getDurability() {
        return durability;
    }

    public void setDurability(int durability) {
        this.durability = durability;
    }

    public CardSet getCardSet() {
        return cardSet;
    }

    public void setCardSet(CardSet cardSet) {
        this.cardSet = cardSet;
    }

    public List<CardClass> getCardClass() {
        return cardClass;
    }

    public void setCardClass(List<CardClass> cardClass) {
        this.cardClass = cardClass;
    }

    public Rarity getRarity() {
        return rarity;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public void setRarity(Rarity rarity) {
        this.rarity = rarity;
    }

    public Boolean getCollectible() {
        return collectible;
    }

    public void setCollectible(Boolean collectible) {
        this.collectible = collectible;
    }

    public Boolean getElite() {
        return elite;
    }

    public void setElite(Boolean elite) {
        this.elite = elite;
    }

    public String getJsonDesc() {
        return jsonDesc;
    }

    public void setJsonDesc(String jsonDesc) {
        this.jsonDesc = jsonDesc;
    }

    public String getQuestReward() {
        return questReward;
    }

    public void setQuestReward(String questReward) {
        this.questReward = questReward;
    }

    public String getHowToEarn() {
        return howToEarn;
    }

    public void setHowToEarn(String howToEarn) {
        this.howToEarn = howToEarn;
    }

    public String getHowToEarnGolden() {
        return howToEarnGolden;
    }

    public void setHowToEarnGolden(String howToEarnGolden) {
        this.howToEarnGolden = howToEarnGolden;
    }

    public CardUserData getUserData() {
        return userData;
    }

    public void setUserData(CardUserData userData) {
        this.userData = userData;
    }
}
