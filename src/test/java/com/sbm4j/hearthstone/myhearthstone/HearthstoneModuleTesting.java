package com.sbm4j.hearthstone.myhearthstone;

import com.google.inject.Provides;
import com.google.inject.Scopes;
import com.sbm4j.hearthstone.myhearthstone.services.config.ConfigManager;
import com.sbm4j.hearthstone.myhearthstone.services.config.ConfigManagerTesting;
import com.sbm4j.hearthstone.myhearthstone.services.imports.ImportCatalogAction;
import com.sbm4j.hearthstone.myhearthstone.services.imports.ImportCollectionAction;
import com.sbm4j.hearthstone.myhearthstone.services.imports.JSonCardImporterTesting;
import com.sbm4j.hearthstone.myhearthstone.services.imports.JsonCollectionImporterTesting;

import java.io.File;

public class HearthstoneModuleTesting extends HearthstoneModule {

    protected File rootFile;

    protected boolean initDB = false;

    protected ConfigManagerTesting config;

    public HearthstoneModuleTesting(File rootFile){
        this.rootFile = rootFile;
    }

    public HearthstoneModuleTesting(File rootFile, boolean initDB){
        this.rootFile = rootFile;
        this.initDB = initDB;
    }

    @Override
    protected void bindConfig() {}

    @Provides
    protected ConfigManager providesConfigManager(){
        if(this.config == null){
            this.config = new ConfigManagerTesting(this.rootFile, this.initDB);
        }
        return this.config;
    }

    @Override
    protected void bindImports() {
        bind(ImportCatalogAction.class).to(JSonCardImporterTesting.class).in(Scopes.SINGLETON);
        bind(ImportCollectionAction.class).to(JsonCollectionImporterTesting.class).in(Scopes.SINGLETON);
    }
}
