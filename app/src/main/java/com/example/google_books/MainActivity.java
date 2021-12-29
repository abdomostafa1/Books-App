package com.example.google_books;

import androidx.annotation.NonNull;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputLayout;

public class MainActivity extends AppCompatActivity {
    EditText topic = null;
    Spinner category;
    Spinner price_category;
    Spinner lang_spinner;
    TextInputLayout textInputLayout;
    public static int selected_language = 0;
    public static int selected_category = 0;
    public static int selected_price_category = 0;
    protected final static String Basic_Url = "https://www.googleapis.com/books/v1/volumes?q=";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textInputLayout=(TextInputLayout)findViewById(R.id.text_input_layout_id);
        topic = (EditText) findViewById(R.id.topic);
        category = (Spinner) findViewById(R.id.book_or_author);
        category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position==0)
                   textInputLayout.setHint("Topic");
                else
                    textInputLayout.setHint("Author");

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

    }


    public void GuideActivity(View view) {
        Intent intent = new Intent(MainActivity.this, HowToUseAppActivity.class);
        startActivity(intent);
    }

    public void search(View view) {
        ConnectivityManager conMan = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = conMan.getActiveNetworkInfo();
        if (!(networkInfo != null && networkInfo.isAvailable() && networkInfo.isConnected())) {
            Toast.makeText(MainActivity.this, "No Internet Connection!", Toast.LENGTH_LONG).show();
            return;
        }

        String topic_str = topic.getText().toString();
        if (topic_str.length() == 0) {
            Toast.makeText(MainActivity.this, "Topic is Empty", Toast.LENGTH_LONG).show();
            return;
        }

        String final_Url = CreateUrl.getUrl(topic_str);
        Intent intent=new Intent(MainActivity.this,ListViewActivity.class);
        intent.putExtra("URL",final_Url);
        startActivity(intent);

    }
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
    }
