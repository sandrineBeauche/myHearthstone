package com.sbm4j.hearthstone.myhearthstone.views;

import com.google.inject.Injector;
import com.sbm4j.hearthstone.myhearthstone.AbstractUITest;
import com.sbm4j.hearthstone.myhearthstone.viewmodel.CardCatalogViewModel;
import de.saxsys.mvvmfx.FluentViewLoader;
import de.saxsys.mvvmfx.ViewTuple;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.testfx.osgi.service.TestFx;

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

    @Test
    public void firstTest() throws InterruptedException {
        CardCatalogItem item = new CardCatalogItem();
        item.setId("AT_001");
        item.setDbfId(1);
        item.setName("une carte");



        System.out.println("coucou");
    }
}
