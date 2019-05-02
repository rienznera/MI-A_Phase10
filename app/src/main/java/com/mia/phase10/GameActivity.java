package com.mia.phase10;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipDescription;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mia.phase10.classes.Card;
import com.mia.phase10.classes.CardStack;
import com.mia.phase10.classes.GameData;
import com.mia.phase10.classes.Player;
import com.mia.phase10.exceptionClasses.EmptyCardStackException;
import com.mia.phase10.gameLogic.GameLogicHandler;

import java.util.Map;

public class GameActivity extends AppCompatActivity implements View.OnLongClickListener {

    private Button shuffle;
    private Button stop;
    private Button switchPlayer;
    private View discardPile;
    private LinearLayout deck;
    private LinearLayout discardPileLayout;
    private LinearLayout p1_playstation;
    private ImageView stack;
    private GameLogicHandler gameLogicHandler;
    private GameData gameData;

    @SuppressLint("CutPasteId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        // Get the Intent that started this activity and extract the string
        final Intent intent = getIntent();
        final TextView player1 = findViewById(R.id.ID_player_1);
        final TextView player2 = findViewById(R.id.ID_player_2);
        // Capture the layout's TextView and set the string as its text
        player1.setText(intent.getStringExtra(MainActivity.FIRST_PLAYER));
        player2.setText(intent.getStringExtra(MainActivity.SECOND_PLAYER));

        gameLogicHandler = GameLogicHandler.getInstance();
        gameLogicHandler.initializeGame();
        gameLogicHandler.addPlayer(new Player("player_1"));
        gameLogicHandler.addPlayer(new Player("player_2"));

        try {
            gameLogicHandler.startRound();
        } catch (EmptyCardStackException e) {
            e.printStackTrace();
        }
        gameData = gameLogicHandler.getGameData();
        gameData.setActivePlayerId("player_1");

        discardPile = findViewById(R.id.ID_discard_pile);
        stack = findViewById(R.id.ID_stack);
        deck = findViewById(R.id.ID_deck);
        discardPileLayout = findViewById(R.id.ID_discard_layout);
        shuffle = findViewById(R.id.openShuffling);
        shuffle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startShufflingActivity();
            }
        });

        showHandCards();
        switchPlayer = findViewById(R.id.ID_player_switch);
        switchPlayer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String, Player> players = gameData.getPlayers();
                if (gameData.getActivePlayerId().equals("player_1")) {
                    gameData.setActivePlayerId("player_2");

                    player1.setText(intent.getStringExtra(MainActivity.SECOND_PLAYER));
                    player2.setText(intent.getStringExtra(MainActivity.FIRST_PLAYER));
                } else {
                    gameData.setActivePlayerId("player_1");
                    ((LinearLayout) deck).removeAllViews();
                    player1.setText(intent.getStringExtra(MainActivity.FIRST_PLAYER));
                    player2.setText(intent.getStringExtra(MainActivity.SECOND_PLAYER));
                }
                showHandCards();
            }
        });

        MyDragEventListener myDragEventListener = new MyDragEventListener(this);
        discardPileLayout.setOnDragListener(myDragEventListener);
        discardPile.setTag("DISCARD PILE");
        discardPile.setOnLongClickListener(this);

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1);
        lp.setMargins(-70, 0, 0, 0);
        ImageView cardImage = new ImageView(this);
        cardImage.setLayoutParams(lp);
        Drawable c = getResources().getDrawable(getResources().getIdentifier(gameData.getDrawStack().getFirstCard().getImagePath(), "drawable", getPackageName()));
        cardImage.setImageDrawable(c);
        cardImage.setTag("DISCARD PILE");
        cardImage.setOnLongClickListener(this);
        discardPileLayout.addView(cardImage);


        stack.setOnClickListener(new View.OnClickListener() {
            //@Override
            public void onClick(View v) {
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1);
                lp.setMargins(-70, 0, 0, 0);
                ImageView cardImage = new ImageView(GameActivity.this);
                Card drawStackCard = null;
                try {
                    drawStackCard = gameData.getDrawStack().drawCard();
                } catch (EmptyCardStackException e) {
                    e.printStackTrace();
                }
                cardImage.setLayoutParams(lp);
                Drawable c = getResources().getDrawable(getResources().getIdentifier(drawStackCard.getImagePath(), "drawable", getPackageName()));
                cardImage.setImageDrawable(c);
                cardImage.setTag("DISCARD PILE");
                cardImage.setOnLongClickListener(GameActivity.this);
                gameData.getPlayers().get(gameData.getActivePlayerId()).getHand().addCard(drawStackCard);
                deck.addView(cardImage);
            }
        });
    }

    public void startShufflingActivity() {
        //CardStack.mixStack();
        Intent intent = new Intent(this, ShufflingActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onLongClick(View v) {
        // Create a new ClipData.Item from the ImageView object's tag
        ClipData.Item item = new ClipData.Item((CharSequence) v.getTag());
        // Create a new ClipData using the tag as a label, the plain text MIME type, and
        // the already-created item. This will create a new ClipDescription object within the
        // ClipData, and set its MIME type entry to "text/plain"
        String[] mimeTypes = {ClipDescription.MIMETYPE_TEXT_PLAIN};
        ClipData data = new ClipData(v.getTag().toString(), mimeTypes, item);
        // Instantiates the drag shadow builder.
        View.DragShadowBuilder dragshadow = new View.DragShadowBuilder(v);
        // Starts the drag
        v.startDrag(data      // data to be dragged
                , dragshadow  // drag shadow builder
                , v           // local data about the drag and drop operation
                , 0      // flags (not currently used, set to 0)
        );
        return true;
    }

    public void showHandCards() {
        Map<String, Player> players = gameData.getPlayers();
        Map<Integer, Card> cards = players.get(gameData.getActivePlayerId()).getHand().getCardList();
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1);
        lp.setMargins(-70, 0, 0, 0);
        for (Card card : cards.values()) {
            ImageView cardImage = new ImageView(GameActivity.this);
            cardImage.setLayoutParams(lp);
            Drawable c = getResources().getDrawable(getResources().getIdentifier(card.getImagePath(), "drawable", getPackageName()));
            cardImage.setImageDrawable(c);
            cardImage.setTag("DISCARD PILE");
            cardImage.setOnLongClickListener(GameActivity.this);
            cardImage.setId(card.getId());
            deck.addView(cardImage);
        }
    }

    public LinearLayout getDeck() {
        return deck;
    }
}
