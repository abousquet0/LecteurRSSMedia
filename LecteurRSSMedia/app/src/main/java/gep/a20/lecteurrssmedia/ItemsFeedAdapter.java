package gep.a20.lecteurrssmedia;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import java.util.ArrayList;
import java.util.List;

/**
 * This class has the responsibility to contain the list
 * of RssFeed object to display as RssFeedViewHolder
 */
public class ItemsFeedAdapter extends ArrayAdapter<RssItem> {
    // Attributes
    int layoutSource;
    private List<RssItem> rssItemsList = new ArrayList<RssItem>();

    static class RssItemViewHolder {
        ImageView image;
        TextView title;
        TextView description;
        TextView link;
        TextView imageUrl;
        TextView pubDate;
    }

    /**
     * CTOR
     * @param context
     * @param textViewResourceId
     */
    public ItemsFeedAdapter(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
        layoutSource = textViewResourceId;
    }

    /**
     * @param object Add an RssFeed to the displayed list
     */
    @Override
    public void add(RssItem object) {
        rssItemsList.add(object);
        super.add(object);
    }

    /**
     * @return the count of rssFeedList
     */
    @Override
    public int getCount() {
        return this.rssItemsList.size();
    }

    /**
     * Get RssFeed from rssFeedList
     * @param index knowed index in rssFeedList
     * @return RssFeed from rssFeedList
     */
    @Override
    public RssItem getItem(int index) {
        return this.rssItemsList.get(index);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View row = convertView;
        RssItemViewHolder viewHolder;
        if (row == null) {
            LayoutInflater inflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(layoutSource, parent, false);
            viewHolder = new RssItemViewHolder();
            viewHolder.image = (ImageView) row.findViewById(R.id.listview_images_nobutton);
            viewHolder.title = (TextView) row.findViewById(R.id.Title_nobutton);
            viewHolder.description = (TextView) row.findViewById(R.id.Description_nobutton);
            viewHolder.imageUrl = (TextView) row.findViewById(R.id.ImageUrl_nobutton);
            viewHolder.link = (TextView) row.findViewById(R.id.Link_nobutton);
            viewHolder.pubDate = (TextView) row.findViewById(R.id.PubDate_nobutton);
            row.setTag(viewHolder);
        } else {
            viewHolder = (RssItemViewHolder)row.getTag();
        }

        RssItem rssItem = getItem(position);
        viewHolder.image.setImageBitmap(rssItem.getImage());
        viewHolder.title.setText(rssItem.getTitle());
        viewHolder.imageUrl.setText(rssItem.getImageUrl());
        viewHolder.link.setText(rssItem.getLink());
        viewHolder.pubDate.setText(rssItem.getPubDate());

        String description = rssItem.getDescription();
        if(description.length() > 50)
        description = description.substring(0, 50);//get only first sentence if description is too long
        viewHolder.description.setText(description);

        return row;
    }

    /**
     * Convert byte array to Bitmap object
     * @param decodedByte
     * @return Bitmap
     */
    public Bitmap decodeToBitmap(byte[] decodedByte) {
        return BitmapFactory.decodeByteArray(decodedByte, 0, decodedByte.length);
    }
}
