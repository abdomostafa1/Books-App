
package com.example.google_books;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import androidx.appcompat.widget.Toolbar;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ListViewActivity extends AppCompatActivity {
    ProgressBar progressBar = null;
    CustomAdapter adapter = null;
    ListView listView = null;
    RequestQueue queue = null;
    ArrayList<Books> arrayList;
    private static int ResponseCount = 0;
    Toolbar toolbar = null;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_view);
        toolbar = (Toolbar) findViewById(R.id.toolbaar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        listView = (ListView) findViewById(R.id.list_view);
        progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        arrayList = new ArrayList<Books>();
        adapter = new CustomAdapter(ListViewActivity.this, arrayList);
        listView.setAdapter(adapter);
        queue = Volley.newRequestQueue(ListViewActivity.this);
        Intent intent = getIntent();
        String url = intent.getStringExtra("URL");
        phase_One(url);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Books book = (Books) parent.getItemAtPosition(position);
                Intent intent = new Intent(ListViewActivity.this, SingleBookActivity.class);
                intent.putExtra("large_img", book.getThumbnail_url());
                intent.putExtra("title", book.getTitle());
                intent.putExtra("subtitle", book.getSubtitle());
                intent.putExtra("catogries", book.getCatorgry());
                intent.putExtra("language", book.getLanguage());
                intent.putExtra("preview_link", book.getPreview_link());
                intent.putExtra("info_link", book.getInfo_link());
                intent.putExtra("rating",book.getRatings());
                intent.putExtra("reviews_num",book.getReviews_num());
                intent.putExtra("selected_language",intent.getIntExtra("selected_language",0));
                startActivity(intent);
            }
        });
    }

    void phase_One(String Url) {
        ArrayList<String> self_links = new ArrayList<String>();
        StringRequest stringRequest1 = new StringRequest(Url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject root = new JSONObject(response);
                    JSONArray items = root.getJSONArray("items");

                    for (int i = 0; i < items.length(); i++) {
                        JSONObject item = items.getJSONObject(i);
                        String item_link = item.getString("selfLink");
                        self_links.add(item_link);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                phase_Two(self_links);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        queue.add(stringRequest1);
    }

    void phase_Two(ArrayList<String> self_link) {

        for (int i = 0; i < self_link.size(); i++) {

            StringRequest stringRequest = new StringRequest(self_link.get(i), new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    ResponseCount++;
                    JSONObject item_root = null;

                    try {
                        item_root = new JSONObject(response);
                        JSONObject volume_info = item_root.getJSONObject("volumeInfo");

                        //Primitive Values
                        String thumbnail = null, image = null, title = null, subtitle = null, publisher = null,
                                language = null, author = null, date = null, catogrey = null, preview_link = null, info_link = null;
                        int pageCount = -1;

                        if (volume_info.has("title"))
                            title = volume_info.getString("title");

                        if (volume_info.has("description"))
                            subtitle = volume_info.getString("description");

                        if (volume_info.has("publisher"))
                            publisher = volume_info.getString("publisher");

                        if (volume_info.has("publishedDate"))
                            date = volume_info.getString("publishedDate");

                        if (volume_info.has("pageCount"))
                            pageCount = volume_info.getInt("pageCount");

                        if (volume_info.has("language"))
                            language = volume_info.getString("language");

                        if (volume_info.has("previewLink"))
                            preview_link = volume_info.getString("previewLink");

                        if (volume_info.has("infoLink"))
                            info_link = volume_info.getString("infoLink");

                        if (volume_info.has("authors")) {
                            JSONArray authors = volume_info.getJSONArray("authors");
                            author = authors.getString(0);
                            for (int i = 1; i < authors.length(); i++)
                                author += " & " + authors.getString(i);
                        }

                        if (volume_info.has("imageLinks")) {
                            JSONObject image_links = volume_info.getJSONObject("imageLinks");
                            if (image_links.has("thumbnail")) ;
                            thumbnail = image_links.getString("thumbnail");
                            if (image_links.has("large"))
                                image = image_links.getString("large");
                        }

                        if (volume_info.has("categories")) {
                            JSONArray categories = volume_info.getJSONArray("categories");
                            catogrey = categories.getString(0);
                            for (int i = 1; i < categories.length(); i++)
                                catogrey += categories.getString(i);
                        }

                        int review_num = 0;
                        double ratings = 0;
                        if (volume_info.has("averageRating"))
                            ratings = volume_info.getDouble("averageRating");
                        if (volume_info.has("ratingsCount"))
                            review_num = volume_info.getInt("ratingsCount");

                        arrayList.add(new Books(thumbnail, title, author, language, publisher, date, pageCount, ratings, (review_num + ""), image, subtitle, catogrey, preview_link, info_link));


                        if (ResponseCount == self_link.size()) {
                            progressBar.setVisibility(View.INVISIBLE);
                            adapter.addAll(arrayList);
                            ResponseCount = 0;
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            });

            queue.add(stringRequest);
        }
    }
}