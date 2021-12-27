package com.example.google_books;

public class Books {
    private String thumbnail_url;
    private String image_url;
    private String title;
    private String subtitle;
    private String author;
    private String language;
    private String publisher;
    private  String date;
    private  int pages_num;
    private   double ratings;
    private  String reviews_num;
    private String catorgry;
    private String preview_link;
    private String info_link;
    Books(String thumbnail_url,String title,String author,String language,String publisher, String date,int pages_num,double ratings,String reviews_num
            ,String image_url,String subtitle,String catorgry,String preview_link, String info_link)
    {
        this.thumbnail_url=thumbnail_url;
        this.image_url=image_url;
        this.title=title;
        this.subtitle=subtitle;
        this.author=author;
        this.language=language;
        this.publisher=publisher;
        this.date=date;
        this.catorgry=catorgry;
        this.pages_num=pages_num;
        this.ratings=ratings;
        this.reviews_num=reviews_num;
        this.preview_link=preview_link;
        this.info_link=info_link;
    }
    public String getThumbnail_url()
    {
        return thumbnail_url;
    }
    public String getImage_url()
    {
        return image_url;
    }

    public String getTitle()
    {
        return title;
    }
    public String getSubtitle()
    {
        return subtitle;
    }
    public String getAuthor()
    {
        return author;
    }
    public String getLanguage()
    {
        return language;
    }
    public String getPublisher()
    {
        return publisher;
    }
    public String getCatorgry()
    {
        return catorgry;
    }

    public String getDate()
    {
        return date;
    }
    public int getPages_num()
    {
        return pages_num;
    }
    public String getReviews_num()
    {
        return reviews_num;
    }
    public double getRatings()
    {
        return ratings;
    }
    public String getPreview_link()
    {
        return preview_link;
    }
    public String getInfo_link()
    {
        return info_link;
    }
}
