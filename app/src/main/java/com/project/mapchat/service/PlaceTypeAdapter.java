package com.project.mapchat.service;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.project.mapchat.entities.Place;

import java.io.IOException;

public class PlaceTypeAdapter extends TypeAdapter<Place> {
    private Gson gson = new Gson();

    @Override
    public void write(JsonWriter out, Place value) throws IOException {
        out.value(gson.toJson(value));
    }

    @Override
    public Place read(JsonReader in) throws IOException {
        return gson.fromJson(in.nextString(), Place.class);
    }
}
