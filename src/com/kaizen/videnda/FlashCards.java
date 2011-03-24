package com.ahri.flashcards;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class FlashCards extends Activity
{
        /** Called when the activity is first created. */
        @Override
        public void onCreate(Bundle savedInstanceState)
        {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.card);

                String state = Environment.getExternalStorageState();

                if (!Environment.MEDIA_MOUNTED.equals(state) || Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
                        // Need read/write so pop up error
                }

                File path = new File(Environment.getExternalStorageDirectory(), "FlashCards");

                // Make sure the directory exists.
                path.mkdirs();
                if (!path.isDirectory()) {
                        // pop up error
                }

                //-----------------------------

                ArrayList<Deck> decks = this.getDecks(path);

                if (decks.size() > 0) {
                        CardDisplay dd = new CardDisplay(this.getApplicationContext(),
                                                         (ImageView) this.findViewById(R.id.card),
                                                         (LinearLayout) this.findViewById(R.id.card_row),
                                                         (LinearLayout) this.findViewById(R.id.button_row),
                                                         decks.get(0),
                                                         3);
                        dd.random();
                }
        }

        public ArrayList<Deck> getDecks(File path)
        {
                ArrayList<Deck> decks = new ArrayList<Deck>();

                String[] files = path.list();
                File f;
                for (int i = 0; i < files.length; i++) {
                        f = new File(path, files[i]);
                        try {
                                Deck d = DeckFactory.provide(f);
                                decks.add(d);
                        } catch (FileNotFoundException e) {
                                // do nothing
                                Log.i("deck_create_fail", f.getAbsolutePath());
                        } catch (NullPointerException e) {
                                // do nothing
                                Log.i("deck_null", f.getAbsolutePath());
                        }
                }

                return decks;
        }
}
