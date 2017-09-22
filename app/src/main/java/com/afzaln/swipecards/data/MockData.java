package com.afzaln.swipecards.data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by afzal on 2017-08-29.
 */

public class MockData {

    private static Business fCoffee;
    private static Business mMuffin;

    static {
        String[] fCoffeePhotos = new String[]{
                "https://s3-media2.fl.yelpcdn.com/bphoto/lwMl4fcbqttaq1_ycBM4WA/o.jpg"
        };

        fCoffee = new Business("Business",
                "Fahrenheit Coffee",
                "fahrenheit-coffee-toronto",
                4.5,
                "https://www.yelp.com/biz/fahrenheit-coffee-toronto?adjust_creative=Y-1n0lcJp-tgIWHHt29vcw&utm_campaign=yelp_api_v3&utm_medium=api_v3_graphql&utm_source=Y-1n0lcJp-tgIWHHt29vcw",
                Arrays.asList(fCoffeePhotos),
                "$$",
                "4.5 km");

        String[] mMuffinPhotos = new String[]{
                "https://s3-media2.fl.yelpcdn.com/bphoto/PcfKqLLzTDBbAaxIzfzuIg/o.jpg"
        };

        mMuffin = new Business("Business",
                "Mystic Muffin",
                "mystic-muffin-toronto",
                4.5,
                "https://www.yelp.com/biz/mystic-muffin-toronto?adjust_creative=Y-1n0lcJp-tgIWHHt29vcw&utm_campaign=yelp_api_v3&utm_medium=api_v3_graphql&utm_source=Y-1n0lcJp-tgIWHHt29vcw",
                Arrays.asList(mMuffinPhotos),
                "$",
                "4 km");
    }

    public static List<Business> getNearbyBusinesses() {
        List<Business> list = new ArrayList<>();
        list.add(fCoffee);
        list.add(mMuffin);
        list.add(fCoffee);
        list.add(mMuffin);
        list.add(fCoffee);
        list.add(fCoffee);
        list.add(mMuffin);
        list.add(fCoffee);
        list.add(mMuffin);
        list.add(fCoffee);
        list.add(fCoffee);
        list.add(mMuffin);
        list.add(fCoffee);
        list.add(mMuffin);
        list.add(fCoffee);
        list.add(fCoffee);
        list.add(mMuffin);
        list.add(fCoffee);
        list.add(mMuffin);
        list.add(fCoffee);
        list.add(fCoffee);
        list.add(mMuffin);
        list.add(fCoffee);
        list.add(mMuffin);
        list.add(fCoffee);
        list.add(mMuffin);

        return list;
    }
}
