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

public class MainProcessInBackground extends AsyncTask<Integer, Void, Exception> {
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
    Utilities utility = new Utilities();
    /**
     * CTOR : Called each time the ListView needs to be updated.
     * @param AddingUrl True if RssFeed object has been added.
     */
    public MainProcessInBackground(MainActivity mainAct, MainMenuAdapter adapter, boolean AddingUrl, String[] sites, EditText addUrlET) {
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
                xpp.setInput(utility.getInputStream(url), "UTF-8");
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
                            Drawable drawable = utility.LoadImageFromWebOperations(imageUrl);
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
            Bitmap bmp = utility.drawableToBitmap(image);
            RssFeed rssFeed = new RssFeed(bmp, titles.get(i), String.valueOf(itemCounts.get(i)));
            mainMenuAdapter.add(rssFeed);
        }
        if (isAddingUrl) {
            addUrlEditText.setText("");
        }
        progressDialog.dismiss();
    }
}