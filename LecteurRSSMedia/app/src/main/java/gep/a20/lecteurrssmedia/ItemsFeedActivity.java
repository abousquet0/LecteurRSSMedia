package gep.a20.lecteurrssmedia;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
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
        url = intent.getStringExtra("url");

        new ItemsFeedProcessInBackground(this,itemsFeedAdapter,url).execute();

        listViewItemsFeed.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent appInfo = new Intent(ItemsFeedActivity.this, ViewItemActivity.class);
                Intent intent1 = appInfo.putExtra("url", url + ";" + i);
                startActivity(appInfo);
            }
        });
    }
}