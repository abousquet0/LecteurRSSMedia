package gep.a20.lecteurrssmedia;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

public class ViewItemActivity extends AppCompatActivity {

    // Attributes
    ImageView image;
    TextView description;
    TextView pubDate;
    TextView title;
    Utilities utility = new Utilities();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_item);
        // Get view elements
        getViewElements();
        // Get data from RssItemParcelable
        getRssItemParcelable();
    }

    /**
     * Get view elements
     */
    private void getViewElements() {
        // get view elements
        image = findViewById(R.id.image_imageUrl);
        description = findViewById(R.id.text_description);
        pubDate = findViewById(R.id.text_pubDate);
        title = findViewById(R.id.text_title);
    }

    /**
     * Get data from RssItemParcelable from previous intent.
     */
    private void getRssItemParcelable() {
        try{
            RssItemParcelable rssItemParcelable = (RssItemParcelable)getIntent().getParcelableExtra(ItemsFeedActivity.PAR_KEY);
            description.setText(rssItemParcelable.getDescription());
            pubDate.setText(rssItemParcelable.getPubDate());
            title.setText(rssItemParcelable.getTitle());
            Drawable drawable = getDrawableFromUrlString(rssItemParcelable.getImage());
            image.setImageDrawable(drawable);
            image.setTag(rssItemParcelable.getLink());
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    /**
     *
     * @param url
     * @return
     */
    private Drawable getDrawableFromUrlString(String url) {
        Drawable drawable = utility.LoadImageFromWebOperations(url);
        return drawable;
    }

    /**
     * Open web browser at the link contain in the view tag
     * @param view view that contains a URL string in the tag
     */
    public void openBrowser(View view){
        String url = (String)view.getTag();
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
    }
}