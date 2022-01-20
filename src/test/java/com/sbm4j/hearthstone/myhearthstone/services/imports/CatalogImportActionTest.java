package com.sbm4j.hearthstone.myhearthstone.services.imports;

import com.github.database.rider.core.api.connection.ConnectionHolder;
import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.junit5.DBUnitExtension;
import com.github.database.rider.junit5.util.EntityManagerProvider;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.sbm4j.hearthstone.myhearthstone.AbstractUITest;
import com.sbm4j.hearthstone.myhearthstone.HearthstoneModuleDB2Testing;
import com.sbm4j.hearthstone.myhearthstone.HearthstoneModuleDBTesting;
import com.sbm4j.hearthstone.myhearthstone.services.db.DBFacade;
import com.sbm4j.hearthstone.myhearthstone.services.db.DBManager;
import javafx.stage.Stage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;
import org.testfx.util.WaitForAsyncUtils;

import java.io.File;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@Disabled
@ExtendWith(DBUnitExtension.class)
public class CatalogImportActionTest extends AbstractUITest {

    protected DBManager manager;

    @TempDir
    protected File tempDir;

    protected Module hearthstoneModule;

    private ConnectionHolder connectionHolder = () ->
            EntityManagerProvider.instance("pu-hearthstone").connection();

    @BeforeEach
    public void beforeEach() throws TimeoutException {
        this.hearthstoneModule = new HearthstoneModuleDB2Testing(tempDir, false);
        Injector injector = Guice.createInjector(this.hearthstoneModule);
        this.manager = injector.getInstance(DBManager.class);
    }


    public void startAppTest(Injector injector, Stage stage){
        ImportCatalogAction action = injector.getInstance((ImportCatalogAction.class));
        action.handle(null);
    }

    @Override
    public List<Module> initModules() {
        return List.of(this.hearthstoneModule);
    }



    @Test
    @DataSet("initDBDataset.xml")
    public void testCatalogImportActionUI() throws TimeoutException {
        this.setupAppTest();
        WaitForAsyncUtils.waitFor(1000, TimeUnit.MINUTES, () -> false);
    }


    @Test
    @DataSet("initDBDatasetWithoutClasses.xml")
    public void testCatalogImportActionUIWithErrors() throws TimeoutException {
        this.setupAppTest();
        WaitForAsyncUtils.waitFor(1000, TimeUnit.MINUTES, () -> false);
    }


    @Test
    @DataSet("initDBDatasetWithoutClassesSets.xml")
    public void testCatalogImportActionUIWithErrors2() throws TimeoutException {
        this.setupAppTest();
        WaitForAsyncUtils.waitFor(1000, TimeUnit.MINUTES, () -> false);
    }
}
