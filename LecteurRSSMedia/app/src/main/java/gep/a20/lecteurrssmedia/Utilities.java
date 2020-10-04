package gep.a20.lecteurrssmedia;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import static gep.a20.lecteurrssmedia.MainActivity.TAG;

public class Utilities {

    public Drawable LoadImageFromWebOperations(String url) {
        try {
            InputStream is = (InputStream) new URL(url).getContent();
            Drawable d = Drawable.createFromStream(is, "src name");
            return d;
        } catch (Exception e) {
            Log.e(TAG + "-LoadImageFromWebOperations", e.getMessage());
            return null;
        }
    }

    public Bitmap drawableToBitmap(Drawable drawable) {
        Bitmap bitmap = null;

        if (drawable instanceof BitmapDrawable) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            if (bitmapDrawable.getBitmap() != null) {
                return bitmapDrawable.getBitmap();
            }
        }
        if (drawable.getIntrinsicWidth() <= 0 || drawable.getIntrinsicHeight() <= 0) {
            bitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888); // Single color bitmap will be created of 1x1 pixel
        } else {
            bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        }

        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bitmap;
    }

    /**
     * Get an InputStream from an Url object
     * @param url
     * @return InputStream
     */
    public InputStream getInputStream(URL url) {
        try {
            return url.openConnection().getInputStream();
        } catch (IOException e) {
            Log.e(TAG + "-getInputStream", e.getMessage());
            return null;
        }
    }


    public String getNodeValue(String tag, XmlPullParser xpp) throws IOException, XmlPullParserException {
        int eventType = xpp.getEventType();
        String returnValue = "";
        try{
            boolean continueLoop = true;
            while(continueLoop){
                xpp.next();

                if(xpp.getText() != null)
                    returnValue += xpp.getText();

                if(xpp.getName() != null && xpp.getName().equalsIgnoreCase(tag))
                    continueLoop = false;
            }
        }catch(Exception ex){

        }
        return returnValue;
    }


}
