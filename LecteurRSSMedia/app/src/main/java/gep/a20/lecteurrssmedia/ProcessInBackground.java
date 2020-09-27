package gep.a20.lecteurrssmedia;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.EditText;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import static gep.a20.lecteurrssmedia.MainActivity.TAG;

public class ProcessInBackground extends AsyncTask<Integer, Void, Exception> {
    // Attributes
    ProgressDialog progressDialog;
    Exception exception = null;
    boolean isAddingUrl = false;
    String[] sitesToAdd;
    MainActivity mainActivity;
    ArrayList<String> titles;
    ArrayList<Integer> itemCounts;
    ArrayList<Drawable> images;
    MainMenuAdapter mainMenuAdapter;
    EditText addUrlEditText;
    /**
     * CTOR : Called each time the ListView needs to be updated.
     * @param AddingUrl True if RssFeed object has been added.
     */
    public ProcessInBackground(MainActivity mainAct,MainMenuAdapter adapter,boolean AddingUrl, String[] sites, EditText addUrlET) {
        titles = new ArrayList<>();
        itemCounts = new ArrayList<>();
        images = new ArrayList<Drawable>();
        sitesToAdd = sites;
        mainActivity = mainAct;
        isAddingUrl = AddingUrl;
        mainMenuAdapter = adapter;
        progressDialog = new ProgressDialog(mainActivity);
        addUrlEditText = addUrlET;
        if (isAddingUrl) {
            sitesToAdd = new String[]{addUrlEditText.getText().toString()};
        }
    }

    /**
     * Called while ListView is in building process to display messages.
     */
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressDialog.setMessage("Busy loading rss feed...please wait.");
        progressDialog.show();
    }

    @Override
    protected Exception doInBackground(Integer... params) {
        try {
            for (int i = 0; i < sitesToAdd.length; i++) {
                URL url = new URL(sitesToAdd[i]);
                XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                factory.setNamespaceAware(false);
                XmlPullParser xpp = factory.newPullParser();
                xpp.setInput(mainActivity.getInputStream(url), "UTF-8");
                boolean insideItem = false;
                int itemCount = 0;
                int eventType = xpp.getEventType();

                while (eventType != XmlPullParser.END_DOCUMENT) {
                    if (eventType == XmlPullParser.START_TAG) {
                        String test = xpp.getName();
                        if (xpp.getName().equalsIgnoreCase("image")) {
                            insideItem = true;
                        } else if (xpp.getName().equalsIgnoreCase("title")) {
                            if (insideItem) {
                                titles.add(xpp.nextText());
                            }
                        } else if (xpp.getName().equalsIgnoreCase("url")) {
                            String imageUrl = xpp.nextText();
                            Drawable drawable = LoadImageFromWebOperations(imageUrl);
                            images.add(drawable);
                        }

                        if (xpp.getName().equalsIgnoreCase("item")) {
                            itemCount++;
                        }
                    } else if (eventType == XmlPullParser.END_TAG && xpp.getName().equalsIgnoreCase("image")) {
                        insideItem = false;
                    }
                    eventType = xpp.next();
                }
                itemCounts.add(itemCount);
            }
        } catch (MalformedURLException e) {
            exception = e;
        } catch (XmlPullParserException e) {
            exception = e;
        } catch (IOException e) {
            exception = e;
        } catch (Exception e) {
            exception = e;
        }

        return exception;
    }

    @Override
    protected void onPostExecute(Exception e) {
        super.onPostExecute(e);

        for (int i = 0; i < titles.size(); i++) {
            Drawable image = images.get(i);
            Bitmap bmp = drawableToBitmap(image);
            RssFeed rssFeed = new RssFeed(bmp, titles.get(i), String.valueOf(itemCounts.get(i)));
            mainMenuAdapter.add(rssFeed);
        }
        if (isAddingUrl) {
            addUrlEditText.setText("");
        }
        progressDialog.dismiss();
    }

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
}