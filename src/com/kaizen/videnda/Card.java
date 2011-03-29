package com.kaizen.videnda;

import java.io.IOException;
import java.io.InputStream;

public abstract class Card
{
        public String answer;

        protected void setAnswerFromFilename(String name)
        {
                this.answer = name.replaceFirst("\\.[^\\.]+$", "");
        }

        public abstract InputStream getInputStream() throws IOException;
}
