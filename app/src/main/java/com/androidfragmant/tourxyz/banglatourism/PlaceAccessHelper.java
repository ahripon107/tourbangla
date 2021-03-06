package com.androidfragmant.tourxyz.banglatourism;

import com.androidfragmant.tourxyz.banglatourism.model.Place;

import java.util.ArrayList;

/**
 * @author Ripon
 */
public class PlaceAccessHelper {

    public static ArrayList<Place> places;

    public static void populate(ArrayList<Place> place) {
        places = place;
    }

    public static Place getPlace(int id) {
        return places.get(id-1);
    }

    public static ArrayList<Place> getPlacesOfDistrict(String district) {
        ArrayList<Place> p = new ArrayList<>();
        for (Place place: places) {
            if (place.getDistrict().toUpperCase().equals(district.toUpperCase())) {
                p.add(place);
            }
        }
        return p;
    }
}
