package com.sbm4j.hearthstone.myhearthstone.services.notifications;

public interface Notificator {

    void notifyAddCardToDeckSuccess(String cardName, String deckName);

    void notifyAddCardToDeckError(int dbfId, String deckName);

    void notifyRemoveCardFromDeckSuccess(String cardName, String deckName);

    void notifyRemoveCardFromDeckError(String cardName, String deckName);
}
