package com.sbm4j.hearthstone.myhearthstone.services.images;

import javafx.scene.image.Image;

import java.io.FileNotFoundException;
import java.net.MalformedURLException;

public interface ImageManager {


    Image getCardSetIcon(String code) throws MalformedURLException, FileNotFoundException;

    Image getIconImage(Class<?> clazz, String code) throws FileNotFoundException;
}
