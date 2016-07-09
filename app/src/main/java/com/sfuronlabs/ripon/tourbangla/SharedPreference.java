package com.sfuronlabs.ripon.tourbangla;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.sfuronlabs.ripon.tourbangla.model.Place;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Ripon on 9/26/15.
 */
public class SharedPreference {
    public static final String PREFS_NAME = "PRODUCT_APP";
    public static final String FAVORITES = "Product_Favorite";

    public SharedPreference() {
        super();
    }

    // This four methods are used for maintaining favorites.
    public void saveFavorites(Context context, List<Place> favorites) {
        SharedPreferences settings;
        SharedPreferences.Editor editor;

        settings = context.getSharedPreferences(PREFS_NAME,
                Context.MODE_PRIVATE);
        editor = settings.edit();


        Gson gson = new Gson();
        String jsonFavorites = gson.toJson(favorites);
        editor.putString(FAVORITES, jsonFavorites);
        editor.apply();
    }

    public void addFavorite(Context context, Place product) {
        List<Place> favorites = getFavorites(context);
        if (favorites == null)
            favorites = new ArrayList<Place>();
        Place place = new Place(product.getId(), product.getName(), product.getDescription(), product.getHowtogo(), product.getLattitude(), product.getLongitude(), product.getHotel(), product.getOthers(), product.getPicture(), product.getDivision(), product.getDistrict());
        favorites.add(place);
        saveFavorites(context, favorites);
    }

    public void removeFavorite(Context context, Place product) {
        ArrayList<Place> favorites = getFavorites(context);
        if (favorites != null) {
            Place place = new Place(product.getId(), product.getName(), product.getDescription(), product.getHowtogo(), product.getLattitude(), product.getLongitude(), product.getHotel(), product.getOthers(), product.getPicture(), product.getDivision(), product.getDistrict());
            for (int i = 0; i < favorites.size(); i++) {
                if (favorites.get(i).toString().equals(place.toString())) {
                    favorites.remove(i);
                    break;
                }
            }
            saveFavorites(context, favorites);
        }
    }

    public ArrayList<Place> getFavorites(Context context) {
        SharedPreferences settings;
        List<Place> favorites;
        settings = context.getSharedPreferences(PREFS_NAME,
                Context.MODE_PRIVATE);

        if (settings.contains(FAVORITES)) {
            String jsonFavorites = settings.getString(FAVORITES, null);
            Gson gson = new Gson();
            Place[] favoriteItems = gson.fromJson(jsonFavorites,
                    Place[].class);

            favorites = Arrays.asList(favoriteItems);
            favorites = new ArrayList<Place>(favorites);
        } else
            return null;

        return (ArrayList<Place>) favorites;
    }

    public boolean containsObject(Context context, Place place) {
        List<Place> favorites = getFavorites(context);
        if (favorites != null) {
            for (int i = 0; i < favorites.size(); i++) {
                if (place.toString().equals(favorites.get(i).toString()))
                    return true;
            }
        }
        return false;
    }


}


