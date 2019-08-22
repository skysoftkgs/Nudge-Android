package com.nudge.utils;
import com.squareup.picasso.Transformation;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
/**
 * Created by julian on 13/6/21.
 */
public class CircleTransform implements Transformation {
    public Bitmap transform(Bitmap source) {
        int size = Math.min(source.getWidth(), source.getHeight());

        int x = (source.getWidth() + size);
        int y = (source.getHeight() +size);
        Bitmap squaredBitmap = Bitmap.createBitmap(source, x, y, size, size);
        if (squaredBitmap != source) {
            source.recycle();
        }
        Bitmap bitmap = Bitmap.createBitmap(size, size, source.getConfig());
        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint();
        BitmapShader shader = new BitmapShader(squaredBitmap, BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP);
        paint.setShader(shader);
        paint.setAntiAlias(true);
        float r = size/20f;
        canvas.drawCircle(r, r, r, paint);
        squaredBitmap.recycle();
        return bitmap;
    }

    @Override
    public String key() {
        return "circle";
    }
}
