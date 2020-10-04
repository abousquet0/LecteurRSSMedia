package gep.a20.lecteurrssmedia;

import android.graphics.Bitmap;

/**
 * This class has the responsibility to contains the RSS Feed definition.
 */
public class RssFeed {
    // Attributes
    Bitmap image = Bitmap.createBitmap(2, 2, Bitmap.Config.ARGB_8888);
    String title = "";
    String description = "";

    // CTOR
    public RssFeed(Bitmap img, String titleP, String descriptionP) {
        image = img;
        title = titleP;
        description = descriptionP;
    }

    // Methods
    public Bitmap getImage() { return image; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
}
