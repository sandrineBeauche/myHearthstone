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
import javafx.scene.chart.XYChart;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import static org.hamcrest.Matchers.arrayContaining;

@Disabled
@ExtendWith(DBUnitExtension.class)
public class DeckEditViewModelTests {

    protected DBManager dbManager;

    protected DBFacade dbFacade;

    protected DeckEditViewModel viewModel;

    private ConnectionHolder connectionHolder = () ->
            EntityManagerProvider.instance("pu-hearthstone").connection();


    public static <T> org.hamcrest.Matcher<T> isTagStats(String tag, int value) {
        return allOf(
                hasProperty("tag", equalTo(tag)),
                hasProperty("value", equalTo(value))
        );
    }

    public static <T> org.hamcrest.Matcher<T> isCardListItem(int dbfId, int nbCards) {
        return allOf(
                hasProperty("dbfId", equalTo(dbfId)),
                hasProperty("nbCards", equalTo(nbCards))
        );
    }

    public static <T> org.hamcrest.Matcher<T[]> isManaCurve(int [] values) {
        return arrayContaining(
                hasProperty("YValue", equalTo(values[0])),
                hasProperty("YValue", equalTo(values[1])),
                hasProperty("YValue", equalTo(values[2])),
                hasProperty("YValue", equalTo(values[3])),
                hasProperty("YValue", equalTo(values[4])),
                hasProperty("YValue", equalTo(values[5])),
                hasProperty("YValue", equalTo(values[6])),
                hasProperty("YValue", equalTo(values[7]))
        );
    }

    public static <T> org.hamcrest.Matcher<T> isDeckListItem(int deckId, int nbCards,
                                                               int nbCardsCollection, int nbCardStandard) {
        return allOf(
                hasProperty("deckId", equalTo(deckId)),
                hasProperty("nbCards", equalTo(nbCards)),
                hasProperty("nbCardsInCollection", equalTo(nbCardsCollection)),
                hasProperty("nbStandardCards", equalTo(nbCardStandard))
        );
    }


    @BeforeEach
    public void beforeEach() throws Exception {
        Injector injector = Guice.createInjector(
                new HearthstoneModuleDBTesting(null));

        this.dbManager = injector.getInstance(DBManager.class);
        this.dbFacade = injector.getInstance(DBFacade.class);
        this.viewModel = injector.getInstance(DeckEditViewModel.class);

        this.viewModel.initialize(null, null);
    }


    @Test
    @DataSet("collectionWithDecks1.xml")
    @ExpectedDataSet(value = "expectedAddNewCard.xml", ignoreCols = "id")
    //@ExportDataSet(format = DataSetFormat.XML,outputName="target/exported/xml/result.xml")
    public void testAddNewCard(){
        DeckListItem deckItem = this.dbFacade.getDeckListItem(2);
        this.viewModel.showDeck(deckItem);
        this.viewModel.refreshCardListTab();

        this.viewModel.addCardFromDbfId(70005);

        assertThat(this.viewModel.getCardsList(),
                hasItems(
                        isCardListItem(66880, 1),
                        isCardListItem(66848, 2),
                        isCardListItem(70005, 1)
                )
        );

        assertThat(this.viewModel.getCurveManaData().toArray(new XYChart.Data[0]),
                isManaCurve(new int[]{0,0,0,1,1,0,0,2}));

        assertThat(this.viewModel.getStatsTagsList(),
                hasItems(
                        isTagStats("Gèle", 1),
                        isTagStats("Sort", 1),
                        isTagStats("Givre", 1)
                )
        );

        assertThat(this.viewModel.currentDeckItem,
                isDeckListItem(2, 4, 4, 4));
    }

