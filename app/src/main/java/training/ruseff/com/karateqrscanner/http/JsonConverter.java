package training.ruseff.com.karateqrscanner.http;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import training.ruseff.com.karateqrscanner.models.User;

public class JsonConverter {

    public User fromStringToUser(String result) {
        final GsonBuilder builder = new GsonBuilder();
        final Gson gson = builder.create();
        return gson.fromJson(result, User.class);
    }

    public ArrayList<User> fromStringToListOfUsers(String result) {
        final GsonBuilder builder = new GsonBuilder();
        final Gson gson = builder.create();
        return gson.fromJson(result, new TypeToken<List<User>>() {
        }.getType());
    }

    public Set<Integer> fromStringToSetOfNumbers(String result) {
        final GsonBuilder builder = new GsonBuilder();
        final Gson gson = builder.create();
        return gson.fromJson(result, new TypeToken<Set<Integer>>() {
        }.getType());
    }
}
