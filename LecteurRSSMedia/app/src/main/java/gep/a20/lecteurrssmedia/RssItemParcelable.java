package gep.a20.lecteurrssmedia;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Parcelable RssItem used to pass data to display
 * between ItemFeedActivity and ViewItemActivity.
 */
public class RssItemParcelable implements Parcelable {
    // Private attributes
    private String description;
    private String image ;
    private String link;
    private String pubDate;
    private String title;

    //Public Accessor
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getImage () { return image ; }
    public void setImage(String image ) { this.image  = image ;}
    public String getLink() { return link; }
    public void setLink(String link) { this.link = link; }
    public String getPubDate() { return pubDate; }
    public void setPubDate(String pubDate) { this.pubDate = pubDate;}
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    // Parcel Creator -> Required
    public static final Creator<RssItemParcelable> CREATOR = new Creator<RssItemParcelable>() {
        @Override
        public RssItemParcelable createFromParcel(Parcel in)
        {
            RssItemParcelable rssItemParcelable = new RssItemParcelable();
            rssItemParcelable.description = in.readString();
            rssItemParcelable.image = in.readString();
            rssItemParcelable.link = in.readString();
            rssItemParcelable.pubDate = in.readString();
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
        parcel.writeString(description);
        parcel.writeString(image);
        parcel.writeString(link);
        parcel.writeString(pubDate);
        parcel.writeString(title);
    }
}
