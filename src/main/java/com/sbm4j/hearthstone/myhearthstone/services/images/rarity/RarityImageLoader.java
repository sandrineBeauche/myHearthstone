package com.sbm4j.hearthstone.myhearthstone.services.images.rarity;

import com.sbm4j.hearthstone.myhearthstone.services.images.mana.ManaImageLoader;
import javafx.scene.image.Image;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.InputStream;
import java.util.HashMap;

public class RarityImageLoader {

    protected static final Logger logger = LogManager.getLogger();

    protected static HashMap<String, Image> rarityCache = new HashMap<String, Image>();

    public static Image getImage(String code){
        if(rarityCache.containsKey(code)){
            return rarityCache.get(code);
        }
        else {
            String name = code + ".png";
            InputStream in = RarityImageLoader.class.getResourceAsStream(name);
            if (in != null) {
                Image result = new Image(in);
                rarityCache.put(code, result);
                return result;
            } else {
                logger.warn("Image resource file not found: " + name);
                return null;
            }
        }
    }
}
