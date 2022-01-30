package com.sbm4j.hearthstone.myhearthstone.services.images.cardClasses;

import com.sbm4j.hearthstone.myhearthstone.model.CardClass;
import javafx.scene.image.Image;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.InputStream;
import java.util.HashMap;

public class CardClassImageLoader {

    protected static final Logger logger = LogManager.getLogger();

    protected static HashMap<String, Image> cardClassCache = new HashMap<String, Image>();

    public static Image getImage(String code){
        if(cardClassCache.containsKey(code)){
            return cardClassCache.get(code);
        }
        else {
            String name = code + ".png";
            InputStream in = CardClassImageLoader.class.getResourceAsStream(name);
            if (in != null) {
                Image result = new Image(in);
                cardClassCache.put(code, result);
                return result;
            } else {
                logger.warn("Image resource file not found: " + name);
                return null;
            }
        }
    }
}
