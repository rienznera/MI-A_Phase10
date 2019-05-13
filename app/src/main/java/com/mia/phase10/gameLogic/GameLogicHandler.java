package com.mia.phase10.gameLogic;

import com.google.gson.Gson;
import com.mia.phase10.classes.Card;
import com.mia.phase10.classes.CardStack;
import com.mia.phase10.classes.GameData;
import com.mia.phase10.classes.Player;
import com.mia.phase10.exceptionClasses.CardNotFoundException;
import com.mia.phase10.exceptionClasses.EmptyCardStackException;
import com.mia.phase10.exceptionClasses.EmptyHandException;
import com.mia.phase10.exceptionClasses.PlayerNotFoundException;
import com.mia.phase10.gameFlow.GamePhase;

import java.util.HashMap;

public class GameLogicHandler {
    private static volatile GameLogicHandler glhInstance = new GameLogicHandler();
    private GameData gameData;

    //private constructor.
    private GameLogicHandler(){

    }

    public static GameLogicHandler getInstance() {
        return glhInstance;
    }

    public void initializeGame(){
        CardStack drawStack = new CardStack();
        drawStack.generateCardStack();

        CardStack layOffStack = new CardStack();

        this.gameData = new GameData(layOffStack,drawStack,new HashMap<String,Player>(),"");

    }

    public void addPlayer(Player player){
        this.gameData.addPlayer(player);
    }

    public void startRound()throws EmptyCardStackException{

        for(Player p : gameData.getPlayers().values()){
            for(int i=0; i<10;i++){
                Card c = this.gameData.getDrawStack().drawCard();
                p.getHand().addCard(c);
            }
        }
        this.gameData.setPhase(GamePhase.DRAW_PHASE);
    }
    public void layoffCard(String playerId, int cardId) throws EmptyHandException, CardNotFoundException, PlayerNotFoundException {

        try{

            Card c = gameData.getPlayers().get(playerId).getHand().removeCard(cardId);
            this.gameData.getLayOffStack().addCard(c);

            this.gameData.nextPlayer();
            this.gameData.setPhase(GamePhase.DRAW_PHASE);

        }catch(Exception c){
            throw new PlayerNotFoundException("Player not found!");
        }
    }
    public void drawCard(String playerId, StackType stackType) throws EmptyCardStackException {

        switch(stackType){

            case DRAW_STACK:
                Card drawCard = gameData.getDrawStack().drawCard();
                gameData.getPlayers().get(playerId).getHand().addCard(drawCard);
                break;
            case LAYOFF_STACK:
                 Card firstCard = gameData.getLayOffStack().getFirstCard();
                gameData.getPlayers().get(playerId).getHand().addCard(firstCard);

        }
        this.gameData.setPhase(GamePhase.LAYOFF_PHASE);
    }


    public String getGameState(){
        Gson gson = new Gson();
        return gson.toJson(this.gameData);
    }

    public void setGameState(String json){
        Gson gson = new Gson();
        this.gameData = gson.fromJson(json,GameData.class);
    }

}
