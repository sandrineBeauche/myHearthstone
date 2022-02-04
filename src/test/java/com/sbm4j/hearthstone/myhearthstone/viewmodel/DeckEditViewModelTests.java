package com.sbm4j.hearthstone.myhearthstone.viewmodel;

import com.github.database.rider.core.api.connection.ConnectionHolder;
import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.core.api.dataset.DataSetFormat;
import com.github.database.rider.core.api.dataset.ExpectedDataSet;
import com.github.database.rider.core.api.exporter.ExportDataSet;
import com.github.database.rider.junit5.DBUnitExtension;
import com.github.database.rider.junit5.util.EntityManagerProvider;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.sbm4j.hearthstone.myhearthstone.HearthstoneModuleDBTesting;
import com.sbm4j.hearthstone.myhearthstone.model.DeckCardListItem;
import com.sbm4j.hearthstone.myhearthstone.model.DeckListItem;
import com.sbm4j.hearthstone.myhearthstone.services.db.DBFacade;
import com.sbm4j.hearthstone.myhearthstone.services.db.DBManager;
import com.sbm4j.hearthstone.myhearthstone.views.DeckCardListViewDBTest;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.api.FxToolkit;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import static org.hamcrest.Matchers.arrayContaining;
import static org.mockito.Mockito.*;

import java.io.File;
import java.util.List;

@ExtendWith(DBUnitExtension.class)
public class DeckEditViewModelTests {

    protected DBManager dbManager;

    protected DBFacade dbFacade;

    protected DeckEditViewModel viewModel;

    private ConnectionHolder connectionHolder = () ->
            EntityManagerProvider.instance("pu-hearthstone").connection();

    @BeforeEach
    public void beforeEach() throws Exception {
        Injector injector = Guice.createInjector(
                new HearthstoneModuleDBTesting(null));

        this.dbManager = injector.getInstance(DBManager.class);
        this.dbFacade = injector.getInstance(DBFacade.class);
        this.viewModel = injector.getInstance(DeckEditViewModel.class);

        FxToolkit.registerPrimaryStage();
        TableView<DeckCardListItem> table = new TableView<>();
        this.viewModel.setSelectedCardModel(table);

        TabPane tab = new TabPane(new Tab("tab1"), new Tab("tab2"), new Tab("tab3"));
        this.viewModel.setTabSelectionModel(tab.getSelectionModel());

        this.viewModel.initialize(null, null);
    }


    @Test
    @DataSet("collectionWithDecks1.xml")
    @ExpectedDataSet(value = "expectedAddNewCard.xml", ignoreCols = "id")
    //@ExportDataSet(format = DataSetFormat.XML,outputName="target/exported/xml/result.xml")
    public void testAddNewCard(){
        DeckListItem deckItem = this.dbFacade.getDeckListItem(2);
        this.viewModel.showDeck(deckItem);
        this.viewModel.getTabSelectionModel().select(1);
        this.viewModel.refreshCurrentTab();

        this.viewModel.addCardFromDbfId(70005);

        ObservableList<DeckCardListItem> lst = this.viewModel.getCardsList();
        assertThat(lst.toArray(new DeckCardListItem[0]),
                arrayContaining(
                        allOf(
                                hasProperty("dbfId", equalTo(66880)),
                                hasProperty("nbCards", equalTo(1))
                        ),
                        allOf(
                                hasProperty("dbfId", equalTo(66848)),
                                hasProperty("nbCards", equalTo(2))
                        ),
                        allOf(
                                hasProperty("dbfId", equalTo(70005)),
                                hasProperty("nbCards", equalTo(1))
                        )
                )
        );
    }

    @Test
    @DataSet("collectionWithDecks1.xml")
    @ExpectedDataSet(value = "expectedAddCard.xml", ignoreCols = "id")
    //@ExportDataSet(format = DataSetFormat.XML,outputName="target/exported/xml/result.xml")
    public void testAddCard(){
        DeckListItem deckItem = this.dbFacade.getDeckListItem(2);
        this.viewModel.showDeck(deckItem);
        this.viewModel.getTabSelectionModel().select(1);
        this.viewModel.refreshCurrentTab();

        this.viewModel.addCardFromDbfId(66880);

        ObservableList<DeckCardListItem> lst = this.viewModel.getCardsList();
        assertThat(lst.toArray(new DeckCardListItem[0]),
                arrayContaining(
                        allOf(
                                hasProperty("dbfId", equalTo(66880)),
                                hasProperty("nbCards", equalTo(2))
                        ),
                        allOf(
                                hasProperty("dbfId", equalTo(66848)),
                                hasProperty("nbCards", equalTo(2))
                        )
                )
        );
    }


