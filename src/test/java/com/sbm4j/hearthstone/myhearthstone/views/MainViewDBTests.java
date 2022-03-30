package com.sbm4j.hearthstone.myhearthstone.views;

import com.github.database.rider.core.api.connection.ConnectionHolder;
import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.core.api.dataset.DataSetFormat;
import com.github.database.rider.core.api.exporter.ExportDataSet;
import com.github.database.rider.junit5.DBUnitExtension;
import com.github.database.rider.junit5.util.EntityManagerProvider;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.sbm4j.hearthstone.myhearthstone.AbstractUITest;
import com.sbm4j.hearthstone.myhearthstone.HearthstoneModuleDBTesting;
import com.sbm4j.hearthstone.myhearthstone.services.db.DBFacade;
import com.sbm4j.hearthstone.myhearthstone.services.db.DBManager;
import com.sbm4j.hearthstone.myhearthstone.viewmodel.MainViewModel;
import de.saxsys.mvvmfx.FluentViewLoader;
import de.saxsys.mvvmfx.ViewTuple;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.util.WaitForAsyncUtils;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@Disabled
@ExtendWith(DBUnitExtension.class)
public class MainViewDBTests extends AbstractUITest {

    protected DBManager manager;

    protected DBFacade dbFacade;

    protected Module hearthstoneModule;

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
        ViewTuple<MainView, MainViewModel> main = FluentViewLoader.fxmlView(MainView.class).load();
        Scene root = new Scene(main.getView());
        stage.setScene(root);
        stage.show();
    }

    @Override
    public List<Module> initModules() {
        return List.of(this.hearthstoneModule);
    }


    @Test
    @DataSet("collectionWithDecks1.xml")
    //@ExportDataSet(format = DataSetFormat.XML,outputName="target/exported/xml/allTables.xml")
    public void mainTest() throws TimeoutException {
        this.setupAppTest();
        WaitForAsyncUtils.waitFor(1000, TimeUnit.MINUTES, () -> false);
    }

    @Test
    @DataSet("bigDataset.xml")
    public void bigTest() throws TimeoutException {
        this.setupAppTest();
        WaitForAsyncUtils.waitFor(1000, TimeUnit.MINUTES, () -> false);
    }
}
