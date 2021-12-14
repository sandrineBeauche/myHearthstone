package com.sbm4j.hearthstone.myhearthstone.model;

import javax.persistence.*;

@org.hibernate.annotations.NamedQuery(
        name="class_from_code",
        query="select c from CardClass c where c.code = :code"
)
@Entity
@Table(name = "cardClass")
public class CardClass {
    @Id
    @Column(nullable = false)
    @GeneratedValue
    private int id;

    @Column(nullable = false, unique = true)
    private String code;

    @Column(nullable = false)
    private String name;

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
}
