package gep.a20.lecteurrssmedia;

import android.graphics.Bitmap;

public class RssItem extends RssFeed{

    String url;

    public RssItem(Bitmap img, String titleP, String descriptionP,String link) {
        super(img, titleP, descriptionP);
        url = link;
    }
}
