package com.sbm4j.hearthstone.myhearthstone.services.imports;

import com.sbm4j.hearthstone.myhearthstone.model.DeckListItem;

public interface DeckStringImporter {

    DeckListItem importDeckString(String deckstring, String deckName) throws Exception;
}
