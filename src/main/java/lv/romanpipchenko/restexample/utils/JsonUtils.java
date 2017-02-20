package lv.romanpipchenko.restexample.utils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import lv.romanpipchenko.restexample.beans.Cat;

import java.io.*;
import java.lang.reflect.Type;
import java.util.List;

public class JsonUtils {

    private static JsonReader jsonReader;
    private static Type listOfTestObject = new TypeToken<List<Cat>>() {
    }.getType();

    public static void main(String[] args) throws Exception {
        Gson gson = new Gson();
        JsonReader jsonReader = new JsonReader(new FileReader("src/main/resources/cats.json"));
        Type listOfTestObject = new TypeToken<List<Cat>>() {
        }.getType();

        System.out.println("111" + jsonReader.toString());
        List<Cat> cats = gson.fromJson(jsonReader,
                listOfTestObject);


        try (Writer writer = new FileWriter("src/main/resources/cats_new.json")) {

            gson.toJson(cats, writer);
        }


    }

    public static List<Cat> getCats() {
        Gson gson = new Gson();
        try {
            jsonReader = new JsonReader(new FileReader("src/main/resources/cats.json"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return gson.fromJson(jsonReader, listOfTestObject);
    }

    public static void saveToFile(List<Cat> cats) {
        Gson gson = new Gson();
        try {
            try (Writer writer = new FileWriter("src/main/resources/cats.json")) {

                gson.toJson(cats, writer);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
