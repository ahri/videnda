package com.kaizen.videnda;

import java.io.File;
import java.io.IOException;

public class DeckDirectory extends Deck
{
        public DeckDirectory(File path) throws IOException
        {
                super(path);
        }

        @Override
        protected void populateCards(File path) throws IOException
        {
                File[] files = path.listFiles();
                if (files == null) {
                        return;
                }

                for (int i = 0; i < files.length; i++) {
                        this.cards.add(new CardFile(files[i]));
                }
        }
}
