package com.sbm4j.hearthstone.myhearthstone.views;

import com.google.inject.Injector;
import com.google.inject.Module;
import com.sbm4j.hearthstone.myhearthstone.AbstractUITest;
import com.sbm4j.hearthstone.myhearthstone.model.Hero;
import javafx.stage.Stage;
import javafx.util.Pair;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.testfx.util.WaitForAsyncUtils;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@Disabled
public class DialogTests extends AbstractUITest {

    protected Optional<Pair<String, Hero>> result;

    @Override
    public void startAppTest(Injector injector, Stage stage) {
        List<Hero> heroes = List.of(new Hero("Héro 1", "HERO 1", null),
                                    new Hero("Héro 2", "HERO_2", null),
                                    new Hero("Héro 3", "HERO_3", null));
        this.result = Dialogs.newDeckDialog(heroes, null);
        System.out.println("done");
    }

    @Override
    public List<Module> initModules() {
        return List.of();
    }

    @Test
    public void execTest() throws TimeoutException {
        WaitForAsyncUtils.waitFor(1000, TimeUnit.MINUTES, () -> false);
    }
}
