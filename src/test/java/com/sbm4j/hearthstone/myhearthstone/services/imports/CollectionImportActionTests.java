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
import com.sbm4j.hearthstone.myhearthstone.services.db.DBManager;
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
public class CollectionImportActionTests extends AbstractUITest {

    protected DBManager manager;

    protected Module hearthstoneModule;

    private ConnectionHolder connectionHolder = () ->
            EntityManagerProvider.instance("pu-hearthstone").connection();

    @BeforeEach
    public void beforeEach() throws TimeoutException {
        this.hearthstoneModule = new HearthstoneModuleDB2Testing(null);
        Injector injector = Guice.createInjector(this.hearthstoneModule);
        this.manager = injector.getInstance(DBManager.class);
    }

    public void startAppTest(Injector injector, Stage stage){
        ImportCollectionAction action = injector.getInstance((ImportCollectionAction.class));
        action.handle(null);
    }

    @Override
    public List<Module> initModules() {
        return List.of(this.hearthstoneModule);
    }



    @Test
    @DataSet("importedCatalogDataset.xml")
    public void testCatalogImportActionUI() throws TimeoutException {
        this.setupAppTest();
        WaitForAsyncUtils.waitFor(1000, TimeUnit.MINUTES, () -> false);
    }
}
