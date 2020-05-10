package com.abhinav.newsfeed;

import android.content.Context;
import android.util.Log;

import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class NewsDownloadHelper {

    private Context context;

    public NewsDownloadHelper( Context context ){
        this.context = context;
    }

    public ArrayList<News> getNewsList(final RecyclerView recyclerView, String country, final String category ){

        final ArrayList<News> result = new ArrayList<>();
        String url = ResourceHelper.NEWS_URL + "country=" + country ;
        if( category != null )
            url += "&category=" + category;
        url += "&apiKey=" + ResourceHelper.NEWS_API_KEY;

        JsonObjectRequest request = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray articles = response.getJSONArray("articles");
                            String title, description, urlToNews, urlToImage;
                            for( int i = 0; i < articles.length(); i++ ){
                                JSONObject article = articles.getJSONObject(i);
                                title = article.getString("title");
                                description = article.getString("description");
                                urlToImage = article.getString("urlToImage");
                                urlToNews = article.getString("url");
                                result.add(new News(title,description,category, urlToNews,urlToImage));
                            }
                        } catch (Exception e) {
                            Log.i("Error:::::::::::::::",e.getMessage());
                        }
                        Log.i("Secces::::::::::::",result.toString());
                        NewsCardAdapter nationalNewsCardAdapter = new NewsCardAdapter(context, result);
                        recyclerView.setAdapter(nationalNewsCardAdapter);
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Log.i("Error:::::::::::::::", error.getMessage());
                    }
                });

        RequestQueue queue = Volley.newRequestQueue( context );
        queue.add(request);
        return result;
    }
}
