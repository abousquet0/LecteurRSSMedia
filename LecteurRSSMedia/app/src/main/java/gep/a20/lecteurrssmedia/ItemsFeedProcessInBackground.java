package gep.a20.lecteurrssmedia;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.widget.EditText;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class ItemsFeedProcessInBackground extends AsyncTask<Integer, Void, Exception> {
    // Attributes
    ProgressDialog progressDialog;
    Exception exception = null;
    ItemsFeedActivity itemsFeedActivity;
    String title;
    Drawable image;
    String description;
    String linkToContent;//Address of the item content
    ItemsFeedAdapter itemsFeedAdapter;
    EditText addUrlEditText;
    int itemCount = 0;
    String urlAddress = ""; //Address of the parent URL
    Utilities utility = new Utilities();

    /**
     * CTOR : Called each time the ListView needs to be updated.
     */
    public ItemsFeedProcessInBackground(ItemsFeedActivity itemsFeedAct, ItemsFeedAdapter adapter,String link) {
        itemsFeedAdapter = adapter;
        itemsFeedActivity = itemsFeedAct;
        progressDialog = new ProgressDialog(itemsFeedActivity);
        urlAddress = link;
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
                URL url = new URL(urlAddress);
                XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                factory.setNamespaceAware(false);
                XmlPullParser xpp = factory.newPullParser();
                xpp.setInput(utility.getInputStream(url), "UTF-8");
                boolean insideItem = false;
                int eventType = xpp.getEventType();

                while (eventType != XmlPullParser.END_DOCUMENT) {
                    if (eventType == XmlPullParser.START_TAG) {
                        String test = xpp.getName();
                        if (xpp.getName().equalsIgnoreCase("image")) {
                            insideItem = true;
                        } else if (xpp.getName().equalsIgnoreCase("title")) {
                            if (insideItem) {
                                title = xpp.nextText();
                            }
                        } else if (xpp.getName().equalsIgnoreCase("url")) {
                            String imageUrl = xpp.nextText();
                            Drawable drawable = utility.LoadImageFromWebOperations(imageUrl);
                            image = drawable;
                        }
                        if (xpp.getName().equalsIgnoreCase("item")) {
                            itemCount++;
                        }
                    } else if (eventType == XmlPullParser.END_TAG && xpp.getName().equalsIgnoreCase("image")) {
                        insideItem = false;
                    }
                    eventType = xpp.next();
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
            Bitmap bmp = utility.drawableToBitmap(image);
            RssFeed rssFeed = new RssFeed(bmp, title, String.valueOf(itemCount));
            itemsFeedAdapter.add(rssFeed);
        progressDialog.dismiss();
    }
}
