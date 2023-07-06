package com.sbm4j.hearthstone.myhearthstone.viewmodel;

import com.google.inject.Inject;
import com.sbm4j.hearthstone.myhearthstone.services.images.ImageManager;
import com.sbm4j.hearthstone.myhearthstone.services.images.cardClasses.CardClassImageLoader;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.image.Image;


import java.io.FileNotFoundException;

public class DeckEditGeneraltabViewModel extends AbstractDeckEditTabViewModel{

    /* name property */
    private StringProperty name = new SimpleStringProperty();
    public StringProperty getNameProperty(){return this.name;}
    public String getName(){return this.name.get();}
    public void setName(String value){this.name.set(value);}


    /* summary property */
    private StringProperty summary = new SimpleStringProperty();
    public StringProperty getSummaryProperty(){return this.summary;}
    public String getSummary(){return this.summary.get();}
    public void setSummary(String value){this.summary.set(value);}

    /* heroImg property */
    private ObjectProperty<Image> heroImg = new SimpleObjectProperty<Image>();
    public ObjectProperty<Image> getHeroImgProperty(){return this.heroImg;}
    public Image getHeroImg(){return this.heroImg.get();}
    public void setHeroImg(Image value){this.heroImg.set(value);}

    /* deckClassIcon property */
    private ObjectProperty<Image> deckClassIcon = new SimpleObjectProperty<Image>();
    public ObjectProperty<Image> getDeckClassIconProperty(){return this.deckClassIcon;}
    public Image getDeckClassIcon(){return this.deckClassIcon.get();}
    public void setDeckClassIcon(Image value){this.deckClassIcon.set(value);}


    public DeckEditGeneraltabViewModel(DeckEditViewModel mainViewModel){
        super(mainViewModel);
    }



    public void refresh() {
        if(!this.refreshed) {
            this.refreshed = true;
            this.setName(currentDeck.getName());
            this.setSummary(currentDeck.getSummary());

            String heroCode = currentDeck.getHero().getCode();
            try {
                Image heroImg = this.mainViewModel.getImageManager().getHeroPortrait(heroCode);
                this.setHeroImg(heroImg);
                Image classeImg = CardClassImageLoader.getImage(currentDeck.getHero().getClasse().getCode());
                this.setDeckClassIcon(classeImg);
            } catch (FileNotFoundException e) {
                logger.error("Image file not found for hero " + heroCode);
            }
        }
    }

    public boolean saveDeck(){
        boolean isDirty = false;
        if(!this.getName().equals(currentDeckItem.getName())){
            currentDeck.setName(this.getName());
            isDirty = true;
        }

        if(this.getSummary() == null){
            if(currentDeckItem.getSummary() != this.getSummary()){
                currentDeck.setSummary(this.getSummary());
                isDirty = true;
            }
        }
        else{
            if(!this.getSummary().equals(currentDeckItem.getSummary())){
                currentDeck.setSummary(this.getSummary());
                isDirty = true;
            }
        }
        return isDirty;
    }
}
