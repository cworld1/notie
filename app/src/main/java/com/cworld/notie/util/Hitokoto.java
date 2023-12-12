package com.cworld.notie.util;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Hitokoto {
    static int maxLength = 16;
    static String apiUrl = "https://v1.hitokoto.cn?max_length=" + maxLength;

    public static String fetchPoem() {
        StringBuilder result = new StringBuilder();

        try {
            URL url = new URL(apiUrl + "&c=i");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(connection.getInputStream()));

                String line;
                while ((line = reader.readLine()) != null) {
                    result.append(line);
                }

                reader.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return parseHitokotoJson(result.toString());
    }

    private static String parseHitokotoJson(String resultJson) {
        try {
            JSONObject jsonObject = new JSONObject(resultJson);
            return jsonObject.getString("hitokoto");
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
}
