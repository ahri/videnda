package com.kaizen.videnda;

import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Scanner;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kaizen.util.Message;

public class Videnda extends Activity
{
        protected File path;
        private HashMap<String, RemoteDeck> remote_decks;

        /** Called when the activity is first created. */
        @Override
        public void onCreate(Bundle savedInstanceState)
        {
                super.onCreate(savedInstanceState);
                this.setContentView(R.layout.deckpick);

                System.setProperty("http.keepAlive", "false");

                String state = Environment.getExternalStorageState();

                if (!Environment.MEDIA_MOUNTED.equals(state)
                                || Environment.MEDIA_MOUNTED_READ_ONLY
                                                .equals(state)) {
                        Message.popup(this, "SD Card must be mounted");
                        this.finish();
                        return;
                }

                this.path = new File(Environment.getExternalStorageDirectory(),
                                this.getApplicationContext().getString(
                                                R.string.app_name));

                // Make sure the directory exists.
                this.path.mkdirs();
                if (!this.path.isDirectory()) {
                        Message.popup(this,
                                        "Cannot write directory: "
                                                        + this.path.getAbsolutePath());
                        this.finish();
                        return;
                }

                this.displayDecks();
        }

        public void displayDecks()
        {
                ArrayList<Deck> decks = this.getDecks(this.path);
                Iterator<Deck> itr = decks.iterator();

                LinearLayout deck_list = (LinearLayout) this
                                .findViewById(R.id.deck_list);
                
                deck_list.removeAllViews();

                final Activity activity = this;

                while (itr.hasNext()) {
                        Button b = new Button(this);
                        Deck d = itr.next();
                        b.setText(d.name);
                        final Intent intent = new Intent(this,
                                        DeckDisplay.class);
                        intent.putExtra("path", d.path);
                        b.setOnClickListener(new View.OnClickListener() {
                                public void onClick(View v)
                                {
                                        Log.i("send_intent",
                                                        intent.getAction()
                                                                        + ":"
                                                                        + intent.getData());
                                        activity.startActivity(intent);
                                }
                        });
                        deck_list.addView(b);
                }
                
                if (deck_list.getChildCount() == 0) {
                        TextView tv = new TextView(this);
                        tv.setText("Press MENU to add decks");
                        deck_list.addView(tv);
                }
        }

        @Override
        public boolean onCreateOptionsMenu(Menu menu)
        {
                MenuInflater inflater = getMenuInflater();
                inflater.inflate(R.menu.main, menu);
                return true;
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item)
        {
                // Handle item selection
                switch (item.getItemId()) {
                case R.id.download:
                        Menu m = item.getSubMenu();
                        m.clear();
                        this.remote_decks = this.getRemoteDecks();
                        Iterator<RemoteDeck> itr = this.remote_decks.values().iterator();
                        while (itr.hasNext()) {
                                RemoteDeck rd = itr.next();
                                MenuItem mi = m.add(rd.name);
                                mi.setCheckable(true);
                                if (rd.alreadyDownloaded()) {
                                        mi.setChecked(true);
                                }
                        }
                        return true;
                default:
                        if (this.remote_decks.containsKey(item.getTitle())) {
                                RemoteDeck rd = this.remote_decks.get(item.getTitle());
                                if (rd.alreadyDownloaded()) {
                                        // remove the file
                                        rd.local.delete();
                                } else {
                                        // download the file
                                        rd.download();
                                }
                                // TODO: the following line doesn't work
                                this.displayDecks();
                                return false;
                        }
                }
                return super.onOptionsItemSelected(item);
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
                                if (d != null) {
                                        decks.add(d);
                                }
                        } catch (IOException e) {
                                // do nothing
                                Log.i("deck_create_fail", f.getAbsolutePath());
                        } catch (NullPointerException e) {
                                // do nothing
                                Log.i("deck_null", f.getAbsolutePath());
                        }
                }

                return decks;
        }

        public HashMap<String, RemoteDeck> getRemoteDecks()
        {
                HashMap<String, RemoteDeck> list = new HashMap<String, RemoteDeck>();
                HttpURLConnection http_conn = null;
                try {
                        URL url = new URL(this.getString(R.string.deck_list_url));
                        http_conn = (HttpURLConnection) url.openConnection();

                        if (http_conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
                                Log.i("http_request", url.toExternalForm());
                                Log.i("http_response", "" + http_conn.getResponseCode());
                                Message.popup(this, "Deck list download failed");
                                return list;
                        }

                        Scanner deck_req = new Scanner(http_conn.getInputStream());
                        while (deck_req.hasNextLine()) {
                                String deck_line = deck_req.nextLine();
                                String filename = deck_line.replaceFirst("^[a-z0-9]{32} ", "");
                                RemoteDeck rd = new RemoteDeck(new URL(url.toExternalForm().replaceFirst("[^/]*$", "") +
                                                                     filename.replaceAll(" ", "%20")),
                                                             new File(this.path, filename),
                                                             deck_line.replaceFirst(" .*$", ""));
                                list.put(rd.name, rd);
                        }
                } catch (MalformedURLException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                }

                return list;
        }
}