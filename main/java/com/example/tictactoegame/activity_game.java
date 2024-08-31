package com.example.tictactoegame;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Arrays;

public class activity_game extends AppCompatActivity {
    boolean gameActive = true;

    int activePlayer = 0;
    int[] gameState = {2, 2, 2, 2, 2, 2, 2, 2, 2};

    int[][] winPositions = {{0, 1, 2}, {3, 4, 5}, {6, 7, 8},
            {0, 3, 6}, {1, 4, 7}, {2, 5, 8},
            {0, 4, 8}, {2, 4, 6}};
    public static int counter = 0;

    public void playerTap(View view) {
        ImageView img = (ImageView) view;
        int tappedImage = Integer.parseInt(img.getTag().toString());

        if (!gameActive) {
            gameReset(view);
            counter = 0;
        }

        if (gameState[tappedImage] == 2) {
            counter++;

            if (counter == 9) {
                gameActive = false;
            }

            gameState[tappedImage] = activePlayer;
            img.setTranslationY(-1000f);

            if (activePlayer == 0) {
                img.setImageResource(R.drawable.x);
                activePlayer = 1;
                TextView status = findViewById(R.id.status);

                status.setText("O's Turn - Tap to play");
            } else {
                img.setImageResource(R.drawable.o);
                activePlayer = 0;
                TextView status = findViewById(R.id.status);

                status.setText("X's Turn - Tap to play");
            }
            img.animate().translationYBy(1000f).setDuration(300);
        }
        int flag = 0;
        if (counter > 4) {
            for (int[] winPosition : winPositions) {
                if (gameState[winPosition[0]] == gameState[winPosition[1]] &&
                        gameState[winPosition[1]] == gameState[winPosition[2]] &&
                        gameState[winPosition[0]] != 2) {
                    flag = 1;

                    String winnerStr;

                    gameActive = false;
                    if (gameState[winPosition[0]] == 0) {
                        winnerStr = "X has won";
                        updateScore("score_playerA");
                    } else {
                        winnerStr = "O has won";
                        updateScore("score_playerB");
                    }
                    TextView status = findViewById(R.id.status);
                    status.setText(winnerStr);
                }
            }
            if (counter == 9 && flag == 0) {
                TextView status = findViewById(R.id.status);
                status.setText("Match Draw");
            }
        }
    }

    public void gameReset(View view) {
        gameActive = true;
        activePlayer = 0;

        Arrays.fill(gameState, 2);

        ((ImageView) findViewById(R.id.block1)).setImageResource(0);
        ((ImageView) findViewById(R.id.block2)).setImageResource(0);
        ((ImageView) findViewById(R.id.block3)).setImageResource(0);
        ((ImageView) findViewById(R.id.block4)).setImageResource(0);
        ((ImageView) findViewById(R.id.block5)).setImageResource(0);
        ((ImageView) findViewById(R.id.block6)).setImageResource(0);
        ((ImageView) findViewById(R.id.block7)).setImageResource(0);
        ((ImageView) findViewById(R.id.block8)).setImageResource(0);
        ((ImageView) findViewById(R.id.block9)).setImageResource(0);

        TextView status = findViewById(R.id.status);
        status.setText("X's Turn - Tap to play");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        Button exit=findViewById(R.id.exit_button);
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity_game.this, MainActivity.class);
                startActivity(intent);
            }
        });
        Button reset=findViewById(R.id.reset_button);
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gameReset(view);
            }
        });
    }

    private void updateScore(String player) {
        SharedPreferences prefs = getSharedPreferences("game_prefs", MODE_PRIVATE);
        int currentScore = prefs.getInt(player, 0);
        currentScore++;
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(player, currentScore);
        editor.apply();
    }
}
