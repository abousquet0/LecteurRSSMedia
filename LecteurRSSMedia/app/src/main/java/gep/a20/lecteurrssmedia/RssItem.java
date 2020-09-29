package gep.a20.lecteurrssmedia;

import android.graphics.Bitmap;

public class RssItem extends RssFeed{

    String link;

    public RssItem(Bitmap img, String titleP, String descriptionP,String url) {
        super(img, titleP, descriptionP);
        link = url;
    }
}
