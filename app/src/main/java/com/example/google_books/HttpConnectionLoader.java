package com.example.google_books;

import android.content.Context;

import androidx.annotation.Nullable;
//import androidx.loader.content.AsyncTaskLoader;
//android.content.AsyncTaskLoader
import android.content.AsyncTaskLoader;
import android.net.Uri;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.os.Build;
import android.os.Bundle;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.util.ArrayList;
import java.util.List;
//import android.support.annotation.RequiresApi;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

public class HttpConnectionLoader extends AsyncTaskLoader<ArrayList<Books>> {
    private static String Url_string;
    private ArrayList<Books> arrayList=null;
    @Nullable
    @Override
    public ArrayList<Books> loadInBackground() {
        if(Url_string==MainActivity.Basic_Url)
            return  arrayList;
        InputStreamReader inputStreamReader=createHttp(Url_string);
        if(inputStreamReader==null)
            return null;
        String JsonString;
        try {
            JsonString =getJsonString(inputStreamReader);
            getArrayList(JsonString);
        } catch (IOException | JSONException ioException) {
            ioException.printStackTrace();
        }
        return arrayList;
    }

    HttpConnectionLoader(Context context,String current_Url)
    {
        super(context);
        Url_string=current_Url;
    }

    public static void  Set_Url(String new_Url)
    {
        Url_string=new_Url;
    }
    public ArrayList<Books> getArrayList()
    {
        return  arrayList;
    }
    private InputStreamReader createHttp(String url)
    {
        InputStreamReader inputStreamReader=null;
        try {
            URL urlObject=new URL(Url_string);
            HttpURLConnection http=(HttpURLConnection) urlObject.openConnection();

            http.connect();
            http.setConnectTimeout(16000);
            if(http.getResponseCode()!=200)
            {
                return null;
            }
            InputStream inputStream=http.getInputStream();
             inputStreamReader =new InputStreamReader(inputStream,Charset.forName("UTF-8"));
        } catch (MalformedURLException malformedURLException) {
            malformedURLException.printStackTrace();
        } catch (IOException ioException) {
            Log.e("abdooooooooooooooooooo",ioException.getMessage());
            Log.e("abdooooooooooooooooooo",ioException.getCause().toString());
            ioException.printStackTrace();
        }

        return inputStreamReader;

    }
    private String getJsonString(InputStreamReader inputStreamReader) throws IOException {

        String line=null;
        StringBuilder JsonString=new StringBuilder();
        BufferedReader bufferReader=new BufferedReader(inputStreamReader);
        line=bufferReader.readLine();
        while(line!=null)
        {
            JsonString.append(line);
            line=bufferReader.readLine();
        }
        return  JsonString.toString();
    }
    private void getArrayList(String JsonString) throws JSONException, IOException {
        JSONObject root=new JSONObject(JsonString);
        JSONArray items=root.getJSONArray("items");
        for (int i=0;i<root.length();i++)
        {
            JSONObject item=items.getJSONObject(i);
            String self_link=item.getString("selfLink");
            getSingleItem(self_link);
        }
    }


    private void getSingleItem(String item_url) throws IOException, JSONException {
        InputStreamReader inputStreamReader;
        inputStreamReader=createHttp(item_url);
        String JSON_String=getJsonString(inputStreamReader);

                JSONObject item_root= null;
                try {
                    String thumbnail=null,image=null, title=null,subtitle=null,publisher=null,language=null,author=null,date=null
                            ,catogrey=null  ,preview_link=null,info_link=null;

                item_root = new JSONObject(JSON_String);
                JSONObject volume_info=item_root.getJSONObject("volumeInfo");
                    int pageCount=-1;
                    if(volume_info.has("title"))
                        title = volume_info.getString("title");
                    if(volume_info.has("description"))
                        subtitle=volume_info.getString("description");
                    if(volume_info.has("publisher"))
                        publisher = "Publisher: " + volume_info.getString("publisher");
                    if(volume_info.has("publishedDate"))
                        date = volume_info.getString("publishedDate");
                    if(volume_info.has("pageCount"))
                        pageCount = volume_info.getInt("pageCount");
                    if(volume_info.has("language"))
                        language = volume_info.getString("language");
                    if(volume_info.has("previewLink"))
                        preview_link=volume_info.getString("previewLink");
                    if(volume_info.has("infoLink"))
                        info_link=volume_info.getString("infoLink");
                    if(volume_info.has("authors")) {
                        JSONArray authors = volume_info.getJSONArray("authors");
                        author = authors.getString(0);
                        for (int i = 1; i < authors.length(); i++)
                            author += " & " + authors.getString(i);
                    }
                    if(volume_info.has("imageLinks")) {
                        JSONObject image_links = volume_info.getJSONObject("imageLinks");
                        thumbnail = image_links.getString("thumbnail");
                        image=image_links.getString("large");
                    }
                    if(volume_info.has("categories")) {
                        JSONArray categories = volume_info.getJSONArray("categories");
                        catogrey=categories.getString(0);
                        for (int i=1;i<categories.length();i++)
                            catogrey+=categories.getString(i);
                    }int review_num = 0;
                    double ratings = 0;
                    if (volume_info.has("averageRating")) {
                        ratings = volume_info.getDouble("averageRating");
                        review_num = volume_info.getInt("ratingsCount");
                    }

                    arrayList.add(new Books(thumbnail, title, author, language, publisher, date, pageCount, ratings, (review_num + " Reviews"),image,subtitle,catogrey,preview_link,info_link));
                } catch (JSONException e) {
                    e.printStackTrace();
                }

    }
}
