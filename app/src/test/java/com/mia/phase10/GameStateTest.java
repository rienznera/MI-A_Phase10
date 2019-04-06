package com.mia.phase10;

import android.util.Log;

import com.google.gson.Gson;
import com.mia.phase10.classes.CardStack;
import com.mia.phase10.classes.GameData;
import com.mia.phase10.classes.Player;

import org.json.JSONStringer;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class GameStateTest {
    @Test
    public void addition_isCorrect() {
        CardStack drawStack = new CardStack();
        drawStack.generateCardStack();
        drawStack.mixStack();
        CardStack layOffStack = new CardStack();

        Player player1 = new Player("player1");
        for(int i = 0; i<10; i++) {
            player1.getHand().addCard(drawStack.drawCard());
        }
        Player player2 = new Player("player2");
        for(int i = 0; i<10; i++) {
            player2.getHand().addCard(drawStack.drawCard());
        }

        Player player3 = new Player("player3");
        for(int i = 0; i<10; i++) {
            player3.getHand().addCard(drawStack.drawCard());
        }

        layOffStack.addCard(player1.getHand().removeCard());

        List<Player> playerList = new ArrayList<Player>();
        playerList.add(player1);

        GameData game = new GameData(layOffStack, drawStack, playerList, "player1");

        Gson  gson = new Gson();

        assertEquals(gson.toJson(game),"");

    }
}