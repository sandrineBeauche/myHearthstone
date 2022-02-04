package com.sbm4j.hearthstone.myhearthstone.services.notifications;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class NotificatorTesting implements Notificator{

    protected static Logger logger = LogManager.getLogger();

    @Override
    public void notifyAddCardToDeckSuccess(String cardName, String deckName) {
        logger.info("Card " + cardName + " added with success to the deck " + deckName);
    }

    @Override
    public void notifyAddCardToDeckError(int dbfId, String deckName) {
        logger.error("Error when adding card " + dbfId + " to the deck " + deckName);
    }

    @Override
    public void notifyRemoveCardFromDeckSuccess(String cardName, String deckName) {
        logger.info("Card " + cardName + " removed with success from the deck " + deckName);
    }

    @Override
    public void notifyRemoveCardFromDeckError(String cardName, String deckName) {
        logger.info("Error when removing the card " + cardName + " from the deck " + deckName);
    }
}
