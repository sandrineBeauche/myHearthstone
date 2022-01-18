package com.sbm4j.hearthstone.myhearthstone.services.images;

import com.google.inject.Inject;
import com.sbm4j.hearthstone.myhearthstone.model.CardClass;
import com.sbm4j.hearthstone.myhearthstone.model.CardSet;
import com.sbm4j.hearthstone.myhearthstone.model.Rarity;
import com.sbm4j.hearthstone.myhearthstone.services.config.ConfigManager;
import com.sbm4j.hearthstone.myhearthstone.views.ManaOption;
import javafx.scene.image.Image;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.MalformedURLException;

public class ImageManagerImpl implements ImageManager{

    @Inject
    protected ConfigManager configManager;


    protected Image getResourceImage(String name) {
        InputStream in = ImageManagerImpl.class.getClassLoader().getResourceAsStream(name);
        if(in != null) {
            return new Image(in);
        }
        else{
            return null;
        }
    }

    @Override
    public Image getCardClassImage(String code) {
        String name = "cardClasses/" + code + ".png";
        return this.getResourceImage(name);
    }

    @Override
    public Image getCardSetIcon(String code) throws FileNotFoundException {
        String name = this.configManager.getImageDirectory().getAbsolutePath()
                + File.separator + "extensions" + File.separator + "icons"
                + File.separator + code + ".png";
        File f = new File(name);
        if(f.exists()){
            return new Image(new FileInputStream(f));
        }
        else{
            return null;
        }
    }

    @Override
    public Image getRarityIcon(String code) {
        String name = "rarity/" + code + ".png";
        return this.getResourceImage(name);
    }

    @Override
    public Image getManaIcon(String code) {
        String name = "mana/" + code + ".png";
        return this.getResourceImage(name);
    }


    @Override
    public Image getIconImage(Class<?> clazz, String code) throws FileNotFoundException {
        if(clazz == CardClass.class){
            return getCardClassImage(code);
        }
        if(clazz == CardSet.class){
            return getCardSetIcon(code);
        }
        if(clazz == Rarity.class){
            return getRarityIcon(code);
        }
        if(clazz == ManaOption.class){
            return getManaIcon(code);
        }
        return null;
    }


}
