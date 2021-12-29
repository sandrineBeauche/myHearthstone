package com.sbm4j.hearthstone.myhearthstone.services.imports;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.inject.Inject;
import com.sbm4j.hearthstone.myhearthstone.model.*;
import com.sbm4j.hearthstone.myhearthstone.model.json.JsonCard;
import com.sbm4j.hearthstone.myhearthstone.services.config.ConfigManager;
import com.sbm4j.hearthstone.myhearthstone.services.db.DBFacade;
import com.sbm4j.hearthstone.myhearthstone.services.db.DBManager;
import com.sbm4j.hearthstone.myhearthstone.services.db.DBManagerImpl;
import com.sbm4j.hearthstone.myhearthstone.services.images.CardImageManager;

import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.controlsfx.dialog.ProgressDialog;
import org.hibernate.Session;
import org.apache.commons.codec.digest.DigestUtils;

import javax.persistence.NoResultException;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class JSONCardImporter extends Task<ImportCardReport> implements ImportCatalogAction {

    protected static Logger logger = LogManager.getLogger();

    @Inject
    protected DBManager dbManager;

    @Inject
    protected CardImageManager imageManager;

    @Inject
    protected ConfigManager configManager;

    @Inject
    protected DBFacade dbFacade;

    protected ImportCardReport report = new ImportCardReport();


    public JSONCardImporter(){

    }


    @Override
    protected ImportCardReport call() throws Exception {
        File jsonFile = this.configManager.getCatalogJsonFile();
        if(jsonFile != null){
            HashSet<String> unknownTags = this.verifyTags(jsonFile);

            this.importCards(jsonFile);
        }
        return null;
    }

    @Override
    public void handle(ActionEvent event) {
        try {
            ProgressDialog dialog = new ProgressDialog(this);
            dialog.setTitle("Importer les données Hearthstone");
            dialog.setHeaderText("Importation des cartes hearthstone");
            dialog.setWidth(600);

            new Thread(this).start();
            dialog.showAndWait();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public ArrayList<JsonCard> parseCards(File jsonFile) throws IOException {
        FileReader reader = new FileReader(jsonFile);
        return this.parseCards(reader, true);
    }

    public ArrayList<JsonCard> parseCards(Reader jsonReader, boolean cutJson) throws IOException {
        ArrayList<JsonCard> result = null;
        Gson gson = new Gson();

        if(cutJson){
            result = new ArrayList<JsonCard>();
            char c = (char) jsonReader.read();
            StringBuilder builder = null;
            boolean inString = false;

            while(c != '\uFFFF'){
                if(builder != null){
                    builder.append(c);
                    if(c == '"'){
                        inString = !inString;
                    }
                    if(c == '}' && !inString){
                        String json = builder.toString();
                        logger.info("parse from Json: " + json);
                        JsonCard card = gson.fromJson(json, JsonCard.class);
                        card.setJsonDesc(json);
                        result.add(card);
                        builder = null;
                    }
                }
                else{
                    if(c == '{'){
                        builder = new StringBuilder();
                        builder.append(c);
                    }
                }
                c = (char) jsonReader.read();
            }
        }
        else{
            Type cardCollectionType = new TypeToken<ArrayList<JsonCard>>(){}.getType();
            result = gson.fromJson(jsonReader, cardCollectionType);
        }

        return result;
    }

    public HashSet<String> verifyTags(File data) throws IOException {
        HashSet<String> unknownTags = new HashSet<String>();

        this.updateMessage("Vérification des tags");
        ArrayList<JsonCard> cards = this.parseCards(data);

        for(JsonCard current: cards){
            this.verifyTag(current.getSpellSchool(), unknownTags);
            this.verifyTag(current.getType(), unknownTags);
            this.verifyTag(current.getRace(), unknownTags);
            this.verifyListOfTags(current.getMechanics(), unknownTags);
            this.verifyListOfTags(current.getReferencedTags(), unknownTags);
        }

        if(unknownTags.size() > 0) {
            logger.warn("list of the unknown tags: " + unknownTags.toString());
        }
        else{
            logger.info("All the tags are valids");
        }
        return unknownTags;
    }

    protected void verifyListOfTags(ArrayList<String> tags, HashSet<String> unknownTags){
        if(tags != null){
            for(String current: tags){
                this.verifyTag(current, unknownTags);
            }
        }
    }


    protected void verifyTag(String tag, HashSet<String> unknownTags){
        if(tag != null){
            try{this.dbFacade.getTag(tag);}
            catch(NoResultException ex){
                unknownTags.add(tag);
                logger.warn("Unknown tag " + tag);
            }
        }
    }





    public void importCards(File jsonFile) throws IOException {
        ArrayList<JsonCard> jsonCards = this.parseCards(jsonFile);

        long nbCards = jsonCards.size();
        long numStep = 0;

        for(JsonCard current: jsonCards){
            switch (this.cardDetailStatus(current)){
                case NEW_CARD -> {
                    this.addCardDetail(current);
                }
                case MODIFIED_CARD -> {
                    this.updateCardDetail(current);
                }
                case UPTODATE_CARD -> {
                    this.report.incrUpToDate();
                    continue;
                }
            }
            numStep++;
            this.updateProgress(numStep, nbCards);
        }

        this.dbManager.closeSession();
    }

    protected void setCardDetailsValues(JsonCard json, CardDetail card){
        card.setDbfId(json.getDbfId());
        card.setName(json.getName());
        card.setId(json.getId());
        card.setText(json.getText());
        card.setCost(json.getCost());
        card.setDurability(json.getDurability());
        card.setAttack(json.getAttack());
        card.setArtist(json.getArtist());
        card.setCollectible(json.getCollectible());
        card.setFlavor(json.getFlavor());
        card.setElite(json.getElite());
        card.setHealth(json.getHealth());
        card.setHowToEarn(json.getHowToEarn());
        card.setHowToEarnGolden(json.getHowToEarnGolden());
        card.setQuestReward(json.getQuestReward());

        String md5 = DigestUtils.md5Hex(json.getJsonDesc());
        card.setJsonDesc(md5);

        Rarity rarity = this.dbFacade.getRarity(json.getRarity());
        card.setRarity(rarity);

        CardSet cardSet = this.dbFacade.getSet(json.getSet());
        card.setCardSet(cardSet);

        card.getCardClass().clear();
        String classe = json.getCardClass();
        if(classe == null) {
            for (String current : json.getClasses()) {
                CardClass cl = this.dbFacade.getClasse(current);
                card.getCardClass().add(cl);
            }
        }
        else{
            CardClass cl = this.dbFacade.getClasse(classe);
            card.getCardClass().add(cl);
        }

        card.getTags().clear();
        this.addTagsToCard(json.getMechanics(), card);
        this.addTagsToCard(json.getReferencedTags(), card);

        String spellSchool = json.getSpellSchool();
        if(spellSchool != null){
            card.getTags().add(this.dbFacade.getTag(spellSchool));
        }

        String race = json.getRace();
        if(race != null){
            card.getTags().add(this.dbFacade.getTag(race));
        }

        String cardType = json.getType();
        if(cardType != null){
            card.getTags().add(this.dbFacade.getTag(cardType));
        }
    }

    protected void addTagsToCard(ArrayList<String> tags, CardDetail card){
        if(tags !=null) {
            for (String current : tags) {
                CardTag tag = this.dbFacade.getTag(current);
                card.getTags().add(tag);
            }
        }
    }


    public void addCardDetail(JsonCard jsonCard){
        this.updateMessage("Ajoute une nouvelle carte: " + jsonCard.getName()
                + " (" + jsonCard.getDbfId() + ":" + jsonCard.getId() + ")");
        try {
            CardDetail result = new CardDetail();
            this.setCardDetailsValues(jsonCard, result);
            Session session = this.dbManager.getSession();
            session.beginTransaction();
            session.save(result);
            session.getTransaction().commit();
            this.report.incrCreated();

            this.imageManager.downloadCardImages(jsonCard.getId());
        }
        catch(Exception ex){
            this.report.addError(jsonCard.getDbfId(), ex.getMessage());
        }
    }

    public void updateCardDetail(JsonCard jsonCard){
        this.updateMessage("Mets à jour une carte: " + jsonCard.getName()
                + " (" + jsonCard.getDbfId() + ":" + jsonCard.getId() + ")");
        try {
            Session session = this.dbManager.getSession();
            CardDetail card = session.get(CardDetail.class, jsonCard.getDbfId());
            this.setCardDetailsValues(jsonCard, card);
            session.beginTransaction();
            session.update(card);
            session.getTransaction().commit();

            this.report.incrUpdated();

            String id = jsonCard.getId();
            this.imageManager.deleteImagesFromCard(id);
            this.imageManager.downloadCardImages(id);
        }
        catch(Exception ex){
            this.report.addError(jsonCard.getDbfId(), ex.getMessage());
        }
    }


    public CardStatus cardDetailStatus(JsonCard jsonCard){
        int dbfId = jsonCard.getDbfId();
        Session session = this.dbManager.getSession();
        List<String> result = session.createNamedQuery("json_from_card_details", String.class)
                .setParameter("dbfId", dbfId)
                .getResultList();

        if(result.size() == 0){
            return CardStatus.NEW_CARD;
        }
        else{
            String md5 = DigestUtils.md5Hex(jsonCard.getJsonDesc());
            if(result.get(0).equals(md5)){
                return CardStatus.UPTODATE_CARD;
            }
            else{
                return CardStatus.MODIFIED_CARD;
            }
        }
    }


    public ImportCardReport getReport() {
        return report;
    }

    public void setReport(ImportCardReport report) {
        this.report = report;
    }
}