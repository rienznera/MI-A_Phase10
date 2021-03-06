package com.mia.phase10.activities;

import android.content.ClipData;
import android.content.ClipDescription;
import android.util.Log;
import android.view.DragEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.mia.phase10.classes.Card;
import com.mia.phase10.exceptionClasses.CardNotFoundException;
import com.mia.phase10.exceptionClasses.EmptyHandException;
import com.mia.phase10.exceptionClasses.PlayerNotFoundException;
import com.mia.phase10.gameLogic.GameLogicHandler;


public class MyDragEventListener implements View.OnDragListener {

    // This is the method that the system calls when it dispatches a drag event to the listener.
    @Override
    public boolean onDrag(View v, DragEvent event) {
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1);
        lp.setMargins(0, 0, 0, 0);
        // Defines a variable to store the action type for the incoming event
        int action = event.getAction();
        // Handles each of the expected events

        switch (action) {

            case DragEvent.ACTION_DRAG_STARTED:
                // Determines if this View can accept the dragged data
                if (event.getClipDescription().hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN)) {
                    return true;
                }
                return false;

            case DragEvent.ACTION_DRAG_ENTERED:
            case DragEvent.ACTION_DRAG_EXITED:
            case DragEvent.ACTION_DRAG_ENDED:
                // Invalidates the view to force a redraw
                v.invalidate();
                // returns true; the value is ignored.
                return true;

            case DragEvent.ACTION_DRAG_LOCATION:
                // Ignore the event
                return true;

            case DragEvent.ACTION_DROP:
                // Invalidates the view to force a redraw
                v.invalidate();

                ImageView vw = (ImageView) event.getLocalState();
                Card c = GameLogicHandler.getInstance().getGameData().getPlayers().get(GameLogicHandler.getInstance().getGameData().getActivePlayerId()).getHand().getCardList().get(vw.getId());

                if(c.getImagePath().equals("card_expose")){
                    try {
                        if(GameLogicHandler.getInstance().getGameData().getPlayers().get(GameLogicHandler.getInstance().getGameData().getActivePlayerId()).getHand().getCardList().size()==1){
                            GameLogicHandler.getInstance().layoffCard(GameLogicHandler.getInstance().getGameData().getActivePlayerId(), vw.getId());
                        }
                        else {GameLogicHandler.getInstance().exposePlayer(vw.getId());}
                    }catch (EmptyHandException | CardNotFoundException | PlayerNotFoundException e) {
                        e.printStackTrace();
                    }
                    return true;}

                try {
                    GameLogicHandler.getInstance().layoffCard(GameLogicHandler.getInstance().getGameData().getActivePlayerId(), vw.getId());
                } catch (EmptyHandException | CardNotFoundException | PlayerNotFoundException e) {
                    e.printStackTrace();
                }


                // Returns true. DragEvent.getResult() will return true.
                return true;


            // An unknown action type was received.
            default:
                Log.e("DragDrop Example", "Unknown action type received by OnDragListener.");
                break;
        }
        return false;
    }
}
