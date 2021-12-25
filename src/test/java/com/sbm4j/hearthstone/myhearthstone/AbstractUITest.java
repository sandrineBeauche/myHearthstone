package com.sbm4j.hearthstone.myhearthstone;

import de.saxsys.mvvmfx.guice.MvvmfxGuiceApplication;
import javafx.stage.Stage;
import org.junit.jupiter.api.BeforeEach;
import org.testfx.api.FxToolkit;

import java.util.concurrent.TimeoutException;

public abstract class AbstractUITest {

    public class  ActionApp extends MvvmfxGuiceApplication {

        public ActionApp(){}

        @Override
        public void startMvvmfx(Stage stage) throws Exception {
            startAppTest();
        }
    }

    @BeforeEach
    public void beforeEach() throws TimeoutException {
        FxToolkit.registerPrimaryStage();
        ActionApp app = new ActionApp();
        FxToolkit.setupApplication(() -> app);
    }

    public abstract void startAppTest();
}
