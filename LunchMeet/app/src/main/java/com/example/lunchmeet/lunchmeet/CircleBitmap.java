package com.example.lunchmeet.lunchmeet;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;

/**
 * A class that contains the static method that takes a bitmap and turns it into a circular
 * bitmap.
 */
public class CircleBitmap {
    /**
     * Takes a bitmap and turns it into a circular bitmap. Also adds a counter in the bottom right
     * corner for group size when necessary.
     * @param bitmap The bitmap to be processed.
     * @param subcircle Whether or not the subcircle should exist.
     * @param num The number that should appear in the subcircle.
     * @return
     */
    public static Bitmap getCircleBitmap(Bitmap bitmap, int subcircle, String num) {
        final Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
                bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        final Canvas canvas = new Canvas(output);

        final int color = Color.RED;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawOval(rectF, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        paint.setTextSize(50);
        //paint.setColor(Color.BLACK);
        if(subcircle==1)
            canvas.drawText(num, (float)25, (float)55, paint );

        bitmap.recycle();

        return output;
    }
}
