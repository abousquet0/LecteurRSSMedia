package gep.a20.lecteurrssmedia;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    // Attributes
    public static final String TAG = "MainActivity";
    ListView listViewMain;
    EditText addUrlEditText;
    private MainMenuAdapter mainMenuAdapter;
    String[] sites = {"https://ici.radio-canada.ca/rss/4159",
            "https://ici.radio-canada.ca/rss/1000524",
            "https://ici.radio-canada.ca/rss/7239",
            "https://ici.radio-canada.ca/rss/4163",
            "https://ici.radio-canada.ca/rss/5717",
            "https://visualstudiotalkshow.libsyn.com/rss",
            "http://www.lapresse.ca/actualites/justice-et-faits-divers/rss", // Le rss de la presse ne fonctionne pas ici. À voir.
            "https://feeds.twit.tv/sn.xml",
            "https://feeds.twit.tv/sn_video_hd.xml"};
    //    ArrayList<String> titles;
    //    ArrayList<Integer> itemCounts;
    //    ArrayList<Drawable> images;

    /**
     * CTOR
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listViewMain = (ListView) findViewById(R.id.list_view);
        addUrlEditText = (EditText) findViewById(R.id.addUrl);

        // Set the ListView in MainActivity
        mainMenuAdapter = new MainMenuAdapter(getApplicationContext(), R.layout.listview_item);
        listViewMain.setAdapter(mainMenuAdapter);

//        titles = new ArrayList<>();
//        itemCounts = new ArrayList<>();
//        images = new ArrayList<Drawable>();


        listViewMain.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            }
        });

        // Set the add url button
        Button addUrlButton = (Button) findViewById(R.id.addBtn);
        addUrlButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    // À faire ici pour ajouter de nouveaux URL.
//                    URL url = new URL(addUrlEditText.getText().toString());
                    new ProcessInBackground(MainActivity.this,mainMenuAdapter,true,sites,addUrlEditText).execute();
                } catch (Exception e) {
                    Log.e(TAG + "-addUrlButton", e.getMessage());
                }
            }
        });

        new ProcessInBackground(this,mainMenuAdapter,false,sites,addUrlEditText).execute();
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

    /**
     *
     */

}