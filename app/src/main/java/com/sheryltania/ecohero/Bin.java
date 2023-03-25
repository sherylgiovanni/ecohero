package com.sheryltania.ecohero;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.sheryltania.ecohero.R;

enum BinType {
    GREEN,
    RECYCLE,
    REFUSE
}

public class Bin {
    public Bitmap binImage;
    public BinType type;
    public int binX = 0;
    public int binY = 0;
    
    public Bin(Context context, BinType t) {
        type = t;
        if(t == BinType.GREEN) {
            binImage = BitmapFactory.decodeResource(context.getResources(), R.drawable.green);
        } else if (t == BinType.RECYCLE) {
            binImage = BitmapFactory.decodeResource(context.getResources(), R.drawable.recyclables);
        } else {
            binImage = BitmapFactory.decodeResource(context.getResources(), R.drawable.refuse);
        }
    }

    public Bin getScaledBin(int deviceWidth, int deviceHeight) {
        Bitmap scaledBin = Bitmap.createScaledBitmap(this.binImage, (int)(deviceWidth*0.35f), (int)(deviceHeight*0.18f), false);
        this.binImage = scaledBin;
        return this;
    }

    public void setBinX(int x) {
        binX = x;
    }

    public void setBinY(int y) {
        binY = y;
    }
}
