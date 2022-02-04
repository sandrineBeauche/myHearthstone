package com.sbm4j.hearthstone.myhearthstone.services.notifications;

import com.sbm4j.hearthstone.myhearthstone.utils.NotificationsUtil;

public class NotificatorImpl implements Notificator{

    protected String consultLog(){
        return "Veuillez consulter les logs pour plus de précisions.";
    }

    @Override
    public void notifyAddCardToDeckSuccess(String cardName, String deckName) {
        NotificationsUtil.showInfoNotification("Ajout d'une carte", "",
                "La carte " + cardName + " a été ajouté avec succès au deck " + deckName);
    }

    @Override
    public void notifyAddCardToDeckError(int dbfId, String deckName) {
        NotificationsUtil.showErrorNotification("Ajout d'une carte", "Erreur",
                "La carte " + dbfId + " n'a pu être ajoutée au deck " + deckName +
                        ". " + this.consultLog());
    }

    @Override
    public void notifyRemoveCardFromDeckSuccess(String cardName, String deckName) {
        NotificationsUtil.showInfoNotification("Suppression d'une carte", "",
                "La carte " + cardName + " a été supprimée avec succès du deck " + deckName);
    }

    @Override
    public void notifyRemoveCardFromDeckError(String cardName, String deckName) {
        NotificationsUtil.showErrorNotification("Suppression d'une carte", "Erreur",
                "La carte " + cardName + " n'a pu être supprimée du deck " + deckName +
                        ". " + this.consultLog());
    }


}
