package com.sbm4j.hearthstone.myhearthstone.services.db;

import com.github.database.rider.core.api.connection.ConnectionHolder;
import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.junit5.DBUnitExtension;
import com.github.database.rider.junit5.util.EntityManagerProvider;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.sbm4j.hearthstone.myhearthstone.HearthstoneModuleDBTesting;
import com.sbm4j.hearthstone.myhearthstone.model.*;
import org.hibernate.Session;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;


@ExtendWith(DBUnitExtension.class)
public class DBManagerTests {

    @TempDir
    protected File tempDir;

    protected DBManager manager;

    private ConnectionHolder connectionHolder = () ->
            EntityManagerProvider.instance("pu-hearthstone").connection();

    @BeforeEach
    public void beforeEach(){
        Injector injector = Guice.createInjector(new HearthstoneModuleDBTesting(this.tempDir));
        this.manager = injector.getInstance(DBManager.class);
    }

    @Test
    @DataSet("emptyDataset.xml")
    public void saveToDatabaseTest() throws Exception {
        Session session = manager.getSession();

        CardSet cardSet = new CardSet();
        cardSet.setCode("LEGACY");
        cardSet.setName("legacy");

        CardClass classe  = new CardClass();
        classe.setCode("NEUTRAL");
        classe.setName("Neutre");

        Rarity rarity = new Rarity();
        rarity.setCost(0);
        rarity.setCostGold(0);
        rarity.setGain(0);
        rarity.setGainGold(0);
        rarity.setCode("FREE");
        rarity.setName("Gratuit");

        CardTag tag1 = new CardTag();
        tag1.setCode("TAUNT");
        tag1.setExclusiveGroup(0);
        tag1.setName("Provocation");

        CardTag tag2 = new CardTag();
        tag2.setCode("MINION");
        tag2.setExclusiveGroup(0);
        tag2.setName("Serviteur");

        CardDetail card = new CardDetail();
        card.setArtist("Donato Giancola");
        card.setAttack(1);
        card.setCost(1);
        card.setHealth(2);
        card.setDbfId(922);
        card.setDurability(0);
        card.setFlavor("Si Comté-de-l’Or n’était protégée que par des serviteurs 1/2, elle aurait dû être envahie il y a des années.");
        card.setId("ESSAI_01");
        card.setName("Soldat de Comté-de-l’Or");
        card.setText("&lt;b&gt;Provocation&lt;/b&gt;");
        card.setCardSet(cardSet);
        card.getCardClass().add(classe);
        card.setRarity(rarity);

        CardUserData userData = new CardUserData();
        userData.setNbCards(1);
        userData.setNbGolden(0);
        userData.setNbTotalCards(1);
        userData.getTags().add(tag1);
        userData.getTags().add(tag2);

        card.setUserData(userData);

        session.beginTransaction();
        session.save(userData);
        session.save(cardSet);
        session.save(classe);
        session.save(rarity);
        session.save(tag1);
        session.save(tag2);
        session.save(card);
        session.getTransaction().commit();
        session.close();
    }



    @Test
    @DataSet("saveCardDataset.xml")
    public void detail2UserRelationTest(){
        Session anotherSession = this.manager.getSession();
        CardDetail card = anotherSession.get(CardDetail.class, 922);
        anotherSession.beginTransaction();
        anotherSession.delete(card);
        anotherSession.getTransaction().commit();
        this.manager.closeSession();

        Session anotherSession2 = this.manager.getSession();
        CardDetail details = anotherSession2.get(CardDetail.class, 922);
        CardUserData d = anotherSession2.get(CardUserData.class, 922);

        assertNull(details);
        assertNotNull(d);
    }


    @Test
    public void checkFunctionTest() throws SQLException {
        boolean result = this.manager.hasFunctionInDatabase("GROUP_CONCAT_DISTINCT");
        assertTrue(result);
    }

}
