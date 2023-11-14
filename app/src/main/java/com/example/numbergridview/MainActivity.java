package com.example.numbergridview;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private TextView mTextViewTurnAndScore;
    private static TextView mTextViewWinner;
    private static NumberGridView mNumberGridView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTextViewTurnAndScore = findViewById(R.id.textView_turn_and_score);
        mTextViewWinner = findViewById(R.id.textView_winner);
        mNumberGridView = findViewById(R.id.number_grid_view);

        mNumberGridView.setUpdateUiListener(new NumberGridView.UpdateUiListener() {
            @Override
            public void updateUi() {
                OthelloGame game = mNumberGridView.getGame();
                int blackScore = game.getScore(OthelloGame.BLACK);
                int whiteScore = game.getScore(OthelloGame.WHITE);
                String currentPlayer = game.getCurrentPlayer() == OthelloGame.BLACK ? "Black" : "White";
                String text = "Turn: " + currentPlayer + " | Black: " + blackScore + " | White: " + whiteScore;
                mTextViewTurnAndScore.setText(text);
                if (game.isGameOver()) {
                    String winnerText;
                    int blackScoreFinal = game.getScore(OthelloGame.BLACK);
                    int whiteScoreFinal = game.getScore(OthelloGame.WHITE);
                    if (blackScoreFinal > whiteScoreFinal) {
                        winnerText = "Winner: Black";
                    } else if (blackScoreFinal < whiteScoreFinal) {
                        winnerText = "Winner: White";
                    } else {
                        winnerText = "It's a tie!";
                    }
                    mTextViewWinner.setText(winnerText);
                }
            }
        });

        // Start a new game when the app launches
        mNumberGridView.getGame().startNewGame();

        Button newGameButton = findViewById(R.id.button_new_game);
        newGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mNumberGridView.getGame().startNewGame();
                mTextViewWinner.setText("");
                mNumberGridView.invalidate();
            }
        });
    }

    public static NumberGridView getNumberGridView() {
        return mNumberGridView;
    }

    public static TextView getTextViewWinner() {
        return mTextViewWinner;
    }
}
