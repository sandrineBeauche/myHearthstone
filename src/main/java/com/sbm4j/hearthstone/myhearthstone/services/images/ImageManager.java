package com.sbm4j.hearthstone.myhearthstone.services.images;

import javafx.scene.image.Image;

import java.io.FileNotFoundException;
import java.net.MalformedURLException;

public interface ImageManager {

    Image getCardClassImage(String code);

    Image getCardSetIcon(String code) throws MalformedURLException, FileNotFoundException;

    Image getRarityIcon(String code);

    Image getManaIcon(String code);

    Image getIconImage(Class<?> clazz, String code) throws FileNotFoundException;
}
