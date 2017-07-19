package com.example.android.newsapp;

import android.text.TextUtils;
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
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Bogdan on 7/3/2017.
 */

public final class QueryUtils {

    private static final String LOG_TAG = QueryUtils.class.getSimpleName();
    private QueryUtils(){

    }

    public static List<News> fetchNews( String requestUrl){
        URL url = creatUrl(requestUrl);
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem with the HTTP request .", e);
        }
        List<News> news = extractFeaturesFromJson(jsonResponse);
        return news;
    }

    private static URL creatUrl(String stringUrl){
        URL url = null;
        try{
            url = new URL(stringUrl);
        }catch (MalformedURLException e){
            Log.e(LOG_TAG,"Problem with creating URL", e);
        }
        return url;
    }

    private static String makeHttpRequest(URL url)throws IOException{
        String jsonResponse = "";
        if (url == null) {
            return jsonResponse;
        }
        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code" + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the JSON response .", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    private static List<News> extractFeaturesFromJson(String newsJson){
        if (TextUtils.isEmpty(newsJson)){
            return null;
        }
        List<News> news = new ArrayList<>();
        try {
            JSONObject baseJsonResponse = new JSONObject(newsJson);
            JSONObject results = baseJsonResponse.getJSONObject("response");

            JSONArray articlesArray = results.getJSONArray("results");
            for (int i = 0; i<articlesArray.length(); i++){

                JSONObject currentArticle = articlesArray.getJSONObject(i);

                String title = currentArticle.getString("webTitle");
                String section = currentArticle.getString("sectionName");
                String url = currentArticle.getString("webUrl");

                News articles = new News(title,section,url);
                news.add(articles);
                Log.v(LOG_TAG , "Json response "+ title + section + url);

            }
        }catch (JSONException e){
            Log.e(LOG_TAG, " Problem parsing the Json response .", e);
        }
        return news;
    }
}
