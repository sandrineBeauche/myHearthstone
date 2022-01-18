package com.sbm4j.hearthstone.myhearthstone.services.imports;

import com.google.inject.Injector;
import com.google.inject.Module;
import com.sbm4j.hearthstone.myhearthstone.AbstractUITest;
import com.sbm4j.hearthstone.myhearthstone.HearthstoneModuleDBTesting;
import javafx.stage.Stage;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.util.List;


public class CatalogImportActionTest extends AbstractUITest {

    @TempDir
    protected File tempDir;

    public void startAppTest(Injector injector, Stage stage){
        ImportCatalogAction action = injector.getInstance((ImportCatalogAction.class));
        action.handle(null);
    }

    @Override
    public List<Module> initModules() {
        return List.of(new HearthstoneModuleDBTesting(tempDir, true));
    }


    @Test
    @Disabled
    public void testCatalogImportActionUI(){
        System.out.println("coucou");
    }
}
