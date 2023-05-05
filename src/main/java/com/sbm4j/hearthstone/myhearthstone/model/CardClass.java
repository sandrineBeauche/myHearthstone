package com.sbm4j.hearthstone.myhearthstone.model;


import javax.persistence.*;
import org.hibernate.annotations.NamedQuery;
import org.hibernate.annotations.NamedQueries;

@NamedQueries({
        @NamedQuery(
                name = "class_from_code",
                query = "select c from CardClass c where c.code = :code"
        ),
        @NamedQuery(
                name = "available_classes",
                query = "select c from CardClass c order by c.name"
        )
})
@Entity
@Table(name = "cardClass")
public class CardClass implements CodedEntity{
    @Id
    @Column(nullable = false)
    @GeneratedValue
    private int id;

    @Column(nullable = false, unique = true)
    private String code;

    @Column(nullable = false)
    private String name;

    public CardClass(){}

    public CardClass(int id, String code, String name){
        this.id = id;
        this.code = code;
        this.name = name;
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

    @Override
    public String toString() {
        return this.name + "(" + this.code + ")";
    }

    public void updateFromJson(CardClass cardClass){
        this.setCode(cardClass.getCode());
        this.setName(cardClass.getName());
    }
}
