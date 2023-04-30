package com.sbm4j.hearthstone.myhearthstone.viewmodel;

import com.google.inject.Inject;
import com.sbm4j.hearthstone.myhearthstone.model.*;
import com.sbm4j.hearthstone.myhearthstone.services.db.DBFacade;
import com.sbm4j.hearthstone.myhearthstone.services.db.DBManager;
import com.sbm4j.hearthstone.myhearthstone.services.images.CardImageManager;
import com.sbm4j.hearthstone.myhearthstone.services.images.ImageManager;
import com.sbm4j.hearthstone.myhearthstone.services.images.cardClasses.CardClassImageLoader;
import com.sbm4j.hearthstone.myhearthstone.views.ParamCommand;
import de.saxsys.mvvmfx.ViewModel;
import de.saxsys.mvvmfx.utils.commands.Action;
import de.saxsys.mvvmfx.utils.mapping.ModelWrapper;
import de.saxsys.mvvmfx.utils.mapping.accessorfunctions.ObjectPropertyAccessor;
import de.saxsys.mvvmfx.utils.notifications.NotificationCenter;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.text.Text;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;

import java.io.FileNotFoundException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class CardDetailsViewModel implements ViewModel, Initializable {

    @Inject
    protected CardImageManager cardImageManager;

    @Inject
    protected ImageManager imageManager;

    @Inject
    protected DBFacade dbFacade;

    @Inject
    protected DBManager dbManager;

    @Inject
    private NotificationCenter notificationCenter;

    public static final String NEW_TAG_MESSAGE = "NEW_TAG";

    public static final String DELETE_TAG_MESSAGE = "DELETE_TAG";

    public final static String SHOW_CARD = "SHOW_CARD";

    public final static String BACK = "BACK";

    protected Boolean [] refreshed;
    public Boolean [] getRefreshed(){ return this.refreshed;}

    /* title property */
    private StringProperty title = new SimpleStringProperty();
    public StringProperty getTitleProperty(){return this.title;}
    public String getTitle(){return this.title.get();}
    public void setTitle(String value){this.title.set(value);}

    protected ModelWrapper<CardDetail> modelWrapper = new ModelWrapper<>();

    protected StringProperty cardNameProperty = modelWrapper.field(CardDetail::getName, CardDetail::setName);
    public StringProperty getCardNameProperty(){return this.cardNameProperty;}

    protected BooleanProperty eliteProperty = modelWrapper.field(CardDetail::getElite, CardDetail::setElite);
    public BooleanProperty getEliteProperty(){ return this.eliteProperty;}

    /* standard property */
    private BooleanProperty standard = new SimpleBooleanProperty();
    public BooleanProperty getStandardProperty(){return this.standard;}
    public Boolean getStandard(){return this.standard.get();}
    public void setStandard(Boolean value){this.standard.set(value);}

    /* cardImage property */
    private ObjectProperty<Image> cardImage = new SimpleObjectProperty<Image>();
    public ObjectProperty<Image> getCardImageProperty(){return this.cardImage;}
    public Image getCardImage(){return this.cardImage.get();}
    public void setCardImage(Image value){this.cardImage.set(value);}

    /* extensionImage property */
    private ObjectProperty<Image> extensionImage = new SimpleObjectProperty<Image>();
    public ObjectProperty<Image> getExtensionImageProperty(){return this.extensionImage;}
    public Image getExtensionImage(){return this.extensionImage.get();}
    public void setExtensionImage(Image value){this.extensionImage.set(value);}

    /* cardClassImage property */
    private ObjectProperty<Image> cardClassImage = new SimpleObjectProperty<Image>();
    public ObjectProperty<Image> getCardClassImageProperty(){return this.cardClassImage;}
    public Image getCardClassImage(){return this.cardClassImage.get();}
    public void setCardClassImage(Image value){this.cardClassImage.set(value);}
    
    /* infos property */
    private ObjectProperty<ObservableList<CardDetailData>> infos = new SimpleObjectProperty<ObservableList<CardDetailData>>();
    public ObjectProperty<ObservableList<CardDetailData>> getInfosProperty(){return this.infos;}
    public ObservableList<CardDetailData> getInfos(){return this.infos.get();}
    public void setInfos(ObservableList<CardDetailData> value){this.infos.set(value);}

    /* nbCards property */
    private StringProperty nbCards = new SimpleStringProperty();
    public StringProperty getNbCardsProperty(){return this.nbCards;}
    public String getNbCards(){return this.nbCards.get();}
    public void setNbCards(String value){this.nbCards.set(value);}


    /* availableTags property */
    private ObjectProperty<ObservableList<CardTag>> availableTags = new SimpleObjectProperty<ObservableList<CardTag>>();
    public ObjectProperty<ObservableList<CardTag>> getAvailableTagsProperty(){return this.availableTags;}
    public ObservableList<CardTag> getAvailableTags(){return this.availableTags.get();}
    public void setAvailableTags(ObservableList<CardTag> value){this.availableTags.set(value);}

    /* associatedTags property */
    private ObjectProperty<ObservableList<CardTag>> associatedTags = new SimpleObjectProperty<ObservableList<CardTag>>();
    public ObjectProperty<ObservableList<CardTag>> getAssociatedTagsProperty(){return this.associatedTags;}
    public ObservableList<CardTag> getAssociatedTags(){return this.associatedTags.get();}
    public void setAssociatedTags(ObservableList<CardTag> value){this.associatedTags.set(value);}

    /* notes property */
    private StringProperty notes = new SimpleStringProperty();
    public StringProperty getNotesProperty(){return this.notes;}
    public String getNotes(){return this.notes.get();}
    public void setNotes(String value){this.notes.set(value);}


    protected List<CardTag> availableTagsDB;

    protected Logger logger = LogManager.getLogger();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.setInfos(FXCollections.observableArrayList());
        this.setAvailableTags(FXCollections.observableArrayList());
        this.setAssociatedTags(FXCollections.observableArrayList());

        this.availableTagsDB = this.dbFacade.getAvailableUserTags();

        this.refreshed = new Boolean[]{false, false, false, false};

        this.notificationCenter.subscribe(SHOW_CARD, (key, payload) -> {
            Object[] params = payload.clone();
            int cardId = (int) params[0];
            try {
                showCard(cardId);
            } catch (FileNotFoundException e) {
                logger.error(e.getMessage(), e);
            }
        });
    }

    public void showCard(int dbfId) throws FileNotFoundException{
        CardDetail card = this.dbFacade.getCardFromDbfId(dbfId);
        if(card != null) {
            this.showCard(card);
        }
    }

    public void showCard(CardDetail card) throws FileNotFoundException {
        this.modelWrapper.set(card);
        this.setTitle("Card " + card.getName());

        this.refreshed[0] = false;
        this.refreshed[1] = false;
        this.refreshed[2] = false;
        this.refreshed[3] = false;

        this.publish(SHOW_CARD, getTitle());
    }

    public void refreshInformationsTab(){
        if(!this.refreshed[0]){
            this.refreshed[0] = true;
            CardDetail card = this.modelWrapper.get();
            this.setCardImage(this.cardImageManager.getBigCardImage(card.getId(), false));

            try {
                this.setExtensionImage(this.imageManager.getCardSetLogo(card.getCardSet().getCode()));
            } catch (FileNotFoundException e) {
                logger.error(e.getMessage(), e);
            }

            this.setCardClassImage(CardClassImageLoader.getImage(card.getCardClass().get(0).getCode()));
            this.setStandard(card.getCardSet().isStandard());
            this.setNbCards("X " + card.getUserData().getNbTotalCards());

            this.getInfos().clear();
            this.getInfos().add(new CardDetailData("Artiste", card.getArtist()));
            this.getInfos().add(new CardDetailData("Flavor", card.getFlavor()));

            String text = card.getText();
            if(text != null && text != "") {
                this.getInfos().add(new CardDetailData("Texte", text));
            }

            String howToEarn = card.getHowToEarn();
            if(howToEarn != null && howToEarn != ""){
                this.getInfos().add(new CardDetailData("Obtenir", howToEarn));
            }

            String howToEarnGolden = card.getHowToEarnGolden();
            if(howToEarnGolden != null && howToEarnGolden != null){
                this.getInfos().add(new CardDetailData("Obtenir dorée", howToEarnGolden));
            }

            String questReward = card.getQuestReward();
            if(questReward != null && questReward != ""){
                this.getInfos().add(new CardDetailData("Récompense quête", questReward));
            }
        }
    }

    public void refreshUserTagsTab(){
        if(!this.refreshed[1]){
            this.refreshed[1] = true;
            CardDetail card = this.modelWrapper.get();
            this.getAssociatedTags().clear();
            List<CardTag> tags = card.getUserData().getTags().stream().filter(cardTag -> cardTag.getUser()).toList();
            this.getAssociatedTags().addAll(tags);

            this.getAvailableTags().clear();
            this.getAvailableTags().addAll(this.availableTagsDB);
            for(CardTag current: tags){
                this.getAvailableTags().removeIf(cardTag -> cardTag.getName().equals(current.getName()));
            }
        }
    }

    public void refreshNotesTab(){
        if(!this.refreshed[2]){
            this.refreshed[2] = true;
            CardDetail card = this.modelWrapper.get();
            String notesValue = card.getUserData().getNotes();
            if(notesValue != null){
                this.setNotes(notesValue);
            }
        }
    }


    public ParamCommand getCreateNewUserTagCommand(){return this.createNewUserTagCommand;}
    protected ParamCommand createNewUserTagCommand = new ParamCommand("Créer un tag utilisateur", () -> new Action(){
        @Override
        protected void action() throws Exception {
            String name = (String) createNewUserTagCommand.getParameters().get("name");

            logger.info("Create a new user tag " + name);
            CardTag newTag = dbFacade.createUserTag(name);
            if(newTag != null) {
                getAvailableTags().add(newTag);
                notificationCenter.publish(NEW_TAG_MESSAGE, newTag);
                createNewUserTagCommand.setNotificationMessage("Le tag " + name + " crée avec succès");
            }
            else{
                createNewUserTagCommand.setNotificationMessage("Erreur lors de la création du tag " + name);
                throw new Exception();
            }
        }
    });

    public ParamCommand getDeleteUserTagCommand(){return this.deleteUserTagCommand;}
    protected ParamCommand deleteUserTagCommand = new ParamCommand("Supprimer un tag utilisateur", () -> new Action(){
        @Override
        protected void action() throws Exception {
            CardTag tag = (CardTag) deleteUserTagCommand.getParameters().get("tag");
            String tagName = tag.getName();

            logger.info("Delete the user tag " + tagName);
            boolean result = dbFacade.deleteUserTag(tag);
            if(result) {
                getAvailableTags().remove(tag);
                notificationCenter.publish(DELETE_TAG_MESSAGE, tag);
                deleteUserTagCommand.setNotificationMessage("Le tag " + tagName + " est supprimé avec succès");
            }
            else{
                createNewUserTagCommand.setNotificationMessage("Erreur lors de la suppression du tag " + tagName);
                throw new Exception();
            }
        }
    });


    public ParamCommand getAddUserTagCommand(){return this.addUserTagCommand;}
    protected ParamCommand addUserTagCommand = new ParamCommand("Ajouter un tag utilisateur", () -> new Action(){
        @Override
        protected void action() throws Exception {
            CardTag tag = (CardTag) addUserTagCommand.getParameters().get("tag");
            String tagName = tag.getName();
            CardDetail card = modelWrapper.get();
            String cardName = card.getName();

            logger.info("Add the user tag " + tagName + " to card " + cardName);
            boolean result = dbFacade.addUserTagToCard(tag, card);
            if(result) {
                getAvailableTags().remove(tag);
                getAssociatedTags().add(tag);
                addUserTagCommand.setNotificationMessage("Le tag " + tagName + " est ajouté avec succès à la carte " + cardName);
            }
            else{
                addUserTagCommand.setNotificationMessage("Erreur lors de l'ajout du tag " + tagName + " à la carte " + cardName);
                throw new Exception();
            }
        }
    });

    public ParamCommand getRemoveUserTagCommand(){return this.removeUserTagCommand;}
    protected ParamCommand removeUserTagCommand = new ParamCommand("Enlever un tag utilisateur", () -> new Action(){
        @Override
        protected void action() throws Exception {
            CardTag tag = (CardTag) addUserTagCommand.getParameters().get("tag");
            String tagName = tag.getName();
            CardDetail card = modelWrapper.get();
            String cardName = card.getName();

            logger.info("Remove the user tag " + tagName + " from card " + cardName);
            boolean result = dbFacade.removeUserTagFromCard(tag, card);
            if(result) {
                getAssociatedTags().remove(tag);
                getAvailableTags().add(tag);
                removeUserTagCommand.setNotificationMessage("Le tag " + tagName + " est enlevé avec succès de la carte " + cardName);
            }
            else{
                removeUserTagCommand.setNotificationMessage("Erreur lors de la suppression du tag " + tagName + " de la carte " + cardName);
                throw new Exception();
            }
        }
    });


    public ParamCommand getRemoveAllUserTagCommand(){return this.removeAllUserTagCommand;}
    protected ParamCommand removeAllUserTagCommand = new ParamCommand("Enlever tous les tags utilisateur", () -> new Action(){
        @Override
        protected void action() throws Exception {
            CardDetail card = modelWrapper.get();
            String cardName = card.getName();

            logger.info("Remove all the user tags from card " + cardName);
            List<CardTag> tags = card.getUserData().getTags().stream().filter(cardTag -> cardTag.getUser()).toList();
            boolean result = tags.stream().map(cardTag -> dbFacade.removeUserTagFromCard(cardTag, card))
                    .reduce(true, (aBoolean, aBoolean2) -> aBoolean && aBoolean2);
            if(result) {
                getAssociatedTags().removeAll(tags);
                getAvailableTags().addAll(tags);
                removeUserTagCommand.setNotificationMessage("Le tags sont enlevés avec succès de la carte " + cardName);
            }
            else{
                removeUserTagCommand.setNotificationMessage("Erreur lors de la suppression des tags utilisateur de la carte " + cardName);
                throw new Exception();
            }
        }
    });



    protected void saveCardNotes(){
        CardDetail card = modelWrapper.get();
        String cardName = card.getName();

        logger.info("Save notes for card " + cardName);
        card.getUserData().setNotes(getNotes());

        Session session = dbManager.getSession();
        session.beginTransaction();
        session.update(card);
        session.getTransaction().commit();
        dbManager.closeSession();
    }

    public void backCallback(){
        this.saveCardNotes();
        this.publish(BACK);
    }


    public class CardDetailData {

        protected String dataName;

        protected String dataValue;

        public CardDetailData(String dataName, String dataValue) {
            this.dataName = dataName;
            this.dataValue = dataValue;
        }

        public String getDataName() {
            return dataName;
        }

        public void setDataName(String dataName) {
            this.dataName = dataName;
        }

        public String getDataValue() {
            return dataValue;
        }

        public void setDataValue(String dataValue) {
            this.dataValue = dataValue;
        }
    }
}
