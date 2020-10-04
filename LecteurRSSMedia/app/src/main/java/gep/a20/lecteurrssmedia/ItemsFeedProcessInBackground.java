package gep.a20.lecteurrssmedia;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.text.Html;
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
    ItemsFeedAdapter itemsFeedAdapter;
    EditText addUrlEditText;
    int itemCount = 0;
    String urlAddress = ""; //Address of the parent URL
    Utilities utility = new Utilities();
    ArrayList<RssItem> items = new ArrayList<RssItem>();

    /**
     * CTOR : Called each time the ListView needs to be updated.
     */
    public ItemsFeedProcessInBackground(ItemsFeedActivity itemsFeedAct, ItemsFeedAdapter adapter, String link) {
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
            String title = "";
            Drawable image = null;
            String description = "";

            //Image vide par defaut si on ne trouve pas l'image
            Bitmap bmp;
            Bitmap.Config conf = Bitmap.Config.ARGB_8888;
            bmp = Bitmap.createBitmap(2, 2, conf);

            while (eventType != XmlPullParser.END_DOCUMENT) {
                if (eventType == XmlPullParser.START_TAG) {
                    String test = xpp.getName();
                    if (xpp.getName().equalsIgnoreCase("item")) {
                        insideItem = true;
                    } else if (xpp.getName().equalsIgnoreCase("title") && insideItem) {
                        if (insideItem) {
                            title = Html.fromHtml(utility.getNodeValue("title", xpp)).toString();
                        }
                    } else if (xpp.getName().equalsIgnoreCase("description") && insideItem) {
                        description = Html.fromHtml(utility.getNodeValue("description", xpp)).toString();
                    } else if (xpp.getName().equalsIgnoreCase("itunes:image") && insideItem) {
                        image = getImageItunesTag("itunes:image", xpp);
                        if (image != null) {
                            bmp = utility.drawableToBitmap(image);
                        }
                    } else if (xpp.getName().equalsIgnoreCase("enclosure") && insideItem) {
                        image = getImageEnclosureTag("itunes:image", xpp);
                        if (image != null) {
                            bmp = utility.drawableToBitmap(image);
                        }
                    }
                    if (xpp.getName().equalsIgnoreCase("item")) {
                        itemCount++;
                    }
                } else if (eventType == XmlPullParser.END_TAG && xpp.getName().equalsIgnoreCase("item") && insideItem) {
                    insideItem = false;
                    RssItem item = new RssItem(bmp, title, description);
                    items.add(item);
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
        for (RssItem item : items) {
            itemsFeedAdapter.add(item);
        }
        progressDialog.dismiss();
    }

    private Drawable getImageItunesTag(String tag, XmlPullParser xpp) {
        String url = xpp.getAttributeValue(null, "href");
        Drawable drawable = utility.LoadImageFromWebOperations(url);
        return drawable;
    }

    private Drawable getImageEnclosureTag(String tag, XmlPullParser xpp) {
        String type = xpp.getAttributeValue(null, "type");
        if (type.contains("image")) {
            String url = xpp.getAttributeValue(null, "url");
            Drawable drawable = utility.LoadImageFromWebOperations(url);
            return drawable;
        }
        return null;
    }

}
