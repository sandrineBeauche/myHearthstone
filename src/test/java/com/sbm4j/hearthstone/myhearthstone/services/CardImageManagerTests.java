package com.sbm4j.hearthstone.myhearthstone.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;

public class CardImageManagerTests {

    @TempDir
    protected File tempDir;

    @Test
    public void downloadImageCardTest(){
        CardImageManager manager = new CardImageManager(this.tempDir);
        manager.downloadCardImages("AT_001");

        File fBig = manager.getBigCardImage("AT_001");
        assert(fBig.exists());

        File fSmall = manager.getSmallCardImage("AT_001");
        assert(fSmall.exists());

        File fTile = manager.getTileCardImage("AT_001");
        assert(fTile.exists());

        manager.deleteImagesFromCard("AT_001");
        assert(!fBig.exists());
        assert(!fSmall.exists());
        assert(!fTile.exists());
    }
}
