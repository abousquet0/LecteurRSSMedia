package gep.a20.lecteurrssmedia;

import android.content.Intent;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import java.util.List;

public class ItemsFeedActivity extends AppCompatActivity {

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
        String url = intent.getStringExtra("url");

        new ItemsFeedProcessInBackground(this,itemsFeedAdapter,url).execute();
    }
}