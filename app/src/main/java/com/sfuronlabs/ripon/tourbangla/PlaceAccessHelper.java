package com.sfuronlabs.ripon.tourbangla;

import com.sfuronlabs.ripon.tourbangla.model.Place;

import java.util.ArrayList;

/**
 * Created by Ripon on 7/7/16.
 */
public class PlaceAccessHelper {

    public static ArrayList<Place> places;

    public static void populate(ArrayList<Place> place) {
        places = place;
    }

    public static Place getPlace(int id) {
        return places.get(id-1);
    }
}
