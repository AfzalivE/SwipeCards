package com.afzaln.swipecards.data;

import java.util.List;

/**
 * Created by afzal on 2017-09-21.
 */

public class Business {
    public final String type;
    public final String name;
    public final String id;
    public final double rating;
    public final String url;
    public final List<String> photos;
    public final String price;
    public final String location;

    public Business(String type, String name, String id, double rating, String url, List<String> photos, String price, String location) {

        this.type = type;
        this.name = name;
        this.id = id;
        this.rating = rating;
        this.url = url;
        this.photos = photos;
        this.price = price;
        this.location = location;
    }
}
