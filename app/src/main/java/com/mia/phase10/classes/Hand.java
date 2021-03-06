package com.mia.phase10.classes;

import com.mia.phase10.exceptionClasses.CardNotFoundException;
import com.mia.phase10.exceptionClasses.EmptyHandException;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Hand implements Serializable {
    private Map<Integer,Card> cardList;

    public Hand() {
        cardList = new HashMap<>();
    }

    public void addCard(Card c){
        cardList.put(c.getId(),c);
    }

    public Card removeCard(int cardid) throws EmptyHandException, CardNotFoundException {
        if(this.cardList.isEmpty()){
            throw new EmptyHandException("tried to remove card from empty Hand!");
        }
        Card c = cardList.remove(cardid);
        if(c == null){
            throw new CardNotFoundException("Card not in Hand");
        }

       return c;
    }

    public Map<Integer, Card> getCardList() {
        return cardList;
    }

    public void setCardList(Map<Integer, Card> cardList) {
        this.cardList = cardList;
    }
}
