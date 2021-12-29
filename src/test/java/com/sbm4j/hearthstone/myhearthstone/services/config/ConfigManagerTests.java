package com.sbm4j.hearthstone.myhearthstone.services.config;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.sbm4j.hearthstone.myhearthstone.HearthstoneModule;
import org.junit.jupiter.api.Test;

public class ConfigManagerTests {

    @Test
    public void testConfig(){
        Injector injector = Guice.createInjector(new HearthstoneModule());
        ConfigManager config = injector.getInstance(ConfigManager.class);
    }
}
