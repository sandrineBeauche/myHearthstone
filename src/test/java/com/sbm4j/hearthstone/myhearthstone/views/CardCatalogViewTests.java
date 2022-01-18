package com.sbm4j.hearthstone.myhearthstone.views;

import com.google.inject.Injector;
import com.google.inject.Module;
import com.sbm4j.hearthstone.myhearthstone.AbstractUITest;
import com.sbm4j.hearthstone.myhearthstone.HearthstoneModuleDBTesting;
import com.sbm4j.hearthstone.myhearthstone.HearthstoneModuleUITesting;
import com.sbm4j.hearthstone.myhearthstone.model.CardCatalogItem;
import com.sbm4j.hearthstone.myhearthstone.viewmodel.CardCatalogViewModel;
import de.saxsys.mvvmfx.FluentViewLoader;
import de.saxsys.mvvmfx.ViewTuple;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.testfx.osgi.service.TestFx;
import org.testfx.util.WaitForAsyncUtils;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@Disabled
public class CardCatalogViewTests extends AbstractUITest {

    protected CardCatalogViewModel viewModel;


    @Override
    public void startAppTest(Injector injector, Stage stage) {
        ViewTuple<CardCatalogView, CardCatalogViewModel> catalog = FluentViewLoader.fxmlView(CardCatalogView.class).load();
        this.viewModel = catalog.getViewModel();
        Scene root = new Scene(catalog.getView());
        stage.setScene(root);
        stage.show();
    }

    @Override
    public List<Module> initModules() {
        return List.of(new HearthstoneModuleUITesting());
    }


    @Test
    public final void showCardTest() throws TimeoutException {
        CardCatalogItem [] items = {
                new CardCatalogItem(1, "AT_001", "Lance-flamme", 2, 1)
        };

        this.viewModel.getCollection().clear();
        this.viewModel.getCollection().addAll(items);

        WaitForAsyncUtils.waitFor(1000, TimeUnit.MINUTES, () -> false);
    }

    @Test
    public final void showUnknownCardTest() throws TimeoutException {
        CardCatalogItem [] items = {
                new CardCatalogItem(1, "AT_001", "Lance-flamme", 2, 1),
                new CardCatalogItem(1, "BLABLA", "blabla", 2, 2)
        };

        this.viewModel.getCollection().clear();
        this.viewModel.getCollection().addAll(items);

        WaitForAsyncUtils.waitFor(1000, TimeUnit.MINUTES, () -> false);
    }

    @Test
    public final void showNotInCollectionCardTest() throws TimeoutException {
        CardCatalogItem [] items = {
                new CardCatalogItem(1, "AT_001", "Lance-flamme", 2, 1),
                new CardCatalogItem(1, "AT_002", "blabla", 0, 2)
        };

        this.viewModel.getCollection().clear();

        this.viewModel.getCollection().addAll(items);

        WaitForAsyncUtils.waitFor(1000, TimeUnit.MINUTES, () -> false);
    }
}
