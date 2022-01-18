package com.sbm4j.hearthstone.myhearthstone.services.images;

import javafx.scene.image.Image;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;


public class ImageManagerTesting extends ImageManagerImpl{

    protected String resourcesPath = System.getProperty("user.dir") + File.separator + "build" +
            File.separator + "resources" + File.separator + "main" + File.separator;

    @Override
    protected Image getResourceImage(String name) {
        String resourcePath = this.resourcesPath + name;
        File f = new File(resourcePath);
        try {
            return new Image(new FileInputStream(f));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
}
