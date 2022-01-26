package com.sbm4j.hearthstone.myhearthstone;

import com.google.inject.AbstractModule;
import com.google.inject.Scopes;
import com.sbm4j.hearthstone.myhearthstone.services.config.ConfigManager;
import com.sbm4j.hearthstone.myhearthstone.services.config.ConfigManagerImpl;
import com.sbm4j.hearthstone.myhearthstone.services.db.*;
import com.sbm4j.hearthstone.myhearthstone.services.download.DownloadManager;
import com.sbm4j.hearthstone.myhearthstone.services.download.DownloadManagerImpl;
import com.sbm4j.hearthstone.myhearthstone.services.images.CardImageManager;
import com.sbm4j.hearthstone.myhearthstone.services.images.CardImageManagerImpl;
import com.sbm4j.hearthstone.myhearthstone.services.images.ImageManager;
import com.sbm4j.hearthstone.myhearthstone.services.images.ImageManagerImpl;
import com.sbm4j.hearthstone.myhearthstone.services.imports.ImportCatalogAction;
import com.sbm4j.hearthstone.myhearthstone.services.imports.ImportCollectionAction;
import com.sbm4j.hearthstone.myhearthstone.services.imports.JSONCardImporter;
import com.sbm4j.hearthstone.myhearthstone.services.imports.JSONCollectionImporter;


public class HearthstoneModule extends AbstractModule{
    @Override
    protected void configure() {
        this.bindConfig();
        this.bindDB();
        this.bindDownload();
        this.bindImages();
        this.bindImports();
    }

    protected void bindConfig(){
        bind(ConfigManager.class).to(ConfigManagerImpl.class).in(Scopes.SINGLETON);
    }

    protected void bindDB(){
        bind(DBManager.class).to(DBManagerImpl.class).in(Scopes.SINGLETON);
        bind(DBFacade.class).to(DBFacadeImpl.class).in(Scopes.SINGLETON);
        bind(DBInitializer.class).to(DBInitializerImpl.class);
    }

    protected void bindDownload(){
        bind(DownloadManager.class).to(DownloadManagerImpl.class).in(Scopes.SINGLETON);
    }

    protected void bindImages(){
        bind(CardImageManager.class).to(CardImageManagerImpl.class).in(Scopes.SINGLETON);
        bind(ImageManager.class).to(ImageManagerImpl.class).in(Scopes.SINGLETON);
    }

    protected void bindImports(){
        bind(ImportCatalogAction.class).to(JSONCardImporter.class);
        bind(ImportCollectionAction.class).to(JSONCollectionImporter.class);
    }
}
