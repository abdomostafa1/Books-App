package com.example.google_books;

import
        androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.Loader;
import android.app.LoaderManager;
import android.content.Context;
import android.content.Loader;
import android.graphics.Rect;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<ArrayList<Books>> {
    EditText topic = null;
    ProgressBar progressBar = null;
    CustomAdapter adapter = null;
    ListView listView = null;
    TextView no_internet = null;
    Spinner category;
    Spinner price_category;
    Spinner lang_spinner;
    RequestQueue queue;
    ArrayList<Books> arrayList;
    public static int selected_language = 0;
    public static int selected_category = 0;
    public static int selected_price_category = 0;
    protected final static String Basic_Url = "https://www.googleapis.com/books/v1/volumes?q=";
    private static int ResponseCount = 0;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            View view = getCurrentFocus();
            if (view instanceof EditText) {
                Rect outRect = new Rect();
                view.getGlobalVisibleRect(outRect);
                if (!outRect.contains((int) event.getX(), (int) event.getY())) {
                    view.clearFocus();
                    InputMethodManager iM = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    iM.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
            }
        }
        return super.dispatchTouchEvent(event);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView = (ListView) findViewById(R.id.list_view);
        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.main_linear);
//        yourtListView.setNestedScrollingEnabled(true);

        View header = getLayoutInflater().inflate(R.layout.before_list, null);
        //  header.setPadding(15,0,15,0);
        listView.addHeaderView(header);
        progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        topic = (EditText) findViewById(R.id.topic);
        arrayList = new ArrayList<Books>();
        adapter = new CustomAdapter(MainActivity.this, arrayList);
        listView.setAdapter(adapter);
        queue = Volley.newRequestQueue(MainActivity.this);
        category = (Spinner) findViewById(R.id.book_or_author);
        category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selected_category = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        price_category = (Spinner) findViewById(R.id.price_spinner);
        price_category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selected_price_category = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        lang_spinner = (Spinner) findViewById(R.id.language_spinner);
        lang_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selected_language = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        no_internet = findViewById(R.id.no_internet);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Books book = (Books) parent.getItemAtPosition(position);
                Intent intent = new Intent(MainActivity.this, SingleBookActivity.class);
                intent.putExtra("large_img", book.getImage_url());
                intent.putExtra("title", book.getTitle());
                intent.putExtra("subtitle", book.getSubtitle());
                intent.putExtra("catogries", book.getCatorgry());
                intent.putExtra("language", book.getLanguage());
                intent.putExtra("preview_link", book.getPreview_link());
                intent.putExtra("info_link", book.getInfo_link());
                startActivity(intent);
            }
        });
       //  getLoaderManager().initLoader(0,null,this);
    }

    @Override
    public Loader<ArrayList<Books>> onCreateLoader(int id, Bundle args) {
        HttpConnectionLoader httpConnectionLoader = new HttpConnectionLoader(MainActivity.this, Basic_Url);

        return httpConnectionLoader;
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<Books>> loader, ArrayList<Books> data) {
        if (data == null) {
            progressBar.setVisibility(View.INVISIBLE);
            return;
        }
        progressBar.setVisibility(View.INVISIBLE);
        adapter.clear();
        adapter.addAll(data);
    }

    @Override
    public void onLoaderReset(Loader<ArrayList<Books>> loader) {

    }

    public void thirdactivity(View view) {
        Intent intent = new Intent(MainActivity.this, HowToUseAppActivity.class);
        startActivity(intent);
    }

    public void search(View view) {
        adapter.clear();
        ConnectivityManager conMan = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = conMan.getActiveNetworkInfo();
        if (!(networkInfo != null && networkInfo.isAvailable() && networkInfo.isConnected())) {
            no_internet.setVisibility(View.VISIBLE);
            return;
        }
        String topic_str = topic.getText().toString();
        if (topic_str.length() == 0) {
            Toast.makeText(MainActivity.this, "Topic is Empty", Toast.LENGTH_LONG).show();
            return;
        }
        no_internet.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.VISIBLE);
        String final_Url = CreateUrl.getUrl(topic_str);

        phase_One(final_Url);

        //adapter.addAll(arrayList);
       // HttpConnectionLoader.Set_Url(final_Url);
        // getLoaderManager().initLoader(0,null,MainActivity.this).forceLoad();

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
                        String thumbnail = null, image = null, title = null, subtitle = null, publisher = null, language = null, author = null, date = null, catogrey = null, preview_link = null, info_link = null;
                        int pageCount = -1;
                        if (volume_info.has("title"))
                            title = volume_info.getString("title");
                        if (volume_info.has("description"))
                            subtitle = volume_info.getString("description");
                        if (volume_info.has("publisher"))
                            publisher = "Publisher: " + volume_info.getString("publisher");
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
                            thumbnail = image_links.getString("thumbnail");
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
                        if (volume_info.has("averageRating")) {
                            ratings = volume_info.getDouble("averageRating");
                            review_num = volume_info.getInt("ratingsCount");
                        }
                        //   Books(String thumbnail_url,String title,String author,String language,String publisher, String date,int pages_num
                        //   ,double ratings,String reviews_num
                        //        ,String image_url,String subtitle,String catorgry,String preview_link, String info_link)
                        arrayList.add(new Books(thumbnail, title, author, language, publisher, date, pageCount, ratings, (review_num + " Reviews"), image, subtitle, catogrey, preview_link, info_link));
                        if (ResponseCount == self_link.size()) {
                            progressBar.setVisibility(View.INVISIBLE);
                            adapter.addAll(arrayList);
                            progressBar.setVisibility(View.INVISIBLE);
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