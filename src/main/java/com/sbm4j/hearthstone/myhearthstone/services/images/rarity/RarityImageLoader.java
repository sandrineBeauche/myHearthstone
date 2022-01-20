package com.sbm4j.hearthstone.myhearthstone.services.images.rarity;

import com.sbm4j.hearthstone.myhearthstone.services.images.mana.ManaImageLoader;
import javafx.scene.image.Image;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.InputStream;

public class RarityImageLoader {

    protected static final Logger logger = LogManager.getLogger();

    public static Image getImage(String code){
        String name = code + ".png";
        InputStream in = RarityImageLoader.class.getResourceAsStream(name);
        if(in != null) {
            return new Image(in);
        }
        else{
            logger.warn("Image resource file not found: " + name);
            return null;
        }
    }
}
