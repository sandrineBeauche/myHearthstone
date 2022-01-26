package com.sbm4j.hearthstone.myhearthstone;

import com.google.inject.Provides;
import com.google.inject.Scopes;
import com.sbm4j.hearthstone.myhearthstone.services.config.ConfigManager;
import com.sbm4j.hearthstone.myhearthstone.services.config.ConfigManagerTesting;
import com.sbm4j.hearthstone.myhearthstone.services.images.CardImageManager;
import com.sbm4j.hearthstone.myhearthstone.services.images.CardImageManagerImpl;
import com.sbm4j.hearthstone.myhearthstone.services.images.ImageManager;
import com.sbm4j.hearthstone.myhearthstone.services.images.ImageManagerTesting;
import com.sbm4j.hearthstone.myhearthstone.services.imports.ImportCatalogAction;
import com.sbm4j.hearthstone.myhearthstone.services.imports.ImportCollectionAction;
import com.sbm4j.hearthstone.myhearthstone.services.imports.JSonCardImporterTesting;
import com.sbm4j.hearthstone.myhearthstone.services.imports.JsonCollectionImporterTesting;

import java.io.File;

public class HearthstoneModuleDB2Testing extends HearthstoneModule {

    protected File rootFile;

    protected ConfigManagerTesting config;

    public HearthstoneModuleDB2Testing(File rootFile){
        this.rootFile = rootFile;
    }


    @Override
    protected void bindConfig() {}

    @Provides
    protected ConfigManager providesConfigManager(){
        if(this.config == null){
            this.config = new ConfigManagerTesting(this.rootFile);
        }
        return this.config;
    }


    @Override
    protected void bindImages() {
        bind(ImageManager.class).to(ImageManagerTesting.class).in(Scopes.SINGLETON);
        bind(CardImageManager.class).to(CardImageManagerImpl.class).in(Scopes.SINGLETON);
    }
}
