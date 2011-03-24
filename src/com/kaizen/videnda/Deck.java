package com.kaizen.videnda;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;
import java.io.FileNotFoundException;

abstract public class Deck
{
        public ArrayList<Card> cards;

        public Deck(File path) throws FileNotFoundException
        {
                this.cards = new ArrayList<Card>();
                this.populateCards(path);
        }

        public ArrayList<Card> getRandomCards(int number, ArrayList<Card> already_used)
        {
                if (already_used.size() >= this.cards.size()) {
                        return null;
                }

                ArrayList<Card> cards = new ArrayList<Card>();
                Random r = new Random();

                boolean new_card = false;
                while (cards.size() < number || !new_card) {
                        Card card = this.cards.get(r.nextInt(this.cards.size()));
                        if (this.contains(cards, card)) {
                                continue;
                        }
                        if (!this.contains(already_used, card)) {
                                new_card = true;
                        }
                        cards.add(card);
                }

                return cards;
        }

        protected boolean contains(ArrayList<Card> cards, Card card)
        {
                Iterator<Card> itr = cards.listIterator();
                while (itr.hasNext()) {
                        if (itr.next().equals(card)) {
                                return true;
                        }
                }
                return false;
        }

        abstract protected void populateCards(File path) throws FileNotFoundException;
}
