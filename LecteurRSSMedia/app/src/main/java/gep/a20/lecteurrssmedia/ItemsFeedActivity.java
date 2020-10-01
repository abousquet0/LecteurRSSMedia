package gep.a20.lecteurrssmedia;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

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

        new ItemsFeedProcessInBackground(this,itemsFeedAdapter,url).execute();

        // Open ViewItemActivity for the clicked item
        listViewItemsFeed.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                // Get good stuff
                ImageView img = adapterView.findViewById(R.id.listview_images_nobutton);
                Bitmap image = ((BitmapDrawable)img.getDrawable()).getBitmap();
                String description = (String)((TextView)adapterView.findViewById(R.id.Description_nobutton)).getText();
                String link = (String)((TextView)adapterView.findViewById(R.id.Description_nobutton)).getText();
                String title = (String)((TextView)adapterView.findViewById(R.id.Title_nobutton)).getText();

                // Create and setRssItemParcelable
                RssItemParcelable rssItemParcelable = new RssItemParcelable();
                rssItemParcelable.setImage(image);
                rssItemParcelable.setDescription(description);
                rssItemParcelable.setLink(link);
                rssItemParcelable.setTitle(title);

                // Create Intent
                Intent intent = new Intent(ItemsFeedActivity.this, ViewItemActivity.class);
                Bundle bundle = new Bundle();
                try{
                    bundle.putParcelable(PAR_KEY, rssItemParcelable);
                }catch (Exception e){
                    e.printStackTrace();
                }
                intent.putExtra("rssItem",bundle);
                startActivity(intent);
            }
        });
    }
}