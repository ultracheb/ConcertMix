package com.example.ConcertMix;

import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Vladislove on 12.10.2014.
 */

public class Event {

    private Date date;
    private String time;
    private URL url;
    private String name;
    private String city;
    private String country;

    public Event() {
    }

    public void setDate(String date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        try {
            this.date = ((Date)format.parse(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public void setUrl(URL url) {
        this.url = url;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setCity(String city) {
        this.city = city;
    }
    public void setCountry(String country) {
        this.country = country;
    }


    @Override
    public String toString() {
        return "Date&time: " + date.toLocaleString() + "\nUrl: " + url
                + "\nName: " + name + "\nCity: " + city + "\nCountry: " + country;
    }

}
