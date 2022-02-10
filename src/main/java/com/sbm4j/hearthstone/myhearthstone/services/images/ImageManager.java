package com.sbm4j.hearthstone.myhearthstone.services.images;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.FileNotFoundException;
import java.net.MalformedURLException;

public interface ImageManager {


    Image getCardSetIcon(String code) throws MalformedURLException, FileNotFoundException;

    Image getCardSetLogo(String code) throws FileNotFoundException;

    Image getHeroIcon(String code) throws FileNotFoundException;

    Image getHeroPortrait(String code) throws FileNotFoundException;

    Image getIconImage(Class<?> clazz, String code) throws FileNotFoundException;

    ImageView getImageViewFromResource(Class ownerClazz, String resourceName);
}
