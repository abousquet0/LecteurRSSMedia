package gep.a20.lecteurrssmedia;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Parcelable RssItem used to pass data to display
 * between ItemFeedActivity and ViewItemActivity.
 */
public class RssItemParcelable implements Parcelable {
    // Private attributes
    private Bitmap image;
    private String description;
    private String link;
    private String title;

    // Public Accessor
    public Bitmap getImage() { return image; }
    public void setImage(Bitmap image) { this.image = image;}
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getLink() { return link; }
    public void setLink(String link) { this.link = link; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    // Parcel Creator -> Required
    public static final Creator<RssItemParcelable> CREATOR = new Creator<RssItemParcelable>() {
        @Override
        public RssItemParcelable createFromParcel(Parcel in)
        {
            RssItemParcelable rssItemParcelable = new RssItemParcelable();
            rssItemParcelable.image = in.readParcelable(Bitmap.class.getClassLoader());
            rssItemParcelable.description = in.readString();
            rssItemParcelable.link = in.readString();
            rssItemParcelable.title = in.readString();
            return rssItemParcelable;
        }

        @Override
        public RssItemParcelable[] newArray(int size) { return new RssItemParcelable[size]; }
    };

    // Used to compare with an other Parcelable  -> Required
    @Override
    public int describeContents() { return 0; }

    // Used to write a RssItemParcelable  -> Required
    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeParcelable(image, i);
        parcel.writeString(description);
        parcel.writeString(link);
        parcel.writeString(title);
    }
}
