package com.ahri.flashcards;

import java.io.File;
import java.io.FileNotFoundException;

public class DeckDirectory extends Deck {
        public DeckDirectory(File path) throws FileNotFoundException
        {
                super(path);
        }

        @Override
        protected void populateCards(File path) throws FileNotFoundException
        {
                File[] files = path.listFiles();
                if (files == null) {
                        return;
                }

                for (int i = 0; i < files.length; i++) {
                        Card c = new Card(files[i]);
                        this.cards.add(c);
                }
        }
}
