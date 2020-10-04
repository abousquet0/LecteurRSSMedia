package gep.a20.lecteurrssmedia;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class ItemsFeedActivity extends AppCompatActivity {

    public  final static String PAR_KEY = "gep.a20.objectPass.par";
    ItemsFeedAdapter itemsFeedAdapter;
    ListView listViewItemsFeed;
    String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_items_feed);

        listViewItemsFeed = findViewById(R.id.listviewItemsFeed);
        itemsFeedAdapter = new ItemsFeedAdapter(getApplicationContext(), R.layout.listview_item_nobutton);
        listViewItemsFeed.setAdapter(itemsFeedAdapter);

        Intent intent = getIntent();
        url = intent.getStringExtra("url");

        RssViewItem.rssUrlView = url; // Important to run ViewItemProcessInBackground

        new ItemsFeedProcessInBackground(this,itemsFeedAdapter,url).execute();

        // Open ViewItemActivity for the clicked item
        listViewItemsFeed.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                // Get good stuff from clicked itemsFeedAdapter
                String title = (String)((TextView)view.findViewById(R.id.Title_nobutton)).getText();
                RssViewItem.titleView = title;
                startViewItemActivity();
            }
        });
    }

    private void startViewItemActivity() {
        startActivity(new Intent(this, ViewItemActivity.class));
    }
}