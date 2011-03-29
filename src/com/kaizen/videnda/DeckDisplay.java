package com.kaizen.videnda;

import java.io.File;
import java.io.IOException;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class DeckDisplay extends Activity {

        public void onCreate(Bundle savedInstanceState)
        {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.card);
                Bundle extras = getIntent().getExtras();
                Deck deck = null;
                try {
                        deck = DeckFactory.provide((File) extras.get("path"));
                } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                }

                CardDisplay dd = new CardDisplay(this.getApplicationContext(),
                                                 (ImageView) this.findViewById(R.id.card),
                                                 (LinearLayout) this.findViewById(R.id.card_row),
                                                 (LinearLayout) this.findViewById(R.id.button_row),
                                                 deck,
                                                 3);
				dd.randomCard();
        }
}
