package com.sbm4j.hearthstone.myhearthstone.services.images;

import javafx.scene.image.Image;

import java.io.File;
import java.io.FileNotFoundException;

public interface CardImageManager {
    void downloadCardImages(String cardId);

    Image getBigCardImage(String cardId, boolean alternate);

    Image getSmallCardImage(String cardId, boolean alternate);

    Image getTileCardImage(String cardId, boolean alternate);

    Image getThumbnailCardImage(String cardId, boolean alternate);

    File getBigCardImageAsFile(String cardId);

    File getSmallCardImageAsFile(String cardId);

    File getTileCardImageAsFile(String cardId);

    File getThumbnailCardImageAsFile(String cardId);

    void deleteImagesFromCard(String cardId);

    void deleteAllCards();

    void clearThumbsCache();

    void clearBigsCache();

}
