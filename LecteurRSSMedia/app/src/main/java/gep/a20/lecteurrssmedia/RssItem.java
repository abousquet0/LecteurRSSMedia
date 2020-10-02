package gep.a20.lecteurrssmedia;

import android.graphics.Bitmap;

public class RssItem extends RssFeed{

    // Attributes
    String link;
    String imageUrl;
    String pubDate;

    // CTOR
    public RssItem(Bitmap img, String titleP, String descriptionP,String link, String imageUrl, String pubDate) {
        super(img, titleP, descriptionP);
        this.link = link;
        this.imageUrl = imageUrl;
        this.pubDate = pubDate;
    }

    // Methods
    public String getLink() { return link; }
    public String getImageUrl() { return imageUrl; }
    public String getPubDate() { return description; }
}