    @Test
    @DataSet("collectionWithDecks1.xml")
    @ExpectedDataSet(value = "expectedAddNewCard2.xml", ignoreCols = "id")
    //@ExportDataSet(format = DataSetFormat.XML,outputName="target/exported/xml/result.xml")
    public void testAddNewCardNotInCollection(){
        DeckListItem deckItem = this.dbFacade.getDeckListItem(2);
        this.viewModel.showDeck(deckItem);
        this.viewModel.refreshCardListTab();

        this.viewModel.addCardFromDbfId(67210);

        assertThat(this.viewModel.getCardsList(),
                hasItems(
                        isCardListItem(66880, 1),
                        isCardListItem(66848, 2),
                        isCardListItem(67210, 1)
                )
        );

        assertThat(this.viewModel.getCurveManaData().toArray(new XYChart.Data[0]),
                isManaCurve(new int[]{0,0,1,0,1,0,0,2}));


        assertThat(this.viewModel.getStatsTagsList(),
                hasItems(
                        isTagStats("Victoire honorable", 1),
                        isTagStats("Sort", 1),
                        isTagStats("Arcane", 1)
                )
        );


        assertThat(this.viewModel.currentDeckItem,
                isDeckListItem(2, 4, 3, 4));
    }

    @Test
    @DataSet("collectionWithDecks1.xml")
    @ExpectedDataSet(value = "expectedAddCard.xml", ignoreCols = "id")
    //@ExportDataSet(format = DataSetFormat.XML,outputName="target/exported/xml/result.xml")
    public void testAddCard(){
        DeckListItem deckItem = this.dbFacade.getDeckListItem(2);
        this.viewModel.showDeck(deckItem);
        this.viewModel.refreshCardListTab();

        this.viewModel.addCardFromDbfId(66880);

        assertThat(this.viewModel.getCardsList(),
                hasItems(
                        isCardListItem(66880, 2),
                        isCardListItem(66848, 2)
                )
        );

        assertThat(this.viewModel.getCurveManaData().toArray(new XYChart.Data[0]),
                isManaCurve(new int[]{0,0,0,0,2,0,0,2}));

        assertThat(this.viewModel.getStatsTagsList(),
                hasItems(
                        isTagStats("Cri de guerre", 4),
                        isTagStats("Serviteur", 2)
                )
        );

        assertThat(this.viewModel.currentDeckItem,
                isDeckListItem(2, 4, 4, 4));
    }


    @Test
    @DataSet("collectionWithDecks1.xml")
    @ExpectedDataSet(value = "expectedAddCard.xml", ignoreCols = "id")
    //@ExportDataSet(format = DataSetFormat.XML,outputName="target/exported/xml/result.xml")
    public void testIncrCard(){
        DeckListItem deckItem = this.dbFacade.getDeckListItem(2);
        this.viewModel.showDeck(deckItem);
        this.viewModel.refreshCardListTab();

        DeckCardListItem cardItem = this.viewModel.getCardsList().get(0);
        this.viewModel.incrSelectedCard(cardItem); /* dbfId 66880 */

        assertThat(this.viewModel.getCardsList(),
                hasItems(
                        isCardListItem(66880, 2),
                        isCardListItem(66848, 2)
                )
        );

        assertThat(this.viewModel.getCurveManaData().toArray(new XYChart.Data[0]),
                isManaCurve(new int[]{0,0,0,0,2,0,0,2}));

        assertThat(this.viewModel.getStatsTagsList(),
                hasItems(
                        isTagStats("Cri de guerre", 4),
                        isTagStats("Serviteur", 2)
                )
        );

        assertThat(this.viewModel.currentDeckItem,
                isDeckListItem(2, 4, 4, 4));
    }

    @Test
    @DataSet("collectionWithDecks1.xml")
    @ExpectedDataSet(value = "expectedAddCard2.xml", ignoreCols = "id")
    //@ExportDataSet(format = DataSetFormat.XML,outputName="target/exported/xml/result.xml")
    public void testIncrCard2(){
        DeckListItem deckItem = this.dbFacade.getDeckListItem(2);
        this.viewModel.showDeck(deckItem);
        this.viewModel.refreshCardListTab();

        DeckCardListItem cardItem = this.viewModel.getCardsList().get(0);
        this.viewModel.incrSelectedCard(cardItem); /* dbfId 66880 */
        this.viewModel.incrSelectedCard(cardItem);

        assertThat(this.viewModel.getCardsList(),
                hasItems(
                        isCardListItem(66880, 3),
                        isCardListItem(66848, 2)
                )
        );

        assertThat(this.viewModel.getCurveManaData().toArray(new XYChart.Data[0]),
                isManaCurve(new int[]{0,0,0,0,3,0,0,2}));

        assertThat(this.viewModel.getStatsTagsList(),
                hasItems(
                        isTagStats("Cri de guerre", 5),
                        isTagStats("Serviteur", 3)
                )
        );

        assertThat(this.viewModel.currentDeckItem,
                isDeckListItem(2, 5, 4, 5));
    }

