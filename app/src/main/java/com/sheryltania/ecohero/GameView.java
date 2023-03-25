package com.sheryltania.ecohero;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.media.MediaPlayer;
import android.os.Handler;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;

import com.sheryltania.ecohero.R;

import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;

public class GameView extends View {
    private MainActivity mActivity;
    Bitmap background, ground;
    Rect rectBackground, rectGround;
    Context context;
    Handler handler;
    final int UPDATE_MILLIS = 50;
    Runnable runnable;
    Paint textPaint = new Paint();
    int score = 0;
    int lives = 10;
    public static int deviceWidth, deviceHeight;
    int binX, binY;
    Random random;
    ArrayList<Garbage> garbages;
    ArrayList<Bin> bins;
    Bin recycleBin;
    Bin greenBin;
    Bin refuseBin;
    Timer timer;
    String correctAnswer;
    boolean gameOver;
    boolean isPaused;
    boolean isTutorialMode;
    int tutorialCounter = 0;

    public GameView(Context context, MainActivity ma) {
        super(context);
        mActivity = ma;

        this.context = context;
        background = BitmapFactory.decodeResource(context.getResources(), R.drawable.wallpaper);
        ground = BitmapFactory.decodeResource(context.getResources(), R.drawable.floor);
        recycleBin = new Bin(context, BinType.RECYCLE);
        greenBin = new Bin(context, BinType.GREEN);
        refuseBin = new Bin(context, BinType.REFUSE);

        // Get the device width and height
        Display display = ((Activity) getContext()).getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        deviceWidth = size.x;
        deviceHeight = size.y;

        // Set areas for background and ground
        rectBackground = new Rect(0, 0, deviceWidth, deviceHeight);
        rectGround = new Rect(0, deviceHeight - ground.getHeight(), deviceWidth, deviceHeight);

        // For timer
        handler = new Handler();
        timer = new Timer();

        // To redraw the view
        runnable = new Runnable() {
            @Override
            public void run() {
                invalidate();
            }
        };

        textPaint.setColor(Color.WHITE);
        textPaint.setTextSize(deviceWidth * 0.05f);
        random = new Random();
        garbages = new ArrayList<>();

        bins = new ArrayList<>();
        bins.add(recycleBin.getScaledBin(deviceWidth, deviceHeight));
        bins.add(greenBin.getScaledBin(deviceWidth, deviceHeight));
        bins.add(refuseBin.getScaledBin(deviceWidth, deviceHeight));

        // Set the coordinates of the first bin
        binX = (int) (deviceWidth * 0.05f);
        binY = deviceHeight - ground.getHeight() - bins.get(0).binImage.getHeight() + 20;
        bins.get(0).setBinY(binY);
        bins.get(0).setBinX(binX);
        bins.get(1).setBinY(binY);
        bins.get(1).setBinX(binX + bins.get(1).binImage.getWidth() - bins.get(1).binImage.getWidth()/5);
        bins.get(2).setBinY(binY);
        bins.get(2).setBinX(binX + 2*(bins.get(2).binImage.getWidth() - bins.get(2).binImage.getWidth()/5));

        // Initialize difficulty
        switch(Preferences.getDifficulty(context)) {
            case 1:
                while (garbages.size() < 3) { // Limit the number of garbages to 5-6
                    Garbage garbage = new Garbage(context);
                    garbages.add(garbage);
                }
                break;
            case 2:
                while (garbages.size() < 4) {
                    Garbage garbage = new Garbage(context);
                    garbages.add(garbage);
                }
                break;
            case 3:
                while (garbages.size() < 5) {
                    Garbage garbage = new Garbage(context);
                    garbages.add(garbage);
                }
                break;
        }

        // Set gameOver and isPaused to false
        gameOver = false;
        isPaused = false;
        isTutorialMode = true;

        // Initialize correct answer
        correctAnswer = "";
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawBitmap(background, null, rectBackground, null);
        canvas.drawBitmap(ground, null, rectGround, null);

        binX = (int) (deviceWidth * 0.05f);

        for (int i = 0; i < bins.size(); i++) {
            canvas.drawBitmap(bins.get(i).binImage, binX, binY, null);
            binX += bins.get(i).binImage.getWidth() - bins.get(i).binImage.getWidth() / 5;
        }

        // Draw score and lives
        canvas.drawText("Score: " + score, deviceWidth*0.075f, deviceHeight*0.05f, textPaint);
        canvas.drawText("Lives: " + lives, deviceWidth*0.75f, deviceHeight*0.05f, textPaint);

        // Draw correct answer
        Paint paint = new Paint();
        paint.setTextSize(canvas.getHeight() * 0.025f);
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setColor(Color.WHITE);
        float xPos = canvas.getWidth() / 2;
        float yPos = canvas.getHeight() * 0.12f;
        String text = correctAnswer;
        String[] lines = text.split("\n");
        float lineHeight = paint.getTextSize();
        for (int i = 0; i < lines.length; i++) {
            float y = yPos + (i * lineHeight);
            canvas.drawText(lines[i], xPos, y, paint);
        }

        if(isTutorialMode) {
            Garbage garbage = garbages.get(tutorialCounter);
            Bitmap garbageBitmap = garbage.getGarbage();
            canvas.drawBitmap(garbageBitmap, garbage.garbageX, garbage.garbageY, null);
            garbage.updatePosition();
            correctAnswer = "Tutorial:\n This is a " + garbage.garbageName + ". It should go to " + garbage.type + ". \nDrag it onto the " + garbage.type + " bin.";

            if (garbage.garbageY + garbage.getGarbageHeight() >= deviceHeight - recycleBin.binImage.getHeight() && !garbage.isDragging) {
                garbage.resetPosition();
            }

            for (int j = 0; j < bins.size(); j++) {
                Bin bin = bins.get(j);
                // Detect collision with a buffer of 10px
                if (collidesWithBin(garbage, bin)) {
                    if (garbage.type == bin.type) {
                        correctAnswer = "Good job!";
                    } else {
                        correctAnswer = garbage.garbageName + " should go to " + garbage.type + "!";
                    }
                    break;
                }
            }
        } else {
            // Draw garbages
            for (int i = 0; i < garbages.size(); i++) {
                Garbage garbage = garbages.get(i);
                Bitmap garbageBitmap = garbage.getGarbage();

                canvas.drawBitmap(garbageBitmap, garbage.garbageX, garbage.garbageY, null);

                garbage.updatePosition();

                // If garbage touches the floor, decrease lives
                if (garbage.garbageY + garbage.getGarbageHeight() >= deviceHeight - recycleBin.binImage.getHeight() && !garbage.isDragging) {
                    decreaseLives();
                    garbage.resetPosition();
                }

                // if garbage falls to the bin, do logic
                for (int j = 0; j < bins.size(); j++) {
                    Bin bin = bins.get(j);
                    // Detect collision with a buffer of 10px
                    if (collidesWithBin(garbage, bin)) {
                        if (garbage.type == bin.type) {
                            correctAnswer = "Good job!";
                            score += 50;
                        } else {
                            correctAnswer = garbage.garbageName + " should go to " + garbage.type + "!";
                            decreaseLives();
                        }
                        garbages.remove(garbage);
                        getMoreGarbage();
                        break;
                    }

                    // Avoid array index out of bounds
                    garbage.garbageIndex = (garbage.garbageIndex + 1) % 3;
                }
            }
        }

        // Schedule the next invalidation, unless game is over
        if(!gameOver && !isPaused) {
            handler.postDelayed(runnable, UPDATE_MILLIS);
        }
    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();

        // Get the x and y coordinates of the touch event
        float touchX = event.getX();
        float touchY = event.getY();

        switch(action) {
            case MotionEvent.ACTION_DOWN:
                // Check if the touch event is within the bounds of a falling garbage object
                for(int i = 0; i < garbages.size(); i++) {
                    Garbage garbage = garbages.get(i);
                    Bitmap garbageBitmap = garbage.getGarbage();
                    if (touchX >= garbage.garbageX && touchX < garbage.garbageX + garbageBitmap.getWidth() &&
                            touchY >= garbage.garbageY && touchY < garbage.garbageY + garbageBitmap.getHeight()) {
                        // Set the garbage's dragging flag to true
                        garbage.isDragging = true;
                        // Set the garbage's touch offset to the difference between the touch coordinates and the garbage's position
                        garbage.touchOffsetX = (int) (touchX - garbage.garbageX);
                        garbage.touchOffsetY = (int) (touchY - garbage.garbageY);
                        // Break out of the loop since we only want to move one garbage object at a time
                        break;
                    }

                }
                break;
            case MotionEvent.ACTION_MOVE:
                // Find the garbage object that is currently being dragged
                for(int i = 0; i < garbages.size(); i++) {
                    Garbage garbage = garbages.get(i);
                    if(garbage.isDragging) {
                        // Move the garbage object according to the touch event
                        garbage.garbageX = (int) (touchX - garbage.touchOffsetX);
                        garbage.garbageY = (int) (touchY - garbage.touchOffsetY);
                        invalidate();
                        // Break out of the loop since we only want to move one garbage object at a time
                        break;
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                // If a garbage object is being dragged, check if it has collided with a bin
                for(int i = 0; i < garbages.size(); i++) {
                    Garbage garbage = garbages.get(i);
                    if(garbage.isDragging) {
                        for(int j = 0; j < bins.size(); j++) {
                            Bin bin = bins.get(j);
                            if(collidesWithBin(garbage, bin)) {
                                if(garbage.type == bin.type && !isTutorialMode) {
                                    score += 50;
                                } else if (garbage.type == bin.type && isTutorialMode) {
                                    if(tutorialCounter == 2) {
                                        isTutorialMode = false;
                                        correctAnswer = "You're on your own now. Good luck!";
                                    } else {
                                        tutorialCounter++;
                                    }
                                    invalidate();
                                } else {
                                    correctAnswer = garbage.garbageName + " should go to " + garbage.type + "!";
                                    decreaseLives();
                                }
                                garbages.remove(garbage);
                                getMoreGarbage();
                                break;
                            }
                        }
                        // Set the garbage's dragging flag to false
                        garbage.isDragging = false;
                    }
                }
                break;
        }
        return true;
    }

    public void getMoreGarbage() {
        Garbage newGarbage = new Garbage(context);
        garbages.add(newGarbage);
    }

    public void decreaseLives() {
        if(lives >= 1) {
            lives--;
        } else {
            if(gameOver == false) {
                mActivity.playAgainPrompt(score);
                gameOver = true;
            }
        }
    }

    // Detect collision with a buffer of 5px
    public boolean collidesWithBin(Garbage garbage, Bin bin) {
        return (garbage.garbageX + garbage.garbageImage.getWidth() + 5 >= bin.binX &&
                garbage.garbageX - 5 <= bin.binX + bin.binImage.getWidth() &&
                garbage.garbageY + garbage.getGarbageHeight() + 5 >= bin.binY &&
                garbage.garbageY - 5 <= bin.binY + bin.binImage.getHeight());
    }

    public void resetGame() {
        score = 0;
        lives = 10;
        garbages.clear();
        bins.clear();
        recycleBin = new Bin(context, BinType.RECYCLE);
        greenBin = new Bin(context, BinType.GREEN);
        refuseBin = new Bin(context, BinType.REFUSE);
        bins.add(recycleBin.getScaledBin(deviceWidth, deviceHeight));
        bins.add(greenBin.getScaledBin(deviceWidth, deviceHeight));
        bins.add(refuseBin.getScaledBin(deviceWidth, deviceHeight));
        binX = (int) (deviceWidth * 0.05f);
        binY = deviceHeight - ground.getHeight() - bins.get(0).binImage.getHeight() + 20;
        bins.get(0).setBinY(binY);
        bins.get(0).setBinX(binX);
        bins.get(1).setBinY(binY);
        bins.get(1).setBinX(binX + bins.get(1).binImage.getWidth() - bins.get(1).binImage.getWidth()/5);
        bins.get(2).setBinY(binY);
        bins.get(2).setBinX(binX + 2*(bins.get(2).binImage.getWidth() - bins.get(2).binImage.getWidth()/5));
        correctAnswer = "";
        gameOver = false;
        isTutorialMode = false;

        switch(Preferences.getDifficulty(context)) {
            case 1:
                while (garbages.size() < 5) { // Limit the number of garbages to 5-6
                    Garbage garbage = new Garbage(context);
                    garbages.add(garbage);
                }
                break;
            case 2:
                while (garbages.size() < 7) {
                    Garbage garbage = new Garbage(context);
                    garbages.add(garbage);
                }
                break;
            case 3:
                while (garbages.size() < 9) {
                    Garbage garbage = new Garbage(context);
                    garbages.add(garbage);
                }
                break;
        }

        // Start the game loop
        handler.postDelayed(runnable, UPDATE_MILLIS);
    }

    public void pauseGame() {
        isPaused = true;
    }

    public void resumeGame() {
        isPaused = false;
        invalidate(); // redraw the view
    }
}
