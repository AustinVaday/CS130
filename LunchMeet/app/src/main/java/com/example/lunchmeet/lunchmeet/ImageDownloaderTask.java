package com.example.lunchmeet.lunchmeet;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import java.lang.ref.WeakReference;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Brian on 12/13/2017.
 */

class ImageDownloaderTask extends AsyncTask<String, Void, Bitmap> {
    private final WeakReference<ImageView> imageViewReference;

    public ImageDownloaderTask(ImageView imageView) {
        imageViewReference = new WeakReference<ImageView>(imageView);
    }

    @Override
    protected Bitmap doInBackground(String... params) {
        try {
            URL url = new URL(params[0]);
            return BitmapFactory.decodeStream(url.openConnection().getInputStream());
        } catch(Exception e){
            Log.e("ImageDownloaderTask",e.getMessage());
        }
        return null;
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        if (isCancelled()) {
            bitmap = null;
        }

        if (imageViewReference != null) {
            ImageView imageView = imageViewReference.get();
            Bitmap bm = CircleBitmap.getCircleBitmap(bitmap,0,"0");
            bm = Bitmap.createScaledBitmap(bm, 100, 100, true);
            imageView.setImageBitmap(bm);
        }
    }
}