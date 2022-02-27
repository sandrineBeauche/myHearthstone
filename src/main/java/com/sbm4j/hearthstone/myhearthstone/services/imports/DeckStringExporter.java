package com.sbm4j.hearthstone.myhearthstone.services.imports;

import com.sbm4j.hearthstone.myhearthstone.model.DeckListItem;

public interface DeckStringExporter {

    String export(DeckListItem deck) throws Exception;
}
