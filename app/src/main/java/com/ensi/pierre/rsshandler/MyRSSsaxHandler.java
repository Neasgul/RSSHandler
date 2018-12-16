package com.ensi.pierre.rsshandler;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.squareup.picasso.Picasso;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

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
    private String imageURL = null ;
    private StringBuffer title = new StringBuffer();
    private StringBuffer description = new StringBuffer();
    private StringBuffer date = new StringBuffer();
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

    public void processFeed() {
        try {
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser parser = factory.newSAXParser();
            XMLReader reader = parser.getXMLReader();
            reader.setContentHandler(this);
            InputStream inputStream = new URL(url).openStream();
            reader.parse(new InputSource(inputStream));
            image = Picasso.with(context).load(imageURL).get();
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
        Log.d(TAG, "startElement: "+qName + ", "+ attributes.getLength());
        switch (qName) {
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
                imageURL = attributes.getValue("url");
                inItem = true;
                break;
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        Log.d(TAG, "endElement: "+qName);
        switch (qName) {
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
                inItem = false;
                break;
        }
    }

    @Override
    public void characters(char ch[], int start, int length){
        String chars = new String(ch).substring(start,start+length);
        if (inTitle) {
            title = new StringBuffer(chars);
        }
        if (inDescription) {
            description = new StringBuffer(chars);
        }
        if (inDate) {
            date = new StringBuffer(chars);
        }

    }

    private Bitmap getBitmap(String imageURL) {

        try {
            URL url = new URL(imageURL);
            InputStream is = url.openStream();
            BufferedInputStream buf = new BufferedInputStream(is, 1024);
            BitmapFactory.Options options=new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            Bitmap bitmap = BitmapFactory.decodeStream(buf,null,options);
            return bitmap;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    public Bitmap getImage() {
        return image;
    }

    public String getTitle() {
        return title.toString();
    }

    public String getDescription() {
        return description.toString();
    }

    public String getDate() {
        return date.toString();
    }

    public int getNumItem() {
        return numItem;
    }
}
