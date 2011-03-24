package com.ahri.flashcards;

import java.io.File;
import java.io.FileNotFoundException;

public class DeckFactory
{
        public static Deck provide(File path) throws FileNotFoundException
        {
                if (path.isDirectory()) {
                        return new DeckDirectory(path);
                }

                return null;
        }
}
