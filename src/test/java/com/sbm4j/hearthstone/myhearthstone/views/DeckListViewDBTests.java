package com.sbm4j.hearthstone.myhearthstone.views;

import com.github.database.rider.core.api.connection.ConnectionHolder;
import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.junit5.DBUnitExtension;
import com.github.database.rider.junit5.util.EntityManagerProvider;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.sbm4j.hearthstone.myhearthstone.AbstractUITest;
import com.sbm4j.hearthstone.myhearthstone.HearthstoneModuleDBTesting;
import com.sbm4j.hearthstone.myhearthstone.services.db.DBFacade;
import com.sbm4j.hearthstone.myhearthstone.services.db.DBManager;
import com.sbm4j.hearthstone.myhearthstone.viewmodel.DeckListViewModel;
import de.saxsys.mvvmfx.FluentViewLoader;
import de.saxsys.mvvmfx.ViewTuple;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.util.WaitForAsyncUtils;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@ExtendWith(DBUnitExtension.class)
public class DeckListViewDBTests extends AbstractUITest {

    protected DBManager manager;

    protected DBFacade dbFacade;

    protected Module hearthstoneModule;

    protected DeckListViewModel viewModel;

    private ConnectionHolder connectionHolder = () ->
            EntityManagerProvider.instance("pu-hearthstone").connection();

    @BeforeEach
    public void beforeEach() throws TimeoutException {
        this.hearthstoneModule = new HearthstoneModuleDBTesting(null);
        Injector injector = Guice.createInjector(this.hearthstoneModule);
        this.manager = injector.getInstance(DBManager.class);
        this.dbFacade = injector.getInstance(DBFacade.class);
    }

    @Override
    public void startAppTest(Injector injector, Stage stage) {
        ViewTuple<DeckListView, DeckListViewModel> deckList = FluentViewLoader.fxmlView(DeckListView.class).load();
        this.viewModel = deckList.getViewModel();
        Scene root = new Scene(deckList.getView());
        stage.setScene(root);
        stage.show();
    }

    @Override
    public List<Module> initModules() {
        return List.of(this.hearthstoneModule);
    }

    @Test
    @DataSet("collectionWithDecks1.xml")
    public void duplicateDeckTest() throws TimeoutException {
        this.setupAppTest();
        WaitForAsyncUtils.waitFor(1000, TimeUnit.MINUTES, () -> false);
    }
}
