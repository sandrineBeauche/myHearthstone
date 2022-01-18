package com.sbm4j.hearthstone.myhearthstone.services.db;

import com.sbm4j.hearthstone.myhearthstone.model.CardClass;
import com.sbm4j.hearthstone.myhearthstone.model.CardSet;
import com.sbm4j.hearthstone.myhearthstone.model.CardTag;
import com.sbm4j.hearthstone.myhearthstone.model.Rarity;
import com.sbm4j.hearthstone.myhearthstone.views.ManaOption;

import java.util.List;

public record CatalogCriteria(CardClass cardClass,
                              CardSet cardSet,
                              Rarity rarity,
                              ManaOption mana,
                              boolean inCollection,
                              List<CardTag> tags) {
}
