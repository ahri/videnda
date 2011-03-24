package com.ahri.flashcards;

import java.io.File;
import java.io.FileNotFoundException;

public class Card
{
        protected File file;
        protected String answer;

        public Card(File file) throws FileNotFoundException
        {
                if(!file.exists()) {
                        throw new FileNotFoundException();
                }

                this.file = file;
                this.answer = file.getName().replaceFirst("\\.[^\\.]+$", "");
        }
}
