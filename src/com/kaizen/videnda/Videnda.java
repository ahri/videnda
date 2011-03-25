package com.kaizen.videnda;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Iterator;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.kaizen.util.Message;

public class Videnda extends Activity
{
        /** Called when the activity is first created. */
        @Override
        public void onCreate(Bundle savedInstanceState)
        {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.deckpick);

                String state = Environment.getExternalStorageState();

                if (!Environment.MEDIA_MOUNTED.equals(state) ||
                    Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
                                Message.popup(this, "SD Card must be mounted");
                                this.finish();
                                return;
                }

                File path = new File(Environment.getExternalStorageDirectory(),
                                     this.getApplicationContext().getString(R.string.app_name));

                // Make sure the directory exists.
                path.mkdirs();
                if (!path.isDirectory()) {
                        Message.popup(this, "Cannot write directory: " + path.getAbsolutePath());
                        this.finish();
                        return;
                }

                //-----------------------------

                ArrayList<Deck> decks = this.getDecks(path);
                Iterator<Deck> itr = decks.iterator();

                LinearLayout deck_list = (LinearLayout) this.findViewById(R.id.deck_list);

                final Activity activity = this;

                while (itr.hasNext()) {
                        Button b = new Button(this);
                        Deck d = itr.next();
                        b.setText(d.name);
                        //final Intent intent = new Intent("com.kaizen.videnda.action.DISPLAY_DECK", Uri.parse(d.name));
                        final Intent intent = new Intent(this, DeckDisplay.class);
                        intent.putExtra("path", d.path);
                        b.setOnClickListener(new View.OnClickListener()
                        {
                                public void onClick(View v)
                                {
                                        Log.i("send_intent", intent.getAction() + ":" + intent.getData());
                                        activity.startActivity(intent);
                                }
                        });
                        deck_list.addView(b);
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
