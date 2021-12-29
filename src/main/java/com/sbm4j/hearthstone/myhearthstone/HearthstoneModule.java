package com.sbm4j.hearthstone.myhearthstone;

import com.google.inject.AbstractModule;
import com.google.inject.Scopes;
import com.sbm4j.hearthstone.myhearthstone.services.config.ConfigManager;
import com.sbm4j.hearthstone.myhearthstone.services.config.ConfigManagerImpl;
import com.sbm4j.hearthstone.myhearthstone.services.db.DBFacade;
import com.sbm4j.hearthstone.myhearthstone.services.db.DBFacadeImpl;
import com.sbm4j.hearthstone.myhearthstone.services.db.DBManager;
import com.sbm4j.hearthstone.myhearthstone.services.db.DBManagerImpl;
import com.sbm4j.hearthstone.myhearthstone.services.download.DownloadManager;
import com.sbm4j.hearthstone.myhearthstone.services.download.DownloadManagerImpl;
import com.sbm4j.hearthstone.myhearthstone.services.images.CardImageManager;
import com.sbm4j.hearthstone.myhearthstone.services.images.CardImageManagerImpl;
import com.sbm4j.hearthstone.myhearthstone.services.imports.ImportCatalogAction;
import com.sbm4j.hearthstone.myhearthstone.services.imports.JSONCardImporter;


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
    }

    protected void bindDownload(){
        bind(DownloadManager.class).to(DownloadManagerImpl.class).in(Scopes.SINGLETON);
    }

    protected void bindImages(){
        bind(CardImageManager.class).to(CardImageManagerImpl.class).in(Scopes.SINGLETON);
    }

    protected void bindImports(){
        bind(ImportCatalogAction.class).to(JSONCardImporter.class).in(Scopes.SINGLETON);
    }
}
