package com.sheryltania.ecohero;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.sheryltania.ecohero.R;

import java.util.ArrayList;
import java.util.Random;


public class Garbage {
    public Bitmap garbageImage;
    public String garbageName;
    public int garbageIndex;
    public int garbageX, garbageY;
    public int garbageVelocity;
    public Random random;
    public boolean isDragging;
    public int touchOffsetX, touchOffsetY;
    public int originalVelocity;
    public BinType type;
    public ArrayList<Bitmap> greens;
    public ArrayList<Bitmap> recyclables;
    public ArrayList<Bitmap> refuses;
    public ArrayList<String> greenNames;
    public ArrayList<String> recyclableNames;
    public ArrayList<String> refuseNames;

    public Garbage(Context context) {
        greens = new ArrayList<Bitmap>();
        recyclables = new ArrayList<Bitmap>();
        refuses = new ArrayList<Bitmap>();
        refuseNames = new ArrayList<String>();
        recyclableNames = new ArrayList<String>();
        greenNames = new ArrayList<String>();
        initImages(context);
        random = new Random();
        garbageIndex = random.nextInt(3);
        if (garbageIndex == 0) {
            int garbageImageInt = random.nextInt(refuses.size());
            garbageImage = refuses.get(garbageImageInt);
            garbageName = refuseNames.get(garbageImageInt);
            type = BinType.REFUSE;
        } else if (garbageIndex == 1) {
            int garbageImageInt = random.nextInt(greens.size());
            garbageImage = greens.get(garbageImageInt);
            garbageName = greenNames.get(garbageImageInt);
            type = BinType.GREEN;
        } else {
            int garbageImageInt = random.nextInt(recyclables.size());
            garbageImage = recyclables.get(garbageImageInt);
            garbageName = recyclableNames.get(garbageImageInt);
            type = BinType.RECYCLE;
        }
        garbageX = random.nextInt(GameView.deviceWidth - garbageImage.getWidth());
        garbageY = -garbageImage.getHeight();
        isDragging = false;
        touchOffsetX = 0;
        touchOffsetY = 0;
        switch(Preferences.getDifficulty(context)) {
            case 1:
                // add 1 to avoid zero
                garbageVelocity = random.nextInt(2) + 1;
                originalVelocity = garbageVelocity;
                break;
            case 2:
                garbageVelocity = random.nextInt(4) + 1;
                originalVelocity = garbageVelocity;
                break;
            case 3:
                garbageVelocity = random.nextInt(6) + 1;
                originalVelocity = garbageVelocity;
                break;
        }
    }

    public void updatePosition() {
        garbageY += garbageVelocity;
    }

    public Bitmap getGarbage() {
        return garbageImage;
    }

    public int getGarbageHeight() {
        return garbageImage.getHeight();
    }

    public void resetPosition() {
        Random random = new Random();
        garbageVelocity = originalVelocity;
        garbageIndex = random.nextInt(3);
        garbageX = random.nextInt(GameView.deviceWidth - garbageImage.getWidth());
        garbageY = -garbageImage.getHeight();
    }

