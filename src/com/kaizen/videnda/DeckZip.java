package com.kaizen.videnda;

import java.io.File;
import java.io.IOException;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class DeckZip extends Deck {
        public DeckZip(File path) throws IOException
        {
                super(path);
                this.name = this.name.replaceFirst("\\.zip$", "");
        }

        @Override
        protected void populateCards(File path) throws IOException
        {
                ZipFile z = new ZipFile(path);
                Enumeration<? extends ZipEntry> e = z.entries();
                while (e.hasMoreElements()) {
                        ZipEntry ze = e.nextElement();
                        this.cards.add(new CardZip(z, ze));
                }
        }
}
