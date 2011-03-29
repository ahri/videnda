package com.kaizen.videnda;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class CardFile extends Card
{
        protected File file = null;

        public CardFile(File file)
        {
                this.file = file;
                this.setAnswerFromFilename(file.getName());
        }

        @Override
        public InputStream getInputStream() throws IOException
        {
                return new FileInputStream(this.file);
        }
}
