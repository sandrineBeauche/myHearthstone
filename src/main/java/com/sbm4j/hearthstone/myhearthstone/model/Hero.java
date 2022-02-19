package com.sbm4j.hearthstone.myhearthstone.model;

import javax.persistence.*;


@org.hibernate.annotations.NamedQueries({
        @org.hibernate.annotations.NamedQuery(
                name="hero_from_code",
                query="select h from Hero h where h.code = :code"
        ),
        @org.hibernate.annotations.NamedQuery(
                name = "available_heros",
                query = "select h from Hero h order by h.name"
        )
})
@Entity
@Table(name = "hero")
public class Hero {

    @Id
    @GeneratedValue
    @Column(nullable = false)
    private int id;

    @Column
    private String name;

    @Column
    private String code;

    @ManyToOne
    @JoinColumn(name = "class_id", foreignKey = @ForeignKey(name = "CLASS_ID_HERO__FK"))
    private CardClass classe;

    public Hero(){}

    public Hero(String name, String code, CardClass classe){
        this.name = name;
        this.code = code;
        this.classe = classe;
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

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public CardClass getClasse() {
        return classe;
    }

    public void setClasse(CardClass classe) {
        this.classe = classe;
    }


    @Override
    public String toString() {
        return this.name;
    }
}
