package gep.a20.lecteurrssmedia;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.text.Html;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * @author Alexandre Pouliot
 * This class has the responsibility to get data from
 * an item in an RSS file for the ViewItemActivity.
 */
public class ViewItemProcessInBackground extends AsyncTask<Integer, Void, Exception> {
    // Attributes
    ProgressDialog progressDialog;
    Exception exception = null;
    ViewItemActivity viewItemActivity;
    Utilities utility = new Utilities();

    /**
     * CTOR : Called each time the ViewItemActivity.RssItem needs to be updated.
     */
    public ViewItemProcessInBackground(ViewItemActivity viewItemActivity) {
        this.viewItemActivity = viewItemActivity;
        progressDialog = new ProgressDialog(viewItemActivity);
    }

    /**
     * Called while ViewItemActivity.RssItem is in building process to display messages.
     */
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressDialog.setMessage("Loading rss news... please wait.");
        progressDialog.show();
    }

    /**
     * Browse an RSS file to fill the static class RssViewItem
     * with appropriate data to display in RssViewActivity.
     * @param integers
     * @return exception as parent expect
     */
    @Override
    protected Exception doInBackground(Integer... integers) {
        try {
            // scope attribute
            XmlPullParser xpp = getXmlPullParser();
            int eventType = xpp.getEventType();
            boolean isTitleFind = false;
            Drawable image = null;
            String tag = "";
            String lastTitleFind = "";

            // Default image if null
            RssViewItem.imageView = Bitmap.createBitmap(2, 2, Bitmap.Config.ARGB_8888);

            // Browse RSS to find the good title and related data
            while (eventType != XmlPullParser.END_DOCUMENT) {
                if (eventType == XmlPullParser.START_TAG && !isTitleFind) { // Find the good title
                    tag = xpp.getName();
                    if (tag.equalsIgnoreCase("title")) {
                        lastTitleFind = Html.fromHtml(utility.getNodeValue("title", xpp)).toString();
                        if (lastTitleFind.equalsIgnoreCase(RssViewItem.titleView))
                            isTitleFind = true;
                    }
                } else if (isTitleFind && eventType == XmlPullParser.START_TAG) { // Get good stuff
                    tag = xpp.getName();
                    switch (tag) {
                        case "link":
                            RssViewItem.linkImageView = utility.getNodeValue("link", xpp);
                            break;
                        case "description":
                            RssViewItem.descriptionView = utility.getNodeValue("description", xpp);
                            break;
                        case "itunes:summary":
                            RssViewItem.descriptionView = utility.getNodeValue("itunes:summary", xpp);
                            break;
                        case "pubDate":
                            RssViewItem.pubDateView = utility.getNodeValue("pubDate", xpp);
                            break;
                        case "enclosure":
                            image = getImageEnclosureTag("itunes:image", xpp);
                            if (image != null) {
                                RssViewItem.imageView = utility.drawableToBitmap(image);
                            }
                            break;
                        case "itunes:image":
                            image = getImageItunesTag(xpp);
                            if (image != null) {
                                RssViewItem.imageView = utility.drawableToBitmap(image);
                            }
                            break;
                        default:
                            break;
                    }
                }
                if (eventType == XmlPullParser.END_TAG && xpp.getName().equalsIgnoreCase("item") && isTitleFind) { // Exit condition
                    break;
                } else
                    eventType = xpp.next();
            }
        } catch (MalformedURLException e) {
            exception = e;
        } catch (XmlPullParserException e) {
            exception = e;
        } catch (IOException e) {
            exception = e;
        } catch (Exception e) {
            exception = e;
        }

        return exception;
    }

    @Override
    protected void onPostExecute(Exception e) {
        super.onPostExecute(e);
        viewItemActivity.setViewElements();
        progressDialog.dismiss();
    }

    /**
     * Get a new XmlPullParser to get the data for
     * the given title and the given RSS url.
     *
     * @return XmlPullParser set with the url get in CTOR
     * @throws MalformedURLException
     * @throws XmlPullParserException
     */
    private XmlPullParser getXmlPullParser() throws MalformedURLException, XmlPullParserException {
        URL url = new URL(RssViewItem.rssUrlView);
        XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
        factory.setNamespaceAware(false);
        XmlPullParser xpp = factory.newPullParser();
        xpp.setInput(utility.getInputStream(url), "UTF-8");
        return xpp;
    }

    private Drawable getImageItunesTag(XmlPullParser xpp) {
        String url = xpp.getAttributeValue(null, "href");
        Drawable drawable = utility.LoadImageFromWebOperations(url);
        return drawable;
    }

    private Drawable getImageEnclosureTag(String tag, XmlPullParser xpp) {
        String type = xpp.getAttributeValue(null, "type");
        if (type.contains("image")) {
            String url = xpp.getAttributeValue(null, "url");
            Drawable drawable = utility.LoadImageFromWebOperations(url);
            return drawable;
        }
        return null;
    }
}
