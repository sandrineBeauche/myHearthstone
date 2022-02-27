package com.sbm4j.hearthstone.myhearthstone.services.imports;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.hamcrest.Matchers.*;


import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class DeckStringTests {

    public static <T> org.hamcrest.Matcher<T> isCard(int dbfId, int count) {
        return allOf(
                hasProperty("dbfId", equalTo(dbfId)),
                hasProperty("count", equalTo(count))
        );
    }

    @Test
    public void testDecode() throws Exception {
        String deckstring = "AAECAZICAortA7uKBA7XvgPevgO50gOM5AO57AOI9APJ9QP09gOB9wOsgASHnwTWoAThpASXpQQA";
        Deckstrings.DeckStringDeck result = Deckstrings.decode(deckstring);
        assertNotNull(result);
        assertThat(result.format, equalTo(Deckstrings.FT_STANDARD));
        assertThat(result.cards,
                hasItems(
                        isCard(57182, 2),
                        isCard(65580, 2),
                        isCard(59705, 2),
                        isCard(64372, 2),
                        isCard(70241, 2),
                        isCard(70295, 2),
                        isCard(57175, 2),
                        isCard(64385, 2),
                        isCard(69718, 2),
                        isCard(63033, 2),
                        isCard(63114, 1),
                        isCard(69511, 2),
                        isCard(64008, 2),
                        isCard(64201, 2),
                        isCard(66875, 1),
                        isCard(61964, 2)
                )
        );
    }

    @Test
    public void testEncode(){
        Deckstrings.DeckStringDeck deck = new Deckstrings.DeckStringDeck();
        deck.format = Deckstrings.FT_STANDARD;
        deck.heroes = List.of(274);
        deck.cards = List.of(
                new Deckstrings.DeckStringCard(57182,2),
                new Deckstrings.DeckStringCard(65580, 2),
                new Deckstrings.DeckStringCard(59705, 2),
                new Deckstrings.DeckStringCard(64372, 2),
                new Deckstrings.DeckStringCard(70241, 2),
                new Deckstrings.DeckStringCard(70295, 2),
                new Deckstrings.DeckStringCard(57175, 2),
                new Deckstrings.DeckStringCard(64385, 2),
                new Deckstrings.DeckStringCard(69718, 2),
                new Deckstrings.DeckStringCard(63033, 2),
                new Deckstrings.DeckStringCard(63114, 1),
                new Deckstrings.DeckStringCard(69511, 2),
                new Deckstrings.DeckStringCard(64008, 2),
                new Deckstrings.DeckStringCard(64201, 2),
                new Deckstrings.DeckStringCard(66875, 1),
                new Deckstrings.DeckStringCard(61964, 2)
        );

        String result = Deckstrings.encode(deck);
        assertEquals("AAECAZICAortA7uKBA7XvgPevgO50gOM5AO57AOI9APJ9QP09gOB9wOsgASHnwTWoAThpASXpQQA", result);
    }
}
