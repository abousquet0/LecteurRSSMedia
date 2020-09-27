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

interface BtnClickListener {
    public abstract void onBtnClick(int position);
}

class RssFeed
{
    public RssFeed(Bitmap img, String titleP, String descriptionP) {
        image = img;
        title = titleP;
        description = descriptionP;
    }
    Bitmap image;
    String title;
    String description;

    public Bitmap getImage(){
        return image;
    }
    public String getTitle(){
        return title;
    }
    public String getDescription(){
        return description;
    }
}

public class MainMenuAdapter extends ArrayAdapter<RssFeed> {
    private static final String TAG = "MainMenuAdapter";
    private List<RssFeed> rssFeedList = new ArrayList<RssFeed>();

    static class RssFeedViewHolder {
        ImageView image;
        TextView title;
        TextView description;
    }
    public MainMenuAdapter(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
    }

    @Override
    public void add(RssFeed object) {
        rssFeedList.add(object);
        super.add(object);
    }

    @Override
    public int getCount() {
        return this.rssFeedList.size();
    }

    @Override
    public RssFeed getItem(int index) {
        return this.rssFeedList.get(index);
    }

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

    public Bitmap decodeToBitmap(byte[] decodedByte) {
        return BitmapFactory.decodeByteArray(decodedByte, 0, decodedByte.length);
    }
}
