package com.example.geniusgame;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Random;

public class MainActivity extends Activity {

    private Button redButton, blueButton, greenButton, yellowButton, startButton, resetScoreButton;
    private TextView textView, textRecord;
    private int[] sequence;
    private int playerIndex, sequenceIndex, points;
    private boolean playerTurn, gameStarted;
    private Handler handler;
    private ArrayList<Button> buttons;

    private Preference preference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        redButton = (Button) findViewById(R.id.red_button);
        blueButton = (Button) findViewById(R.id.blue_button);
        greenButton = (Button) findViewById(R.id.green_button);
        yellowButton = (Button) findViewById(R.id.yellow_button);
        startButton = (Button) findViewById(R.id.start_button);
        resetScoreButton = (Button) findViewById(R.id.reset_score);
        textView = (TextView) findViewById(R.id.text_view);
        textRecord = (TextView) findViewById(R.id.text_records);

        redButton.setAlpha((float) 0.3);
        blueButton.setAlpha((float) 0.3);
        yellowButton.setAlpha((float) 0.3);
        greenButton.setAlpha((float) 0.3);

        buttons = new ArrayList<>();
        buttons.add(redButton);
        buttons.add(blueButton);
        buttons.add(greenButton);
        buttons.add(yellowButton);

        handler = new Handler();

        preference = new Preference(getApplicationContext());

        textRecord.setText("Maior pontuação: " + preference.getData());

        resetGame();
    }

    public void startGame(View view) {
        if (!gameStarted) {
            gameStarted = true;
            startButton.setEnabled(false);
            resetScoreButton.setEnabled(false);
            textRecord.setText("");
            textView.setText("Atenção!");
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    newSequence();
                    showSequence();
                }
            }, 1000);
        }
    }

    public void resetGame(View view) {
        if (gameStarted){
            resetGame();
        }
        else {
            Toast.makeText(this, "O jogo não está em andamento para ser resetado!", Toast.LENGTH_SHORT).show();
        }
    }

    public void onButtonClick(View view) {
        if (playerTurn) {
            Button button = (Button) view;
            int buttonIndex = buttons.indexOf(button);
            if (buttonIndex == sequence[playerIndex]) {
                button.setAlpha(1);
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        button.setAlpha((float) 0.3);
                    }
                }, 300);
                playerIndex++;
                if (playerIndex == sequenceIndex) {
                    playerTurn = false;
                    playerIndex = 0;
                    textView.setText("Acertou! espere para ver a nova sequencia.");
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            textView.setText("");
                            newSequence();
                            showSequence();
                        }
                    }, 2000);
                    points++;
                }
            } else {
                playerTurn = false;
                textView.setText("Errou! Sua pontuação final: " + points + " pontos");
                compareScores(String.valueOf(points));
            }
        }
    }

    private void resetGame() {
        sequence = new int[100];
        playerIndex = 0;
        sequenceIndex = 0;
        playerTurn = false;
        gameStarted = false;
        textView.setText("Genius");
        textRecord.setText("Maior pontuação: " + preference.getData());
        startButton.setEnabled(true);
        resetScoreButton.setEnabled(true);
        points = 0;
    }

    private void newSequence() {
        Random random = new Random();
        sequence[sequenceIndex] = random.nextInt(4);
        sequenceIndex++;
    }

    private void showSequence() {
        playerTurn = false;
        for (int i = 0; i < sequenceIndex; i++) {
            final int index = i;
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Button button = buttons.get(sequence[index]);
                    button.setAlpha(1);
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            button.setAlpha((float) 0.3);
                            if (index == sequenceIndex - 1) {
                                playerTurn = true;
                                textView.setText("Repita a sequencia!");
                            }
                        }
                    }, 500);
                }
            }, i * 1000);
        }
    }

    public void resetScoreHistory(View view){
        preference.saveData("0");
        Toast.makeText(this, "Pontuação resetada!", Toast.LENGTH_SHORT).show();
        textRecord.setText("Maior pontuação: " + preference.getData());
    }

    private void compareScores(String points){
        if (Integer.parseInt(points) > Integer.parseInt(preference.getData())){
            preference.saveData(points);
            textView.setText("Parabéns, novo recorde estabelecido! " + points + " pontos!");
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    resetGame();
                }
            }, 1500);
        }
    }
}