package com.androidfragmant.tourxyz.banglatourism;

import android.content.Context;
import android.util.Log;

import com.androidfragmant.tourxyz.banglatourism.model.Place;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

/**
 * @author Ripon
 */
public class FileProcessor {
    private Context context;
    private ArrayList<Place> places;

    public FileProcessor(Context context) {
        this.context = context;
        this.places = new ArrayList<>();
    }

    public void writeToFile(String data) {
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput("data.txt", Context.MODE_PRIVATE));
            outputStreamWriter.write(data);
            outputStreamWriter.close();
            places = readFileAndProcess();
            PlaceAccessHelper.populate(places);
        } catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }

    public ArrayList<Place> readFileAndProcess() {
        ArrayList<Place> all = new ArrayList<>();
        String s = readFromFile();
        try {
            JSONObject jsonObject = new JSONObject(s);
            JSONArray jsonArray = jsonObject.getJSONArray("content");

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject object = jsonArray.getJSONObject(i);
                Gson gson = new Gson();
                Place place = gson.fromJson(String.valueOf(object),Place.class);
                all.add(place);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        PlaceAccessHelper.populate(all);
        return all;
    }

    private String readFromFile() {
        String contentOfFile = "";
        try {
            InputStream inputStream = context.openFileInput("data.txt");

            if (inputStream != null) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                StringBuilder stringBuilder = new StringBuilder();

                while ((receiveString = bufferedReader.readLine()) != null) {
                    stringBuilder.append(receiveString);
                }
                inputStream.close();
                contentOfFile = stringBuilder.toString();

            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return contentOfFile;
    }
}
