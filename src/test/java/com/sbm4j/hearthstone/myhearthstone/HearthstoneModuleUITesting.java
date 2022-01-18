package com.sbm4j.hearthstone.myhearthstone;


import com.google.inject.Scopes;
import com.sbm4j.hearthstone.myhearthstone.services.db.DBFacade;
import com.sbm4j.hearthstone.myhearthstone.services.db.DBFacadeTesting;
import com.sbm4j.hearthstone.myhearthstone.services.db.DBManager;
import com.sbm4j.hearthstone.myhearthstone.services.db.DBManagerImpl;

public class HearthstoneModuleUITesting extends HearthstoneModuleDBTesting{

    public HearthstoneModuleUITesting() {
        super(null);
    }

    @Override
    protected void bindDB() {
        bind(DBFacade.class).to(DBFacadeTesting.class).in(Scopes.SINGLETON);
        bind(DBManager.class).to(DBManagerImpl.class).in(Scopes.SINGLETON);
    }
}
