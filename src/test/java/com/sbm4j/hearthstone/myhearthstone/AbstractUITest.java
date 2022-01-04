package com.sbm4j.hearthstone.myhearthstone;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Module;
import de.saxsys.mvvmfx.guice.MvvmfxGuiceApplication;
import javafx.stage.Stage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.io.TempDir;
import org.testfx.api.FxRobot;
import org.testfx.api.FxToolkit;

import java.io.File;
import java.util.List;
import java.util.concurrent.TimeoutException;

public abstract class AbstractUITest extends FxRobot {

    @TempDir
    protected File tempDir;


    public class  ActionApp extends MvvmfxGuiceApplication {

        @Inject
        protected Injector injector;

        public ActionApp(){}

        @Override
        public void initGuiceModules(List<Module> modules) throws Exception {
            HearthstoneModuleTesting module = new HearthstoneModuleTesting(tempDir, true);
            modules.add(module);
        }

        @Override
        public void startMvvmfx(Stage stage) throws Exception {
            startAppTest(this.injector, stage);
        }
    }

    @BeforeEach
    public void beforeEach() throws TimeoutException {
        FxToolkit.registerPrimaryStage();
        ActionApp app = new ActionApp();
        FxToolkit.toolkitContext().setSetupTimeoutInMillis(Long.MAX_VALUE);
        FxToolkit.setupApplication(() -> app);
    }

    public abstract void startAppTest(Injector injector, Stage stage);


}
