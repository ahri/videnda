package com.kaizen.videnda;

import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class CardZip extends Card
{
        protected ZipEntry entry;
        protected ZipFile file;

        public CardZip(ZipFile file, ZipEntry entry)
        {
                this.file = file;
                this.entry = entry;
                this.setAnswerFromFilename(entry.getName());
        }

        @Override
        public InputStream getInputStream() throws IOException {
                return this.file.getInputStream(this.entry);
        }
}
