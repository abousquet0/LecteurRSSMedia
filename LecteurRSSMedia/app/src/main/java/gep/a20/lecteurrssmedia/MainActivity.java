package gep.a20.lecteurrssmedia;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    ListView listViewMain;
    private MainMenuAdapter mainMenuAdapter;
    String[] sites = { "https://ici.radio-canada.ca/rss/4159",
            "https://ici.radio-canada.ca/rss/1000524",
            "https://ici.radio-canada.ca/rss/7239",
            "https://ici.radio-canada.ca/rss/4163",
            "https://ici.radio-canada.ca/rss/5717",
            "https://visualstudiotalkshow.libsyn.com/rss",
            "http://www.lapresse.ca/actualites/justice-et-faits-divers/rss", // Le rss de la presse ne fonctionne pas ici. À voir.
            "https://feeds.twit.tv/sn.xml",
            "https://feeds.twit.tv/sn_video_hd.xml"};


    ArrayList<String> titles;
    ArrayList<Integer> itemCounts;
    ArrayList<Drawable> images;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listViewMain = (ListView) findViewById(R.id.list_view);

        // TEST
        mainMenuAdapter = new MainMenuAdapter(getApplicationContext(), R.layout.listview_item);
        listViewMain.setAdapter(mainMenuAdapter);

        titles = new ArrayList<>();
        itemCounts = new ArrayList<>();
        images = new ArrayList<Drawable>();


        listViewMain.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            }
        });

        new ProcessInBackground().execute();
    }

    public InputStream getInputStream(URL url) {
        try {
            return url.openConnection().getInputStream();
        }
        catch (IOException e) {
            return null;
        }
    }

    public class ProcessInBackground extends AsyncTask<Integer, Void, Exception>
    {
        ProgressDialog progressDialog = new ProgressDialog(MainActivity.this);
        Exception exception = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.setMessage("Busy loading rss feed...please wait.");
            progressDialog.show();
        }

        @Override
        protected Exception doInBackground(Integer... params) {
            try {
                for (int i = 0; i < sites.length; i ++) {
                    URL url = new URL(sites[i]);
                    XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                    factory.setNamespaceAware(false);
                    XmlPullParser xpp =  factory.newPullParser();
                    xpp.setInput(getInputStream(url), "UTF-8");
                    boolean insideItem = false;
                    int itemCount = 0;
                    int eventType = xpp.getEventType();

                    while (eventType != XmlPullParser.END_DOCUMENT) {
                        if (eventType == XmlPullParser.START_TAG) {
                            String test = xpp.getName();
                            if (xpp.getName().equalsIgnoreCase("image")) {
                                insideItem = true;
                            }
                            else if (xpp.getName().equalsIgnoreCase("title")) {
                                if (insideItem) {
                                    titles.add(xpp.nextText());
                                }
                            }
                            else if (xpp.getName().equalsIgnoreCase("url")) {
                                String imageUrl = xpp.nextText();
                                Drawable drawable = LoadImageFromWebOperations(imageUrl);
                                images.add(drawable);
                            }

                            if (xpp.getName().equalsIgnoreCase("item")) {
                                itemCount++;
                            }
                        }
                        else if (eventType == XmlPullParser.END_TAG && xpp.getName().equalsIgnoreCase("image")) {
                            insideItem = false;
                        }
                        eventType = xpp.next();
                    }
                    itemCounts.add(itemCount);
                }
            }
            catch (MalformedURLException e) {
                exception = e;
            }
            catch (XmlPullParserException e)
            {
                exception = e;
            }
            catch (IOException e)
            {
                exception = e;
            }
            catch (Exception e)
            {
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
                RssFeed rssFeed = new RssFeed(bmp, titles.get(i), String.valueOf(itemCounts.get(0)));
                mainMenuAdapter.add(rssFeed);

            }
            progressDialog.dismiss();
        }

        public Drawable LoadImageFromWebOperations(String url) {
            try {
                InputStream is = (InputStream) new URL(url).getContent();
                Drawable d = Drawable.createFromStream(is, "src name");
                return d;
            } catch (Exception e) {
                return null;
            }
        }

        public Bitmap drawableToBitmap (Drawable drawable) {
            Bitmap bitmap = null;

            if (drawable instanceof BitmapDrawable) {
                BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
                if(bitmapDrawable.getBitmap() != null) {
                    return bitmapDrawable.getBitmap();
                }
            }
            if(drawable.getIntrinsicWidth() <= 0 || drawable.getIntrinsicHeight() <= 0) {
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
}