    @Test
    @DataSet("collectionWithDecks1.xml")
    @ExpectedDataSet(value = "expectedAddCard.xml", ignoreCols = "id")
    //@ExportDataSet(format = DataSetFormat.XML,outputName="target/exported/xml/result.xml")
    public void testIncrCard(){
        DeckListItem deckItem = this.dbFacade.getDeckListItem(2);
        this.viewModel.showDeck(deckItem);
        this.viewModel.getTabSelectionModel().select(1);
        this.viewModel.refreshCurrentTab();

        DeckCardListItem cardItem = this.viewModel.getCardsList().get(0);
        this.viewModel.getSelectedCardModel().select(cardItem);
        this.viewModel.incrSelectedCard();

        ObservableList<DeckCardListItem> lst = this.viewModel.getCardsList();
        assertThat(lst.toArray(new DeckCardListItem[0]),
                arrayContaining(
                        allOf(
                                hasProperty("dbfId", equalTo(66880)),
                                hasProperty("nbCards", equalTo(2))
                        ),
                        allOf(
                                hasProperty("dbfId", equalTo(66848)),
                                hasProperty("nbCards", equalTo(2))
                        )
                )
        );
    }

    @Test
    @DataSet("collectionWithDecks1.xml")
    @ExpectedDataSet(value = "expectedDecrCard.xml", ignoreCols = "id")
    //@ExportDataSet(format = DataSetFormat.XML,outputName="target/exported/xml/result.xml")
    public void testDecrCard(){
        DeckListItem deckItem = this.dbFacade.getDeckListItem(2);
        this.viewModel.showDeck(deckItem);
        this.viewModel.getTabSelectionModel().select(1);
        this.viewModel.refreshCurrentTab();

        DeckCardListItem cardItem = this.viewModel.getCardsList().get(1);
        this.viewModel.getSelectedCardModel().select(cardItem);
        this.viewModel.decrSelectedCard();

        ObservableList<DeckCardListItem> lst = this.viewModel.getCardsList();
        assertThat(lst.toArray(new DeckCardListItem[0]),
                arrayContaining(
                        allOf(
                                hasProperty("dbfId", equalTo(66880)),
                                hasProperty("nbCards", equalTo(1))
                        ),
                        allOf(
                                hasProperty("dbfId", equalTo(66848)),
                                hasProperty("nbCards", equalTo(1))
                        )
                )
        );
    }

    @Test
    @DataSet("collectionWithDecks1.xml")
    @ExpectedDataSet(value = "expectedDecrRemoveCard.xml", ignoreCols = "id")
    //@ExportDataSet(format = DataSetFormat.XML,outputName="target/exported/xml/result.xml")
    public void testDecrRemoveCard(){
        DeckListItem deckItem = this.dbFacade.getDeckListItem(2);
        this.viewModel.showDeck(deckItem);
        this.viewModel.getTabSelectionModel().select(1);
        this.viewModel.refreshCurrentTab();

        DeckCardListItem cardItem = this.viewModel.getCardsList().get(0);
        this.viewModel.getSelectedCardModel().select(cardItem);
        this.viewModel.decrSelectedCard();

        ObservableList<DeckCardListItem> lst = this.viewModel.getCardsList();
        assertThat(lst.toArray(new DeckCardListItem[0]),
                arrayContaining(
                        allOf(
                                hasProperty("dbfId", equalTo(66848)),
                                hasProperty("nbCards", equalTo(2))
                        )
                )
        );
    }


    @Test
    @DataSet("collectionWithDecks1.xml")
    @ExpectedDataSet(value = "expectedRemoveCard.xml", ignoreCols = "id")
    //@ExportDataSet(format = DataSetFormat.XML,outputName="target/exported/xml/result.xml")
    public void testRemoveCard(){
        DeckListItem deckItem = this.dbFacade.getDeckListItem(2);
        this.viewModel.showDeck(deckItem);
        this.viewModel.getTabSelectionModel().select(1);
        this.viewModel.refreshCurrentTab();

        DeckCardListItem cardItem = this.viewModel.getCardsList().get(1);
        this.viewModel.getSelectedCardModel().select(cardItem);
        this.viewModel.deleteSelectedCard();

        ObservableList<DeckCardListItem> lst = this.viewModel.getCardsList();
        assertThat(lst.toArray(new DeckCardListItem[0]),
                arrayContaining(
                        allOf(
                                hasProperty("dbfId", equalTo(66880)),
                                hasProperty("nbCards", equalTo(1))
                        )
                )
        );
    }

}
