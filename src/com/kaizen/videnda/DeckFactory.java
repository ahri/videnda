package com.kaizen.videnda;

import java.io.File;
import java.io.IOException;

public class DeckFactory
{
        public static Deck provide(File path) throws IOException
        {
                if (path.isDirectory()) {
                        return new DeckDirectory(path);
                } else if (path.getAbsolutePath().endsWith(".zip")) {
                        return new DeckZip(path);
                }

                return null;
        }
}
