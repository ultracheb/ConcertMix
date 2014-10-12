package com.example.ConcertMix;

import android.os.Environment;
import android.os.StrictMode;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by Vladislove on 12.10.2014.
 */
public class XMLParser {


    private ArrayList<Event> events = new ArrayList<Event>();

    public XMLParser() {
    }

    public void createXML(String artist) {
        try {
            artist = artist.replace(" ", "%20");
            URL url = new URL("http://api.bandsintown.com/artists/"+artist+"/events.xml");
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.connect();

            File SDCardRoot = Environment.getExternalStorageDirectory();
            File file = new File(SDCardRoot,"input.xml");
            FileOutputStream fileOutput = new FileOutputStream(file);
            InputStream inputStream = urlConnection.getInputStream();
            int downloadedSize = 0;

            byte[] buffer = new byte[1024];
            int bufferLength;

            while ( (bufferLength = inputStream.read(buffer)) > 0 ) {
                fileOutput.write(buffer, 0, bufferLength);
                downloadedSize += bufferLength;
            }
            fileOutput.close();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<Event> parseXML(String fileName) {
        XmlPullParserFactory factory;
        XmlPullParser parser;
        Event e = null;
        Boolean venue = false;
        String text ="";
        try {
            factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);
            parser = factory.newPullParser();
            File file = new File(Environment.getExternalStorageDirectory()+"/input.xml");
            FileInputStream fis = new FileInputStream(file);
            parser.setInput(new InputStreamReader(fis));

            int eventType = parser.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                String tagname = parser.getName();
                switch (eventType) {
                    case XmlPullParser.START_TAG:
                        if (tagname.equalsIgnoreCase("event")) {
                            // create a new instance of employee
                            e = new Event();
                        }
                        else if (tagname.equalsIgnoreCase("venue")) {
                            venue = true;
                        }
                        break;

                    case XmlPullParser.TEXT:
                        text = parser.getText();
                        break;

                    case XmlPullParser.END_TAG:
                        if (tagname.equalsIgnoreCase("event")) {
                            // add employee object to list
                            events.add(e);
                            venue = false;
                        } else if (tagname.equalsIgnoreCase("datetime")) {
                            e.setDate(text);
                        } else if (tagname.equalsIgnoreCase("url") ) {
                            if(venue)
                                e.setUrl(new URL(text));
                        } else if (tagname.equalsIgnoreCase("name")) {
                            if(venue)
                                e.setName(text);
                        }
                        else if (tagname.equalsIgnoreCase("city")) {
                            if(venue)
                                e.setCity(text);
                        }
                        else if (tagname.equalsIgnoreCase("country")) {
                            if(venue)
                                e.setCountry(text);
                        }
                        break;

                    default:
                        break;
                }
                eventType = parser.next();
            }

        } catch (XmlPullParserException e1) {
            e1.printStackTrace();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return events;
    }

}
