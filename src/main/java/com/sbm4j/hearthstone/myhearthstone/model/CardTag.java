package com.sbm4j.hearthstone.myhearthstone.model;

import javax.persistence.*;

@NamedQueries({
        @NamedQuery(
                name="tag_from_code",
                query="select t from CardTag t where t.code = :code"
        ),
        @NamedQuery(
                name="available_tags",
                query="select t from CardTag t order by t.name"
        ),
        @NamedQuery(
                name = "tags_stats_from_deck",
                query = "select new com.sbm4j.hearthstone.myhearthstone.model.TagStat(t.name, sum(a.nbCards)) " +
                        "from DeckAssociation a join a.card.userData.tags t " +
                        "where a.deck = :deck " +
                        "group by t.name " +
                        "order by t.name"
        ),
        @NamedQuery(
                name="available_user_tags",
                query="select t from CardTag t where t.isUser = true order by t.name"
        ),
        @NamedQuery(
                name="associated_user_tags",
                query="select t " +
                        "from CardDetail c join c.userData.tags t " +
                        "where t.isUser = true and c = :card " +
                        "order by t.name"
        )
})
@NamedNativeQuery(
        name="delete_associations_user_tags",
        query="DELETE FROM CARDUSERDATA_CARDTAG WHERE TAGS_ID = :tagId"
)
@Entity
@Table(name = "cardTag")
public class CardTag {
    @Id
    @Column(nullable = false)
    @GeneratedValue
    private int id;

    @Column(unique = true)
    private String code;

    @Column(nullable = false)
    private String name;

    @Column
    private Boolean isUser = false;

    @Column
    private int exclusiveGroup;

    public CardTag(){}

    public CardTag(int id, String code, String name, boolean isUser, int exclusiveGroup){
        this.id = id;
        this.code = code;
        this.name = name;
        this.isUser = isUser;
        this.exclusiveGroup = exclusiveGroup;

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getUser() {
        return isUser;
    }

    public void setUser(Boolean user) {
        isUser = user;
    }

    public int getExclusiveGroup() {
        return exclusiveGroup;
    }

    public void setExclusiveGroup(int exclusiveGroup) {
        this.exclusiveGroup = exclusiveGroup;
    }

    @Override
    public String toString() {
        return this.name;
    }
}
