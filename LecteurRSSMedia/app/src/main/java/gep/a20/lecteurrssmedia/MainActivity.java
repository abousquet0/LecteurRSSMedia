package gep.a20.lecteurrssmedia;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    // Attributes
    public static final String TAG = "MainActivity";
    ListView listViewMain;
    EditText addUrlEditText;
    private MainMenuAdapter mainMenuAdapter;
    List<String> sites = new ArrayList<>();

    private void initializeDefaultSite(){
        sites.add( "https://ici.radio-canada.ca/rss/4159");
        sites.add( "https://ici.radio-canada.ca/rss/1000524");
        sites.add( "https://ici.radio-canada.ca/rss/7239");
        sites.add( "https://ici.radio-canada.ca/rss/4163");
        sites.add( "https://ici.radio-canada.ca/rss/5717");
        sites.add("https://visualstudiotalkshow.libsyn.com/rss");
        sites.add("https://feeds.twit.tv/sn.xml");
        sites.add("https://feeds.twit.tv/sn_video_hd.xml");
        sites.add("https://www.lapresse.ca/actualites/justice-et-faits-divers/rss");
    }
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
        initializeDefaultSite();
        // Set the ListView in MainActivity
        mainMenuAdapter = new MainMenuAdapter(getApplicationContext(), R.layout.listview_item);
        listViewMain.setAdapter(mainMenuAdapter);

        listViewMain.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent appInfo = new Intent(MainActivity.this, ItemsFeedActivity.class);
                appInfo.putExtra("url", sites.get(i));
                startActivity(appInfo);
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
                    new MainProcessInBackground(MainActivity.this,mainMenuAdapter,true,sites,addUrlEditText).execute();
                } catch (Exception e) {
                    Log.e(TAG + "-addUrlButton", e.getMessage());
                }
            }
        });

        new MainProcessInBackground(this,mainMenuAdapter,false,sites,addUrlEditText).execute();
    }
}