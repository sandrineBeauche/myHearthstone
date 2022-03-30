package com.sbm4j.hearthstone.myhearthstone.views;

import com.google.inject.Injector;
import com.google.inject.Module;
import com.sbm4j.hearthstone.myhearthstone.AbstractUITest;
import com.sbm4j.hearthstone.myhearthstone.HearthstoneModuleUITesting;
import com.sbm4j.hearthstone.myhearthstone.model.DeckListItem;
import com.sbm4j.hearthstone.myhearthstone.viewmodel.DeckListViewModel;
import de.saxsys.mvvmfx.FluentViewLoader;
import de.saxsys.mvvmfx.ViewTuple;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.testfx.util.WaitForAsyncUtils;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@Disabled
public class DeckListViewTest extends AbstractUITest {

    protected DeckListViewModel viewModel;


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
        return List.of(new HearthstoneModuleUITesting());
    }

    @Test
    public void showDeckTests() throws TimeoutException {
        DeckListItem [] items = {
                new DeckListItem(1, "deck1", "un résumé", "JAINA_PORTVAILLANT",
                        25L, 15L, 10L, "tag1,tag2"),
                new DeckListItem(2, "deck2", "un autre résumé", "THRALL",
                        30L, 30L, 30L, "tag3,tag4")
        };

        this.viewModel.getItems().clear();
        this.viewModel.getItems().addAll(items);

        WaitForAsyncUtils.waitFor(1000, TimeUnit.MINUTES, () -> false);
    }
}
