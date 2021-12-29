package com.sbm4j.hearthstone.myhearthstone.services.images;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.sbm4j.hearthstone.myhearthstone.HearthstoneModuleTesting;
import javafx.scene.image.Image;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.io.FileNotFoundException;

public class CardImageManagerTests {

    @TempDir
    protected File tempDir;

    protected CardImageManager imageManager;

    @BeforeEach
    public void beforeEach(){
        Injector injector = Guice.createInjector(
                new HearthstoneModuleTesting(this.tempDir));
        this.imageManager = injector.getInstance(CardImageManager.class);
    }

    @Test
    public void downloadImageCardTest(){
        String cardId = "AT_001";
        this.imageManager.downloadCardImages(cardId);

        File fBig = this.imageManager.getBigCardImageAsFile(cardId);
        assert(fBig.exists());

        File fSmall = this.imageManager.getSmallCardImageAsFile(cardId);
        assert(fSmall.exists());

        File fTile = this.imageManager.getTileCardImageAsFile(cardId);
        assert(fTile.exists());

        File fThumb = this.imageManager.getThumbnailCardImageAsFile(cardId);
        assert(fThumb.exists());

        this.imageManager.deleteImagesFromCard(cardId);
        assert(!fBig.exists());
        assert(!fSmall.exists());
        assert(!fTile.exists());
        assert(!fThumb.exists());
    }


    @Test
    public void downloadUnknownImageTest() throws FileNotFoundException {
        String cardId = "blabla";
        this.imageManager.downloadCardImages(cardId);

        File fBig = this.imageManager.getBigCardImageAsFile(cardId);
        assert(!fBig.exists());

        File fSmall = this.imageManager.getSmallCardImageAsFile(cardId);
        assert(!fSmall.exists());

        File fTile = this.imageManager.getTileCardImageAsFile(cardId);
        assert(!fTile.exists());

        File fThumb = this.imageManager.getThumbnailCardImageAsFile(cardId);
        assert(!fThumb.exists());

        Image alt = this.imageManager.getBigCardImage(cardId, true);

        assert(alt != null);
    }
}