    @Test
    @DataSet("collectionWithDecks1.xml")
    @ExpectedDataSet(value = "expectedDecrCard.xml", ignoreCols = "id")
    //@ExportDataSet(format = DataSetFormat.XML,outputName="target/exported/xml/result.xml")
    public void testDecrCard(){
        DeckListItem deckItem = this.dbFacade.getDeckListItem(2);
        this.viewModel.showDeck(deckItem);
        this.viewModel.refreshCardListTab();

        DeckCardListItem cardItem = this.viewModel.getCardsList().get(1);
        this.viewModel.decrSelectedCard(cardItem);

        assertThat(this.viewModel.getCardsList(),
                hasItems(
                        isCardListItem(66880, 1),
                        isCardListItem(66848, 1)
                )
        );

        assertThat(this.viewModel.getCurveManaData().toArray(new XYChart.Data[0]),
                isManaCurve(new int[]{0,0,0,0,1,0,0,1}));

        assertThat(this.viewModel.getStatsTagsList(),
                hasItems(
                        isTagStats("Cri de guerre", 2),
                        isTagStats("Serviteur", 1)
                )
        );

        assertThat(this.viewModel.currentDeckItem,
                isDeckListItem(2, 2, 2, 2));
    }

    @Test
    @DataSet("collectionWithDecks1.xml")
    @ExpectedDataSet(value = "expectedDecrRemoveCard.xml", ignoreCols = "id")
    //@ExportDataSet(format = DataSetFormat.XML,outputName="target/exported/xml/result.xml")
    public void testDecrRemoveCard(){
        DeckListItem deckItem = this.dbFacade.getDeckListItem(2);
        this.viewModel.showDeck(deckItem);
        this.viewModel.refreshCardListTab();

        DeckCardListItem cardItem = this.viewModel.getCardsList().get(0);
        this.viewModel.decrSelectedCard(cardItem);

        assertThat(this.viewModel.getCardsList(),
                hasItems(
                        isCardListItem(66848, 2)
                )
        );
        assertThat(this.viewModel.getCardsList(), hasSize(1));


        assertThat(this.viewModel.getCurveManaData().toArray(new XYChart.Data[0]),
                isManaCurve(new int[]{0,0,0,0,0,0,0,2}));

        assertThat(this.viewModel.getStatsTagsList(),
                hasItems(
                        isTagStats("Cri de guerre", 2),
                        isTagStats("Héro", 2)
                )
        );
        assertThat(this.viewModel.getStatsTagsList(), hasSize(2));

        assertThat(this.viewModel.currentDeckItem,
                isDeckListItem(2, 2, 2, 2));
    }


    @Test
    @DataSet("collectionWithDecks1.xml")
    @ExpectedDataSet(value = "expectedRemoveCard.xml", ignoreCols = "id")
    //@ExportDataSet(format = DataSetFormat.XML,outputName="target/exported/xml/result.xml")
    public void testRemoveCard(){
        DeckListItem deckItem = this.dbFacade.getDeckListItem(2);
        this.viewModel.showDeck(deckItem);
        this.viewModel.refreshCardListTab();

        DeckCardListItem cardItem = this.viewModel.getCardsList().get(1);
        this.viewModel.deleteSelectedCard(cardItem);

        assertThat(this.viewModel.getCardsList(),
                hasItems(
                        isCardListItem(66880, 1)
                )
        );

        assertThat(this.viewModel.getCurveManaData().toArray(new XYChart.Data[0]),
                isManaCurve(new int[]{0,0,0,0,1,0,0,0}));

        assertThat(this.viewModel.getStatsTagsList(),
                hasItems(
                        isTagStats("Cri de guerre", 1),
                        isTagStats("Serviteur", 1)
                )
        );

        assertThat(this.viewModel.currentDeckItem,
                isDeckListItem(2, 1, 1, 1));
    }

}
