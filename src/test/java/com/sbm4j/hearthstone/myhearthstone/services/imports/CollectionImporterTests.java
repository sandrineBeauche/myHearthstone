package com.sbm4j.hearthstone.myhearthstone.services.imports;

import com.github.database.rider.core.api.connection.ConnectionHolder;
import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.junit5.DBUnitExtension;
import com.github.database.rider.junit5.util.EntityManagerProvider;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.sbm4j.hearthstone.myhearthstone.HearthstoneModuleDBTesting;
import com.sbm4j.hearthstone.myhearthstone.model.CardDetail;
import com.sbm4j.hearthstone.myhearthstone.model.json.JsonUserData;
import com.sbm4j.hearthstone.myhearthstone.services.db.DBManager;
import org.hibernate.Session;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.io.File;
import java.io.FileNotFoundException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(DBUnitExtension.class)
public class CollectionImporterTests {

    protected File jsonCollectionFile;

    protected ImportCollectionAction importer;

    protected DBManager dbManager;

    private ConnectionHolder connectionHolder = () ->
            EntityManagerProvider.instance("pu-hearthstone").connection();

    @BeforeEach
    public void beforeEach(){
        ClassLoader classLoader = getClass().getClassLoader();
        this.jsonCollectionFile= new File(classLoader.getResource("hsreplayCollection.json").getFile());

        Injector injector = Guice.createInjector(
                new HearthstoneModuleDBTesting(null, false));

        this.dbManager = injector.getInstance(DBManager.class);
        this.importer = injector.getInstance(ImportCollectionAction.class);
    }


    @Test
    public void parseCollection() throws FileNotFoundException {
        JsonUserData data = this.importer.parseUserData(this.jsonCollectionFile);
        assertNotNull(data);
    }

    @Test
    @DataSet("importedCatalogDataset.xml")
    public void importCollection() throws FileNotFoundException {
        this.importer.importCollection(this.jsonCollectionFile);

        Session session = this.dbManager.getSession();

        CardDetail card = session.get(CardDetail.class, 922);
        assertEquals(2, card.getUserData().getNbCards());
        assertEquals(4, card.getUserData().getNbTotalCards());

        card = session.get(CardDetail.class, 70107);
        assertEquals(1, card.getUserData().getNbCards());
        assertEquals(2, card.getUserData().getNbTotalCards());
    }
}
