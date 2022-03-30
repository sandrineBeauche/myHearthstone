package com.sbm4j.hearthstone.myhearthstone;

import com.google.inject.Module;
import com.sbm4j.hearthstone.myhearthstone.viewmodel.MainViewModel;
import com.sbm4j.hearthstone.myhearthstone.views.MainView;
import de.saxsys.mvvmfx.FluentViewLoader;
import de.saxsys.mvvmfx.ViewTuple;
import de.saxsys.mvvmfx.guice.MvvmfxGuiceApplication;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.List;


public class HearthstoneApplication extends MvvmfxGuiceApplication{


    public static void main(String[] args) {
        launch();
    }

    @Override
    public void initGuiceModules(List<Module> modules) throws Exception {
        modules.add(new HearthstoneModule());
    }

    @Override
    public void initMvvmfx() throws Exception {
        super.initMvvmfx();
    }

    @Override
    public void startMvvmfx(Stage stage) throws Exception {
        ViewTuple<MainView, MainViewModel> main = FluentViewLoader.fxmlView(MainView.class).load();
        Scene root = new Scene(main.getView());
        stage.setScene(root);
        stage.show();
    }

    @Override
    public void stopMvvmfx() throws Exception {
        super.stopMvvmfx();
    }


}