package com.example.android.newsapp;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

class HttpQueryUtils {

    private static final String TAG = HttpQueryUtils.class.getSimpleName();
    private static final int CONNECT_TIMEOUT = 10000; //milliseconds
    private static final int READ_TIMEOUT = 15000; //milliseconds

    public HttpQueryUtils() {
    }

    public List<Article> queryUrl(String url) {
        URL goodUrl = formatUrl(url);
        if (goodUrl == null) {
            return null;
        }

        try {
            HttpURLConnection connection = (HttpURLConnection) goodUrl.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(CONNECT_TIMEOUT);
            connection.setReadTimeout(READ_TIMEOUT);
            connection.connect();
            InputStream in = connection.getInputStream();
            String jsonResponse = convertStream(in);
            return parseJSON(jsonResponse);
        } catch (IOException e) {
            Log.e(TAG, "Error making HTTP request", e);
            return null;
        }
    }

    private URL formatUrl(String stringUrl) {
        //Make URL object from string url
        URL url;
        try {
            url = new URL(stringUrl);
            return url;
        } catch (MalformedURLException e) {
            Log.e(TAG, "Error creating URL", e);
            return null;
        }
    }

    //Convert input stream to string
    private String convertStream(InputStream in) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        StringBuilder sb = new StringBuilder();
        String line;

        //Add each line from the reader to the StringBuilder.
        while ((line = reader.readLine()) != null) {
            sb.append(line).append("/n");
        }
        reader.close();

        //return response String
        return sb.toString();
    }

    //Parse the JSON response from the server.
    private ArrayList<Article> parseJSON(String response) {
        if (response == null) {
            return null;
        }
        ArrayList<Article> articles = new ArrayList<>();
        String articleTitle;
        String articleTopic;
        String articlePublishDate;
        String articleUrl;
        String byLine;
        String trailText;
        try {
            //Extract the base JSON objects
            JSONObject root = new JSONObject(response).getJSONObject("response");
            JSONArray results = root.getJSONArray("results");

            //Extract articles from json array and create Article objects and add them to articles list
            for (int item = 0; item < results.length(); item++) {
                JSONObject articleObject = results.getJSONObject(item);
                articleTitle = articleObject.optString("webTitle");
                articleTopic = articleObject.optString("sectionName");
                articlePublishDate = articleObject.optString("webPublicationDate");
                articleUrl = articleObject.optString("webUrl");

                //Get fields
                JSONObject fields = articleObject.getJSONObject("fields");
                byLine = fields.optString("byline", "unavailable");
                trailText = fields.optString("trailText");

                articles.add(new Article(
                        articleTitle, articlePublishDate,
                        articleTopic, articleUrl,
                        byLine, trailText));
            }

            // Return the list of Article objects
            return articles;

        } catch (JSONException e) {
            Log.e(TAG, "Error parsing JSON", e);
            return null;
        }
    }
}
