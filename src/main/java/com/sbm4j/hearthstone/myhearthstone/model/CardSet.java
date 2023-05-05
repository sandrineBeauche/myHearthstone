package com.sbm4j.hearthstone.myhearthstone.model;

import javax.persistence.*;

@org.hibernate.annotations.NamedQueries({
        @org.hibernate.annotations.NamedQuery(
                name="cardSet_from_code",
                query="select s from CardSet s where s.code = :code"
        ),
        @org.hibernate.annotations.NamedQuery(
                name = "available_sets",
                query = "select s from CardSet s order by s.orderChrono desc, s.name"
        )
})

@Entity
@Table(name = "cardSet")
public class CardSet implements CodedEntity{

    @Id
    @Column(nullable = false)
    @GeneratedValue
    private int id;

    @Column(nullable = false, unique = true)
    private String code;

    @Column(nullable = false)
    private String name;

    @Column()
    protected Boolean isStandard = false;

    @Column()
    protected int orderChrono;

    public CardSet(){}

    public CardSet(int id, String code, String name, boolean standard, int order){
        this.id = id;
        this.code = code;
        this.name = name;
        this.isStandard = standard;
        this.orderChrono = order;
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

    public Boolean isStandard() {
        return isStandard;
    }

    public void setStandard(Boolean standard) {
        isStandard = standard;
    }

    public int getOrderChrono() {
        return orderChrono;
    }

    public void setOrderChrono(int orderChrono) {
        this.orderChrono = orderChrono;
    }

    @Override
    public String toString() {
        return this.name + "(" + this.code + ")";
    }

    public void updateFromJson(CardSet cardSet){
        this.setCode(cardSet.getCode());
        this.setName(cardSet.getName());
        this.setStandard(cardSet.isStandard());
        this.setOrderChrono(cardSet.getOrderChrono());
    }
}
