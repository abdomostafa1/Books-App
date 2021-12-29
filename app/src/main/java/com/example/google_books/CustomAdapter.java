package com.example.google_books;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class CustomAdapter extends ArrayAdapter<Books> {
    Context context;
    CustomAdapter(Context context, ArrayList<Books> arrayList)
    {
        super(context,0,arrayList);
        this.context=context;
    }
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        if(convertView==null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.cutom_book_layout, parent, false);
        }
        Books book=getItem(position);

        TextView title=(TextView)convertView.findViewById(R.id.title_id);
        if(book.getTitle()!=null)
        title.setText(book.getTitle());

        TextView author=(TextView)convertView.findViewById(R.id.author_id);
        if(book.getAuthor()!=null)
            author.setText("By: "+book.getAuthor());

        TextView language=(TextView)convertView.findViewById(R.id.lang_id);
        if(book.getLanguage()!=null)
        language.setText("Language: "+book.getLanguage());

        TextView pages=(TextView)convertView.findViewById(R.id.page_num_id);
        if(book.getPages_num()!=-1)
        pages.setText("Pages: "+book.getPages_num());

        TextView publisher=(TextView)convertView.findViewById(R.id.publisher_id);
        if(book.getPublisher()!=null)
        publisher.setText(book.getPublisher());

        TextView date=(TextView)convertView.findViewById(R.id.date_id);
        if(book.getDate()!=null)
        date.setText(book.getDate());

        ImageView thumbnail =(ImageView) convertView.findViewById(R.id.book_image);
        if(book.getThumbnail_url()!=null)
        Glide.with(context).load(book.getThumbnail_url()).into(thumbnail);
        return convertView;
    }
}
