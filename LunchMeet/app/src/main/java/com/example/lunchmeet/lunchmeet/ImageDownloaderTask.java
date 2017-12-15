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
 * Asynchronously downloads a URL from the Internet and executes the given behavior upon completion.
 */
class ImageDownloaderTask extends AsyncTask<String, Void, Bitmap> {
    private final WeakReference<ImageView> imageViewReference;

    /**
     * The constructor that takes in an ImageView.
     * @param imageView The ImageView.
     */
    public ImageDownloaderTask(ImageView imageView) {
        imageViewReference = new WeakReference<ImageView>(imageView);
    }

    /**
     * The behavior to occur in the background, asynchronously. In this case, it downloads the
     * image from the URL into a bitmap.
     * @param params The URL(s) to download.
     * @return
     */
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

    /** The behavior that occurs when the asynchronous task is finished executing. In this case,
     * it modifies the bitmap and loads it into the given ImageView.
     * @param bitmap The bitmap that was loaded asynchronously.
     */
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