package com.sheryltania.ecohero;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;

import com.sheryltania.ecohero.R;

public class MainActivity extends AppCompatActivity {

    private MusicPlayer musicPlayer;
    private GameView gameView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        musicPlayer = MusicPlayer.getInstance(this);
        if (Preferences.soundOn(this) && !musicPlayer.isPlaying()) {
            musicPlayer.start();
        }
    }

    public void startGame(View view) {
        gameView = new GameView(this, this);
        setContentView(gameView);
    }

    public void showInstructions(View view) {
        String message = "In this game, you will be an <i>EcoHero</i> that sorts garbage based on their type: <b>recyclable</b>, <b>refuse</b>, or <b>green</b>.<br><br>" +
                "The game comes in three different modes: easy, medium, and difficult which you can configure yourself by tapping the gear icon on the top right screen of the menu.<br><br>" +
                "For each garbage that you sort correctly, you will be rewarded 50 points. For each mistake that you make, you will lose 1 life. You have ten lives, so you have ten chances to make mistakes.<br><br>" +
                "As a rule of thumb, here is how you sort garbage:" +
                "<ul>" +
                "<li> <b>Recyclables</b>: metal cans, glass bottles, newspaper, cardboard, plastic containers, papers.</li>" +
                "<li> <b>Green</b>: leaves, flowers, branches, Christmas trees.</li>" +
                "<li> <b>Refuse</b>: plastic bags, styrofoam, books, toys, electronics, magazines, cereal boxes.</li>" +
                "</ul>" +
                "<i>For more information, please refer to the <u>Recycling and Disposal Guide for Oahu</u>.</i>";

        Spanned spannedMessage = Html.fromHtml(message);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(spannedMessage);
        builder.setTitle("Instructions");

        builder.setPositiveButton("Got it!", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    /**
     * show the preferences window when the user taps the top right corner of the screen,
     * otherwise show the MainActivity (the game window)
     * @param m user's action with the mouse
     */
    @Override
    public boolean onTouchEvent(MotionEvent m) {
        if(m.getAction() == MotionEvent.ACTION_DOWN) {
            float x = m.getX();
            float y = m.getY();
            DisplayMetrics displayMetrics = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
            int w = displayMetrics.widthPixels;
            int h = displayMetrics.heightPixels;
            if (x > 0.75*w && y < 0.2*h) {
                Intent i = new Intent(this, Preferences.class); // the parameters are like a bridge, first start from the class you're instantiating it from (this) onto the activity that will be launched afterwards (MainActivity);
                startActivity(i);
            } else {
                Intent i = new Intent(this, MainActivity.class); // the parameters are like a bridge, first start from the class you're instantiating it from (this) onto the activity that will be launched afterwards (MainActivity);
                startActivity(i); //  start the activity
                finish(); // finish the current activity
            }
        }
        return true;
    }

    public void promptExit() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Warning");
        builder.setMessage("Are you sure you want to exit the app?");
        builder.setCancelable(true);
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                musicPlayer.stop();
                musicPlayer.release();
                finish();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                gameView.resumeGame();
                dialogInterface.dismiss();
            }
        });
        // Create the alert dialog using alert dialog builder
        AlertDialog dialog = builder.create();

        // Finally, display the dialog when user press back button
        dialog.show();
    }

    public void playAgainPrompt(int score) {
        androidx.appcompat.app.AlertDialog.Builder ab = new androidx.appcompat.app.AlertDialog.Builder(MainActivity.this);
        ab.setMessage("GAME OVER!\nScore: " + score)
                .setCancelable(false)
                .setPositiveButton("Play again", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        gameView.resetGame();
                    }
                })
                .setNeutralButton("Back to Menu", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                })
                .setNegativeButton("Exit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        musicPlayer.stop();
                        musicPlayer.release();
                        finish();
                    }
                });
        androidx.appcompat.app.AlertDialog a = ab.create();
        a.show();
    }

    @Override
    public void onResume() {
        super.onResume();
        boolean soundOn = Preferences.soundOn(this);
        if (soundOn && !musicPlayer.isPlaying()) {
            musicPlayer.start();
        } else if (!soundOn && musicPlayer.isPlaying()) {
            musicPlayer.pause();
        }
    }

    @Override
    public void onBackPressed() {
        gameView.pauseGame();
        promptExit();
    }
}