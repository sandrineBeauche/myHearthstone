package com.sbm4j.hearthstone.myhearthstone.services.download;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.sbm4j.hearthstone.myhearthstone.HearthstoneModule;
import com.sbm4j.hearthstone.myhearthstone.model.BattleAccount;

import static org.junit.jupiter.api.Assertions.assertNotNull;


public class DownloadManagerTests {


    //@Test
    public void downloadTest(){
        Injector injector = Guice.createInjector(new HearthstoneModule());
        DownloadManager downloadManager = injector.getInstance(DownloadManager.class);
        BattleAccount account = new BattleAccount();
        account.setEmail("sandrine.beauche@gmail.com");
        account.setPassword("battleKendapoa16");
        account.setBattleTag("SandrineB#2427");
        account.setAccount_lo("41268540");
        Boolean connected = downloadManager.connectToHSReplay(account);
        if(connected) {
            String json2 = downloadManager.downloadCollectionFile(account);
            assertNotNull(json2);
        }
    }



}
