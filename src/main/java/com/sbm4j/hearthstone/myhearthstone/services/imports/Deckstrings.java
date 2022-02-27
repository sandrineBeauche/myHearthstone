package com.sbm4j.hearthstone.myhearthstone.services.imports;

import java.nio.ByteBuffer;
import java.util.*;

public class Deckstrings {
    public static int FT_WILD = 1;
    public static int FT_STANDARD = 2;

    private static int VERSION = 1;

    public static class DeckStringDeck {
        public List<Integer> heroes;
        public Integer format;
        public List<DeckStringCard> cards;
    }

    public static class DeckStringCard implements Comparable<DeckStringCard>{
        protected int dbfId;
        protected int count; // number of times this card appears in the deck

        public DeckStringCard(int dbfId_, int count_) {
            this.dbfId = dbfId_;
            this.count = count_;
        }

        public int getDbfId() {
            return dbfId;
        }

        public void setDbfId(int dbfId) {
            this.dbfId = dbfId;
        }

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }

        @Override
        public int compareTo(DeckStringCard o) {
            return this.dbfId - o.dbfId;
        }
    }

    public static class ParseException extends Exception {
        ParseException(String message) {
            super(message);
        }
    }

    public static DeckStringDeck decode(String encodedString) throws Exception {
        byte[] decodedBytes = Base64.getDecoder().decode(encodedString);
        return decode(decodedBytes);
    }

    /**
     *
     * @param data the base64 decoded data. This method intentionnaly does not
     *             decode Base64 as implementation differ greatly between android and
     *             other JVM. See https://github.com/auth0/java-jwt/issues/131 for more
     *             details
     * @return
     * @throws Exception
     */
    public static DeckStringDeck decode(byte[] data) throws Exception {
        DeckStringDeck result = new DeckStringDeck();

        ByteBuffer byteBuffer = ByteBuffer.wrap(data);

        byte reserved = byteBuffer.get();
        int version = VarInt.getVarInt(byteBuffer);
        if (version != VERSION) {
            throw new ParseException("bad version: " + version);
        }

        result.format = VarInt.getVarInt(byteBuffer);
        if (result.format != FT_STANDARD && result.format != FT_WILD) {
            throw new ParseException("bad format: " + result.format);
        }

        int heroCount = VarInt.getVarInt(byteBuffer);
        result.heroes = new ArrayList<>();
        for (int i = 0; i < heroCount; i++) {
            result.heroes.add(VarInt.getVarInt(byteBuffer));
        }

        result.cards = new ArrayList<>();
        for (int i = 1; i <= 3; i++) {
            int c = VarInt.getVarInt(byteBuffer);
            for (int j = 0; j < c; j++) {
                int dbfId = VarInt.getVarInt(byteBuffer);
                int count;
                if (i == 3) {
                    count = VarInt.getVarInt(byteBuffer);
                } else {
                    count = i;
                }
                result.cards.add(new DeckStringCard(dbfId, count));
            }
        }

        Collections.sort(result.cards, new Comparator<DeckStringCard>() {
            @Override
            public int compare(DeckStringCard o1, DeckStringCard o2) {
                return o2.dbfId - o1.dbfId;
            }
        });

        return result;
    }


    public static String encode(DeckStringDeck deck){
        ByteBuffer buffer = ByteBuffer.allocate(120);
        buffer.put((byte) 0x00);
        VarInt.putVarInt(VERSION, buffer);
        VarInt.putVarInt(deck.format, buffer);
        VarInt.putVarInt(deck.heroes.size(), buffer);
        for(Integer currentHero: deck.heroes){
            VarInt.putVarInt(currentHero, buffer);
        }

        List<DeckStringCard> cards1 = deck.cards.stream()
                .filter(c -> c.count == 1)
                .sorted().toList();
        VarInt.putVarInt(cards1.size(), buffer);
        for(DeckStringCard current: cards1){
            VarInt.putVarInt(current.dbfId, buffer);
        }

        List<DeckStringCard> cards2 = deck.cards.stream()
                .filter(c -> c.count == 2)
                .sorted().toList();
        VarInt.putVarInt(cards2.size(), buffer);
        for(DeckStringCard current: cards2){
            VarInt.putVarInt(current.dbfId, buffer);
        }

        List<DeckStringCard> cardsN = deck.cards.stream()
                .filter(c -> c.count > 2)
                .sorted().toList();
        VarInt.putVarInt(cardsN.size(), buffer);
        for(DeckStringCard current: cardsN){
            VarInt.putVarInt(current.dbfId, buffer);
        }

        byte[] bytes = new byte[buffer.position()];
        byte[] bufferBytes = buffer.array();
        //buffer.get(bytes, 0, buffer.position() - 1);
        for(int i = 0; i < buffer.position(); i++){
            bytes[i] = bufferBytes[i];
        }
        return Base64.getEncoder().encodeToString(bytes);
    }
}

