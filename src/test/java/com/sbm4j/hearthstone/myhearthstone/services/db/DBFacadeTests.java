package com.sbm4j.hearthstone.myhearthstone.services.db;

import com.github.database.rider.core.api.connection.ConnectionHolder;
import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.junit5.DBUnitExtension;
import com.github.database.rider.junit5.util.EntityManagerProvider;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.sbm4j.hearthstone.myhearthstone.HearthstoneModuleTesting;
import com.sbm4j.hearthstone.myhearthstone.model.CardClass;
import com.sbm4j.hearthstone.myhearthstone.model.CardSet;
import com.sbm4j.hearthstone.myhearthstone.model.CardTag;
import com.sbm4j.hearthstone.myhearthstone.model.Rarity;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;

import javax.persistence.NoResultException;

import java.io.File;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(DBUnitExtension.class)
public class DBFacadeTests {

    @TempDir
    protected File tempDir;

    protected DBFacade facade;

    protected DBManager db;

    private ConnectionHolder connectionHolder = () ->
            EntityManagerProvider.instance("pu-hearthstone").connection();

    @BeforeEach
    public void beforeEach(){
        Injector injector = Guice.createInjector(new HearthstoneModuleTesting(this.tempDir));
        this.facade = injector.getInstance(DBFacade.class);
        this.db = injector.getInstance(DBManager.class);
    }

    @Test
    public void initDBTest() throws Exception{
        this.facade.initDB();
    }

    @Test
    @DataSet("initDBDataset.xml")
    public void getExisitingRarity() throws Exception {
        Rarity result = this.facade.getRarity("COMMON");
        assertNotNull(result);
    }

    @Test
    @DataSet("initDBDataset.xml")
    public void getNonExisitingRarity() throws Exception {
        Assertions.assertThrows(NoResultException.class, () ->{
            Rarity result = this.facade.getRarity("BLABLA");
        });
    }


    @Test
    @DataSet("initDBDataset.xml")
    public void getExisitingCardClass() throws Exception {
        CardClass result = this.facade.getClasse("SHAMAN");
        assertNotNull(result);
    }

    @Test
    @DataSet("initDBDataset.xml")
    public void getNonExisitingCardClasse() throws Exception {
        Assertions.assertThrows(NoResultException.class, () ->{
            CardClass result = this.facade.getClasse("BLABLA");
        });
    }


    @Test
    @DataSet("initDBDataset.xml")
    public void getExisitingCardSet() throws Exception {
        CardSet result = this.facade.getSet("NAXX");
        assertNotNull(result);
    }

    @Test
    @DataSet("initDBDataset.xml")
    public void getNonExisitingCardSet() throws Exception {
        Assertions.assertThrows(NoResultException.class, () ->{
            CardSet result = this.facade.getSet("BLABLA");
        });
    }


    @Test
    @DataSet("initDBDataset.xml")
    public void getExisitingTag() throws Exception {
        CardTag result = this.facade.getTag("BATTLECRY");
        assertNotNull(result);
    }

    @Test
    @DataSet("initDBDataset.xml")
    public void getNonExisitingTag() throws Exception {
        Assertions.assertThrows(NoResultException.class, () ->{
            CardTag result = this.facade.getTag("BLABLA");
        });
    }


}
