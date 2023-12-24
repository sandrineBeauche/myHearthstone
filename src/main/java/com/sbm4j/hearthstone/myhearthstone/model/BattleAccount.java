package com.sbm4j.hearthstone.myhearthstone.model;

import javax.persistence.*;

@org.hibernate.annotations.NamedQueries({
        @org.hibernate.annotations.NamedQuery(
                name="connected_account",
                query="select b from BattleAccount b where b.connected = true"
        ),
        @org.hibernate.annotations.NamedQuery(
                name="available_accounts",
                query="select b from BattleAccount b order by b.battleTag"
        )
})
@Entity
@Table(name = "battleAccount")
public class BattleAccount {

    @Id
    @Column(nullable = false, unique = true)
    protected String battleTag;

    @Column(nullable = false, unique = true)
    protected String email;

    @Column(nullable = false)
    protected String password;

    @Column(nullable = false)
    protected String account_lo;

    @Column()
    protected Boolean connected = false;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getBattleTag() {
        return battleTag;
    }

    public void setBattleTag(String battleTag) {
        this.battleTag = battleTag;
    }

    public String getAccount_lo() {
        return account_lo;
    }

    public void setAccount_lo(String account_lo) {
        this.account_lo = account_lo;
    }

    public Boolean getConnected() {
        return connected;
    }

    public void setConnected(Boolean connected) {
        this.connected = connected;
    }
}
