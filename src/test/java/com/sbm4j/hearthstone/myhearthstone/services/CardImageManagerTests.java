package com.sbm4j.hearthstone.myhearthstone.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;

public class CardImageManagerTests {

    @TempDir
    protected File tempDir;

    protected CardImageManager imageManager;

    @BeforeEach
    public void beforeEach(){
        this.imageManager = new CardImageManager(this.tempDir);
    }

    @Test
    public void downloadImageCardTest(){
        String cardId = "AT_001";
        this.imageManager.downloadCardImages(cardId);

        File fBig = this.imageManager.getBigCardImage(cardId, false);
        assert(fBig.exists());

        File fSmall = this.imageManager.getSmallCardImage(cardId, false);
        assert(fSmall.exists());

        File fTile = this.imageManager.getTileCardImage(cardId, false);
        assert(fTile.exists());

        File fThumb = this.imageManager.getThumbnailCardImage(cardId, false);
        assert(fThumb.exists());

        this.imageManager.deleteImagesFromCard(cardId);
        assert(!fBig.exists());
        assert(!fSmall.exists());
        assert(!fTile.exists());
        assert(!fThumb.exists());
    }


    @Test
    public void downloadUnknownImageTest(){
        String cardId = "blabla";
        this.imageManager.downloadCardImages(cardId);

        File fBig = this.imageManager.getBigCardImage(cardId, false);
        assert(!fBig.exists());

        File fSmall = this.imageManager.getSmallCardImage(cardId, false);
        assert(!fSmall.exists());

        File fTile = this.imageManager.getTileCardImage(cardId, false);
        assert(!fTile.exists());

        File fThumb = this.imageManager.getThumbnailCardImage(cardId, false);
        assert(!fThumb.exists());

        File alt = this.imageManager.getBigCardImage(cardId, true);
        assert(alt.getName().equals("backCardClassic.jpg"));


    }
}
