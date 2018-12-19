package com.ensi.pierre.rsshandler;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.squareup.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

public class MyRSSsaxHandler extends DefaultHandler {
    private String url = null ;// l'URL du flux RSS à parser
    private Context context;
    private boolean inTitle = false ;
    private boolean inDescription = false ;
    private boolean inItem = false ;
    private boolean inDate = false ;
    private Bitmap image = null ;
    List<RssItem> itemList= new ArrayList<>();
    RssItem currentItem;

    private int numItem = 0;
    private int numItemMax = -1;
    private String TAG = "RSSHandler";

    public MyRSSsaxHandler(Context context) {
        super();
        this.context = context;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void processFeed(int rssItem) {
        try {
            numItem = rssItem;
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser parser = factory.newSAXParser();
            XMLReader reader = parser.getXMLReader();
            reader.setContentHandler(this);
            InputStream inputStream = new URL(url).openStream();
            reader.parse(new InputSource(inputStream));
            Log.d(TAG, "startElement: "+itemList.get(numItem).getImageURL());

            image = getPicasso().load(itemList.get(numItem).getImageURL()).get();
            numItemMax = numItem;



        } catch (SAXException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        switch (qName) {
            case "item":
                currentItem = new RssItem();
                break;
            case "title":
                inTitle = true;
                break;
            case "description":
                inDescription = true;
                break;
            case "pubDate":
                inDate = true;
                break;
            case "enclosure":
                currentItem.setImageURL(attributes.getValue("url"));
                break;
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        switch (qName) {
            case "item" :
                itemList.add(currentItem);
                currentItem = null;
            case "title":
                inTitle = false;
                break;
            case "description":
                inDescription = false;
                break;
            case "pubDate":
                inDate = false;
                break;
            case "enclosure":
                break;
        }
    }

    @Override
    public void characters(char ch[], int start, int length){
        String chars = new String(ch).substring(start,start+length);
        if (inTitle && currentItem!=null) {
            currentItem.setTitle(new StringBuffer(chars));
        }
        if (inDescription  && currentItem!=null) {
            currentItem.setDescription(new StringBuffer(chars));
        }
        if (inDate && currentItem!=null) {
            currentItem.setDate(new StringBuffer(chars));
        }

    }

    private Bitmap getBitmap(String imageURL) {
        try {
            URL url = new URL(imageURL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.setFollowRedirects(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            Log.e(TAG, "getBitmap: ", e);
            return null;
        }
    }

    public int getNumItem() {
        return numItem;
    }

    public Bitmap getImage() {
        return image;
    }


    Picasso getPicasso() {
        Picasso.Builder builder = new Picasso.Builder(context);
        builder.downloader(new OkHttp3Downloader(context));
        return builder.build();
    }
}
