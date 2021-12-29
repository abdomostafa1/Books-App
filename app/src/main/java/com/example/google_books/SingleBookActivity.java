package com.example.google_books;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import com.bumptech.glide.Glide;

public class SingleBookActivity extends AppCompatActivity {
    private String previewlink;
    private String infolink;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_book);
        Toolbar toolbar=findViewById(R.id.toolbaar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        Intent intent=getIntent();
        ImageView image=(ImageView)findViewById(R.id.large_img);
        String title,subtitle;
        String imageUrl=intent.getStringExtra("large_img");
        if(imageUrl!=null)
        Glide.with(SingleBookActivity.this).load(imageUrl).into(image);

        String language=intent.getStringExtra("language");
        subtitle="description: ";

        TextView  mtitle=(TextView)findViewById(R.id.repeated_title);
        if(intent.getStringExtra("title")!=null) {
            String str = intent.getStringExtra("title").replaceAll("title", "");
            mtitle.setText(str);
        }
            else
            mtitle.setText("");
        TextView  msubtitle=(TextView)findViewById(R.id.subtitle);
        if(intent.getStringExtra("subtitle")!=null) {
           String str = intent.getStringExtra("subtitle").replaceAll("\\<.*?\\>", "");
            msubtitle.setText(subtitle+str);
        }else
            msubtitle.setText(subtitle+"...");
        TextView reviews=(TextView)findViewById(R.id.review_id);
        reviews.setText(intent.getStringExtra("reviews_num"));
        RatingBar rating=(RatingBar)findViewById(R.id.book_rating);
        rating.setRating((float)intent.getDoubleExtra("rating",0.0));
        rating.setIsIndicator(true);

        previewlink=intent.getStringExtra("preview_link");
        infolink=intent.getStringExtra("info_link");
        }
        public void toGoogle(View view)
        {
            if(previewlink!=null)
            {
                Uri uri=Uri.parse(previewlink);
                Intent intent=new Intent(Intent.ACTION_VIEW,uri);
                startActivity(intent);
            }
        }
    public void toPlayStore(View view)
    {
        if(infolink!=null)
        {
            Uri uri=Uri.parse(infolink);
            Intent intent=new Intent(Intent.ACTION_VIEW,uri);
            startActivity(intent);
        }
    }
    }