    private void initImages(Context context) {
        Bitmap green1 = BitmapFactory.decodeResource(context.getResources(), R.drawable.green1);
        Bitmap green2 = BitmapFactory.decodeResource(context.getResources(), R.drawable.green2);
        Bitmap green3 = BitmapFactory.decodeResource(context.getResources(), R.drawable.green3);
        Bitmap green4 = BitmapFactory.decodeResource(context.getResources(), R.drawable.green4);
        Bitmap green5 = BitmapFactory.decodeResource(context.getResources(), R.drawable.green5);
        Bitmap green6 = BitmapFactory.decodeResource(context.getResources(), R.drawable.green6);

        greens.add(Bitmap.createScaledBitmap(green1, (int)(GameView.deviceWidth*0.15f), (int)(GameView.deviceWidth*0.12f), false));
        greenNames.add("Leaf");
        greens.add(Bitmap.createScaledBitmap(green2, (int)(GameView.deviceWidth*0.15f), (int)(GameView.deviceWidth*0.12f), false));
        greenNames.add("Leaf");
        greens.add(Bitmap.createScaledBitmap(green3, (int)(GameView.deviceWidth*0.15f), (int)(GameView.deviceWidth*0.12f), false));
        greenNames.add("Flower");
        greens.add(Bitmap.createScaledBitmap(green4, (int)(GameView.deviceWidth*0.15f), (int)(GameView.deviceWidth*0.12f), false));
        greenNames.add("Flower");
        greens.add(Bitmap.createScaledBitmap(green5, (int)(GameView.deviceWidth*0.15f), (int)(GameView.deviceWidth*0.12f), false));
        greenNames.add("Branch");
        greens.add(Bitmap.createScaledBitmap(green6, (int)(GameView.deviceWidth*0.15f), (int)(GameView.deviceWidth*0.12f), false));
        greenNames.add("Branch");

        Bitmap recyclable1 = BitmapFactory.decodeResource(context.getResources(), R.drawable.recyclable1);
        Bitmap recyclable2 = BitmapFactory.decodeResource(context.getResources(), R.drawable.recyclable2);
        Bitmap recyclable3 = BitmapFactory.decodeResource(context.getResources(), R.drawable.recyclable3);
        Bitmap recyclable4 = BitmapFactory.decodeResource(context.getResources(), R.drawable.recyclable4);
        Bitmap recyclable5 = BitmapFactory.decodeResource(context.getResources(), R.drawable.recyclable5);
        Bitmap recyclable6 = BitmapFactory.decodeResource(context.getResources(), R.drawable.recyclable6);
        Bitmap recyclable7 = BitmapFactory.decodeResource(context.getResources(), R.drawable.recyclable7);

        recyclables.add(Bitmap.createScaledBitmap(recyclable1, (int)(GameView.deviceWidth*0.15f), (int)(GameView.deviceWidth*0.12f), false));
        recyclableNames.add("Metal can");
        recyclables.add(Bitmap.createScaledBitmap(recyclable2, (int)(GameView.deviceWidth*0.15f), (int)(GameView.deviceWidth*0.12f), false));
        recyclableNames.add("Glass bottle");
        recyclables.add(Bitmap.createScaledBitmap(recyclable3, (int)(GameView.deviceWidth*0.15f), (int)(GameView.deviceWidth*0.12f), false));
        recyclableNames.add("Cardboard paper");
        recyclables.add(Bitmap.createScaledBitmap(recyclable4, (int)(GameView.deviceWidth*0.15f), (int)(GameView.deviceWidth*0.12f), false));
        recyclableNames.add("Newspaper");
        recyclables.add(Bitmap.createScaledBitmap(recyclable5, (int)(GameView.deviceWidth*0.15f), (int)(GameView.deviceWidth*0.12f), false));
        recyclableNames.add("Plastic container");
        recyclables.add(Bitmap.createScaledBitmap(recyclable6, (int)(GameView.deviceWidth*0.15f), (int)(GameView.deviceWidth*0.12f), false));
        recyclableNames.add("Book");
        recyclables.add(Bitmap.createScaledBitmap(recyclable7, (int)(GameView.deviceWidth*0.15f), (int)(GameView.deviceWidth*0.12f), false));
        recyclableNames.add("Book");

        Bitmap refuse1 = BitmapFactory.decodeResource(context.getResources(), R.drawable.refuse1);
        Bitmap refuse2 = BitmapFactory.decodeResource(context.getResources(), R.drawable.refuse2);
        Bitmap refuse3 = BitmapFactory.decodeResource(context.getResources(), R.drawable.refuse3);
        Bitmap refuse4 = BitmapFactory.decodeResource(context.getResources(), R.drawable.refuse4);
        Bitmap refuse5 = BitmapFactory.decodeResource(context.getResources(), R.drawable.refuse5);
        Bitmap refuse6 = BitmapFactory.decodeResource(context.getResources(), R.drawable.refuse6);
        Bitmap refuse7 = BitmapFactory.decodeResource(context.getResources(), R.drawable.refuse7);
        Bitmap refuse8 = BitmapFactory.decodeResource(context.getResources(), R.drawable.refuse8);
        Bitmap refuse9 = BitmapFactory.decodeResource(context.getResources(), R.drawable.refuse9);

        refuses.add(Bitmap.createScaledBitmap(refuse1, (int)(GameView.deviceWidth*0.15f), (int)(GameView.deviceWidth*0.12f), false));
        refuseNames.add("Plastic cup");
        refuses.add(Bitmap.createScaledBitmap(refuse2, (int)(GameView.deviceWidth*0.15f), (int)(GameView.deviceWidth*0.12f), false));
        refuseNames.add("Plastic bottle");
        refuses.add(Bitmap.createScaledBitmap(refuse3, (int)(GameView.deviceWidth*0.15f), (int)(GameView.deviceWidth*0.12f), false));
        refuseNames.add("Plastic bag");
        refuses.add(Bitmap.createScaledBitmap(refuse4, (int)(GameView.deviceWidth*0.15f), (int)(GameView.deviceWidth*0.12f), false));
        refuseNames.add("Lightbulb");
        refuses.add(Bitmap.createScaledBitmap(refuse5, (int)(GameView.deviceWidth*0.15f), (int)(GameView.deviceWidth*0.12f), false));
        refuseNames.add("Calculator");
        refuses.add(Bitmap.createScaledBitmap(refuse6, (int)(GameView.deviceWidth*0.15f), (int)(GameView.deviceWidth*0.12f), false));
        refuseNames.add("Electronic");
        refuses.add(Bitmap.createScaledBitmap(refuse7, (int)(GameView.deviceWidth*0.15f), (int)(GameView.deviceWidth*0.12f), false));
        refuseNames.add("Electronic");
        refuses.add(Bitmap.createScaledBitmap(refuse8, (int)(GameView.deviceWidth*0.15f), (int)(GameView.deviceWidth*0.12f), false));
        refuseNames.add("Styrofoam");
        refuses.add(Bitmap.createScaledBitmap(refuse9, (int)(GameView.deviceWidth*0.15f), (int)(GameView.deviceWidth*0.12f), false));
        refuseNames.add("Toy");
    }


}
