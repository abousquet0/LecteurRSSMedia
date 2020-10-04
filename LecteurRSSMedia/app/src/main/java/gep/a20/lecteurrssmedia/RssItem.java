package gep.a20.lecteurrssmedia;

import android.graphics.Bitmap;

public class RssItem extends RssFeed{

    // Attributes
    String link = "";

    // CTOR
    public RssItem(Bitmap img, String titleP, String descriptionP) {
        super(img, titleP, descriptionP);
    }

    // Methods
    public String getLink() { return link; }
}
