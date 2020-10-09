package gep.a20.lecteurrssmedia;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * This class has the responsibility to contain the list
 * of RssFeed object to display as RssFeedViewHolder
 */
public class MainMenuAdapter extends ArrayAdapter<RssFeed> {
    // Attributes
    int layoutSource;
    ProgressDialog progressDialog;
    private List<RssFeed> rssFeedList = new ArrayList<RssFeed>();

    List<String> sites = new ArrayList<>();
    File fileSites;

    private List<String> initializeDefaultSite() {
        List<String> defaultList = new ArrayList<>();

        defaultList.add("https://ici.radio-canada.ca/rss/4159");
        defaultList.add("https://ici.radio-canada.ca/rss/1000524");
        defaultList.add("https://ici.radio-canada.ca/rss/7239");
        defaultList.add("https://ici.radio-canada.ca/rss/4163");
        defaultList.add("https://ici.radio-canada.ca/rss/5717");
        defaultList.add("https://visualstudiotalkshow.libsyn.com/rss");
        defaultList.add("https://feeds.twit.tv/sn.xml");
        defaultList.add("https://feeds.twit.tv/sn_video_hd.xml");
        defaultList.add("https://www.lapresse.ca/actualites/justice-et-faits-divers/rss");

        return defaultList;
    }

    static class RssFeedViewHolder {
        ImageView image;
        TextView title;
        TextView description;
    }

    /**
     * CTOR
     *
     * @param context
     * @param textViewResourceId
     */
    public MainMenuAdapter(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
        layoutSource = textViewResourceId;
        sites = deserializeSites();
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
     *
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
     *
     * @param position    in rssFeedList
     * @param convertView -> View formated as listview_item
     * @param parent      -> a ListView that contains listview_item
     * @return View -> listview_item
     */
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View row = convertView;
        final RssFeedViewHolder viewHolder;
        if (row == null) {
            LayoutInflater inflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(layoutSource, parent, false);
            viewHolder = new RssFeedViewHolder();
            viewHolder.image = (ImageView) row.findViewById(R.id.listview_images);
            viewHolder.title = (TextView) row.findViewById(R.id.Title);
            viewHolder.description = (TextView) row.findViewById(R.id.Description);
            row.setTag(viewHolder);
        } else {
            viewHolder = (RssFeedViewHolder) row.getTag();
        }

        final RssFeed rssFeed = getItem(position);
        viewHolder.image.setImageBitmap(rssFeed.getImage());
        viewHolder.title.setText(rssFeed.getTitle());
        viewHolder.description.setText(rssFeed.getDescription());

        // Handle buttons and add onClickListeners
        Button deleteBtn = (Button) row.findViewById(R.id.removeButton);

        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rssFeedList.remove(position);
                sites.remove(position);
                try {
                    serializeSites(sites);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                notifyDataSetChanged();
            }
        });

        return row;
    }

    /**
     * Convert byte array to Bitmap object
     *
     * @param decodedByte
     * @return Bitmap
     */
    public Bitmap decodeToBitmap(byte[] decodedByte) {
        return BitmapFactory.decodeByteArray(decodedByte, 0, decodedByte.length);
    }

    /**
     * Allows the saving of the new list of URLs when a site is added.
     *
     * @param newListSites The new list of URLs to save.
     */
    public void updateSites(List<String> newListSites) {
        try {
            sites = newListSites;
            serializeSites(sites);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Serialize the list of URLs.
     *
     * @param sites List of URLs to serialize.
     * @throws IOException Exception thrown when something goes wrong with the file or the serialization.
     */
    public void serializeSites(List<String> sites) throws IOException {
        if (sites.size() == 0) {
            if (fileSites != null && fileSites.exists()) {
                if (!fileSites.delete() && fileSites.exists()) {
                    throw new IOException(getContext().getString(R.string.erreurSuppression) + fileSites);
                }
            }
        } else {
            FileOutputStream fOut = null;
            ObjectOutputStream oOut = null;
            Context ctx = getContext();

            try {
                fOut = new FileOutputStream(ctx.getFilesDir() + File.separator + "RssUrls.txt");
                oOut = new ObjectOutputStream(fOut);
                oOut.writeObject(sites);
                oOut.flush();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    oOut.close();
                    fOut.close();
                } catch (IOException e) {
                    progressDialog.setMessage(ctx.getString(R.string.erreurSauvegarde));
                    progressDialog.show();
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Deserialize the file containing the list of URLs if it exists.
     * Else, use the default list of URLs.
     *
     * @return List of URLs depending on the status of the file.
     */
    public List<String> deserializeSites() {
        Context ctx = getContext();
        //File dir = context.getFilesDir();
        ObjectInputStream in;
        fileSites = new File(ctx.getFilesDir() + File.separator + "RssUrls.txt");
        List<String> list = null;

        if (fileSites != null && fileSites.exists()) {
            FileInputStream fIn = null;
            ObjectInputStream oIn = null;
            try {
                fIn = new FileInputStream((fileSites));
                oIn = new ObjectInputStream(fIn);
                list = (List<String>) oIn.readObject();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } finally {
                try {
                    oIn.close();
                    fIn.close();
                } catch (IOException e) {
                    progressDialog.setMessage(ctx.getString(R.string.erreurChargement));
                    progressDialog.show();
                    e.printStackTrace();
                }
            }
        } else {
            list = initializeDefaultSite();
        }

        return list;
    }
}
