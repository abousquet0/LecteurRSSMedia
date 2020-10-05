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
import java.util.ArrayList;
import java.util.List;

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
     *
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
            List<String> mediaLinks = new ArrayList<>();

            // Default image if null
            RssViewItem.imageView = Bitmap.createBitmap(2, 2, Bitmap.Config.ARGB_8888);

            // Browse RSS to find the good title and related data
            while (eventType != XmlPullParser.END_DOCUMENT) {
                // Find the good title
                if (eventType == XmlPullParser.START_TAG && !isTitleFind) {
                    tag = xpp.getName();
                    if (tag.equalsIgnoreCase("title")) {
                        lastTitleFind = Html.fromHtml(utility.getNodeValue("title", xpp)).toString();
                        if (lastTitleFind.equalsIgnoreCase(RssViewItem.titleView))
                            isTitleFind = true;
                    }
                }
                // Get good stuff
                else if (isTitleFind && eventType == XmlPullParser.START_TAG) {
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
                            image = getImageEnclosureTag(xpp);
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
                            String media = findIfMediaLink(tag, xpp);
                            if (media != "")
                                mediaLinks.add(media);
                            break;
                    }
                }
                // Exit condition
                if (eventType == XmlPullParser.END_TAG && xpp.getName().equalsIgnoreCase("item") && isTitleFind) {
                    break;
                } else
                    eventType = xpp.next();
            }
            // Add an extra link to web page
            addExtraWebPagelinkInDescription();
            // Add .mp3 link
            addMp3LinkToTheDescription(mediaLinks);
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

    /**
     * Finish to set data into the viewItemActivity
     *
     * @param e exception expected by parent class
     */
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

    /**
     * Get article image from RSS itune style
     *
     * @param xpp
     * @return
     */
    private Drawable getImageItunesTag(XmlPullParser xpp) {
        String url = xpp.getAttributeValue(null, "href");
        Drawable drawable = utility.LoadImageFromWebOperations(url);
        return drawable;
    }

    /**
     * Get article image from more or less standard RSS using enclosure tag
     *
     * @param xpp
     * @return
     */
    private Drawable getImageEnclosureTag(XmlPullParser xpp) {
        String type = xpp.getAttributeValue(null, "type");
        if (type.contains("image")) {
            String url = xpp.getAttributeValue(null, "url");
            Drawable drawable = utility.LoadImageFromWebOperations(url);
            return drawable;
        }
        return null;
    }

    /**
     * Get url for .mp3 files to play
     *
     * @param tag
     * @param xpp
     * @return String url to play a .mp3 file
     * @throws IOException
     * @throws XmlPullParserException
     */
    private String findIfMediaLink(String tag, XmlPullParser xpp) throws IOException, XmlPullParserException {
        int eventType = xpp.getEventType();
        String returnValue = "";
        try {
            boolean continueLoop = true;
            while (continueLoop) {
                xpp.next();

                if (xpp.getText() != null && xpp.getText().contains(".mp3"))
                    returnValue += xpp.getText();

                if (xpp.getName() != null && xpp.getName().equalsIgnoreCase(tag))
                    continueLoop = false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return returnValue;
    }

    /**
     * Add an extra link to web page in the description displayed in the WebView
     */
    private void addExtraWebPagelinkInDescription() {
        RssViewItem.descriptionView += "<a href='" + RssViewItem.linkImageView + "' class='btn btn-info btn-lg'>" +
                "<span class='glyphicon glyphicon-globe'></span> Web</a>";
    }

    private void addMp3LinkToTheDescription(List<String> mediaLinks) {
        int audioCounter = 0;
        for (String media : mediaLinks) {
            audioCounter++;
            RssViewItem.descriptionView += "<a class='btn btn-success btn-lg' href='" + media + "'><span class='glyphicon glyphicon-play'></span>" + " Play audio" + audioCounter + "</a>";
        }
    }
}
