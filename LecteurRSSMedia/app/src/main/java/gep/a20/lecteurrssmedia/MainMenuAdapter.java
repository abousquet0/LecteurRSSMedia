package gep.a20.lecteurrssmedia;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.renderscript.ScriptIntrinsicYuvToRGB;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * This class has the responsibility to contain the list
 * of RssFeed object to display as RssFeedViewHolder
 */
public class MainMenuAdapter extends ArrayAdapter<RssFeed> {
    // Attributes
    private static final String TAG = "MainMenuAdapter";
    private List<RssFeed> rssFeedList = new ArrayList<RssFeed>();

    static class RssFeedViewHolder {
        ImageView image;
        TextView title;
        TextView description;
    }

    /**
     * CTOR
     * @param context
     * @param textViewResourceId
     */
    public MainMenuAdapter(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
    }

    /**
     * @param object Add an RssFeed to the displayed list
     */
    @Override
    public void add(RssFeed object) {
        rssFeedList.add(object);
        super.add(object);
    }

    /**
     * @return the count of rssFeedList
     */
    @Override
    public int getCount() {
        return this.rssFeedList.size();
    }

    /**
     * Get RssFeed from rssFeedList
     * @param index knowed index in rssFeedList
     * @return RssFeed from rssFeedList
     */
    @Override
    public RssFeed getItem(int index) {
        return this.rssFeedList.get(index);
    }

    /**
     * Convert RssFeed as a View Object -> listview_item
     * Maybe some refactoring is required.
     * @param position in rssFeedList
     * @param convertView -> View formated as listview_item
     * @param parent -> a ListView that contains listview_item
     * @return View -> listview_item
     */
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View row = convertView;
        RssFeedViewHolder viewHolder;
        if (row == null) {
            LayoutInflater inflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.listview_item, parent, false);
            viewHolder = new RssFeedViewHolder();
            viewHolder.image = (ImageView) row.findViewById(R.id.listview_images);
            viewHolder.title = (TextView) row.findViewById(R.id.Title);
            viewHolder.description = (TextView) row.findViewById(R.id.Description);
            row.setTag(viewHolder);
        } else {
            viewHolder = (RssFeedViewHolder)row.getTag();
        }

        RssFeed rssFeed = getItem(position);
        viewHolder.image.setImageBitmap(rssFeed.getImage());
        viewHolder.title.setText(rssFeed.getTitle());
        viewHolder.description.setText(rssFeed.getDescription());

        //Handle buttons and add onClickListeners
        Button deleteBtn = (Button)row.findViewById(R.id.removeButton);

        deleteBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                rssFeedList.remove(position);
                notifyDataSetChanged();
            }
        });

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
