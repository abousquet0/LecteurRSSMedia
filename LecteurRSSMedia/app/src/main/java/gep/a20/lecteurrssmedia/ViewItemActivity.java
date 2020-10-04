package gep.a20.lecteurrssmedia;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.Base64;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

public class ViewItemActivity extends AppCompatActivity {

    // Attributes
    private ImageView image;
    private WebView description;
    private TextView pubDate;
    private TextView title;

    Utilities utility = new Utilities();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_item);
        // Get view elements
        getViewElements();
        // Set RssViewItem and view elements when ready
        new ViewItemProcessInBackground(this).execute();
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
     * Set view elements based on RssViewItem
     */
    public void setViewElements() {

        String unencodedHtml ="<html><body>" + RssViewItem.descriptionView +"</body></html>";
        String encodedHtml = Base64.encodeToString(unencodedHtml.getBytes(), Base64.NO_PADDING);
        description.loadData(encodedHtml, "text/html", "base64");

        image.setImageBitmap(RssViewItem.imageView);
        image.setTag(RssViewItem.linkImageView);

        pubDate.setText(RssViewItem.pubDateView);
        title.setText(RssViewItem.titleView);
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