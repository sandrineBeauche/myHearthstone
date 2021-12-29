package com.sbm4j.hearthstone.myhearthstone.services.imports;

import com.google.inject.Injector;
import com.sbm4j.hearthstone.myhearthstone.AbstractUITest;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;


public class CatalogImportActionTest extends AbstractUITest {

    public void startAppTest(Injector injector){
        ImportCatalogAction action = injector.getInstance((ImportCatalogAction.class));
        action.handle(null);
    }


    @Test
    @Disabled
    public void testCatalogImportActionUI(){
        System.out.println("coucou");
    }
}
