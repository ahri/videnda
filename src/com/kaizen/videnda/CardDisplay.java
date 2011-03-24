package com.ahri.flashcards;

import java.util.ArrayList;
import java.util.Random;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

class CardDisplay
{
        protected Context context;
        protected ImageView card_view;
        protected ViewGroup card_row;
        protected ViewGroup button_row;
        protected Deck deck;
        protected int num_answers;

        public CardDisplay(Context context, ImageView card_view, ViewGroup card_row, ViewGroup button_row, Deck deck, int num_answers)
        {
                this.context = context;
                this.card_view = card_view;
                this.card_row = card_row;
                this.button_row = button_row;
                this.deck = deck;
                this.num_answers = num_answers;
        }

        public void random()
        {
                ArrayList<Card> answers = this.deck.getRandomCards(this.num_answers, new ArrayList<Card>());
                if (answers == null) {
                        return;
                }

                this.button_row.removeAllViews();

                Random r = new Random();
                final Card card = answers.get(r.nextInt(this.num_answers));

                this.card_view.setImageBitmap(BitmapFactory.decodeFile(card.file.getAbsolutePath()));

                for (int i = 0; i < this.num_answers; i++) {
                        final Card answer = answers.get(i);
                        Button b = this.makeButton(answer.answer);

                        b.setOnClickListener(new View.OnClickListener()
                        {
                                public void onClick(View v)
                                {
                                        Toast.makeText(v.getContext(),
                                                       answer.equals(card)? "Correct!" : "Incorrect",
                                                       Toast.LENGTH_SHORT).show();

                                        CardDisplay.this.random();
                                }
                        });

                        this.button_row.addView(b);
                }
        }

        protected Button makeButton(String answer) {
                Button b = new Button(this.context);
                b.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.FILL_PARENT, 1));
                b.setText(answer.toCharArray(), 0, answer.length());
                return b;
        }
}
