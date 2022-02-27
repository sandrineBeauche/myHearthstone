package com.sbm4j.hearthstone.myhearthstone.services.imports;

import com.github.database.rider.core.api.connection.ConnectionHolder;
import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.junit5.DBUnitExtension;
import com.github.database.rider.junit5.util.EntityManagerProvider;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.sbm4j.hearthstone.myhearthstone.HearthstoneModuleDBTesting;
import com.sbm4j.hearthstone.myhearthstone.model.DeckListItem;
import com.sbm4j.hearthstone.myhearthstone.services.db.DBFacade;
import com.sbm4j.hearthstone.myhearthstone.services.db.DBManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(DBUnitExtension.class)
public class DeckStringExporterTests {

    private ConnectionHolder connectionHolder = () ->
            EntityManagerProvider.instance("pu-hearthstone").connection();

    protected DBManager dbManager;

    protected DBFacade dbFacade;

    protected DeckStringExporter exporter;

    protected DeckStringImporter importer;

    @BeforeEach
    public void beforeEach() throws Exception {
        Injector injector = Guice.createInjector(
                new HearthstoneModuleDBTesting(null));

        this.dbManager = injector.getInstance(DBManager.class);
        this.dbFacade = injector.getInstance(DBFacade.class);
        this.exporter = injector.getInstance(DeckStringExporter.class);
        this.importer = injector.getInstance(DeckStringImporter.class);
    }

    @AfterEach
    public void afterEach(){
        this.dbManager.closeSession();
    }


    @DataSet("bigDataset.xml")
    @Test
    public void exportTest1() throws Exception {
        DeckListItem item = this.dbFacade.getDeckListItem(183);
        String result = this.exporter.export(item);
        assertEquals("AAECAf0ECPjMA5XNA9nRA/fRA/bWA9ToA8eKBJ+SBAuSywPgzAObzQPNzgP7zgOk0QPV6AOgigTAigShkgT1ogQA", result);
    }

    @DataSet("bigDataset.xml")
    @Test
    public void importTest1() throws Exception {
        String deckstring =
                        "#########\n" +
                        "#Deck Mage\n" +
                        "AAECAf0ECPjMA5XNA9nRA/fRA/bWA9ToA8eKBJ+SBAuSywPgzAObzQPNzgP7zgOk0QPV6AOgigTAigShkgT1ogQA\n" +
                        "#Fin";
        DeckListItem deck = this.importer.importDeckString(deckstring, "imported deck 1");

        assertNotNull(deck);
        assertEquals(deck.getName(), "imported deck 1");
        assertEquals(deck.getNbCards(), 30);

    }

}
