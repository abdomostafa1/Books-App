package com.example.google_books;

public class CreateUrl {
    private static String URL="https://www.googleapis.com/books/v1/volumes?q=";
    private static String final_URL;
    private static String key="&key=yourAPIKeyAIzaSyCQB2htnfPM5GadBz4F3NZyAqwLTp_Hdd8";
    CreateUrl() {
    }

    public static String getUrl(String topic) {
        final_URL=URL;
        check_Category(topic);
        check_lang_Category();
        check_price_Category();
        maxrresult();
        //addkey();
        return final_URL;
    }

    private static void check_Category(String topic) {

        if (MainActivity.selected_category == 0)
            final_URL += topic;
        if (MainActivity.selected_category == 1)
            final_URL += "inauthor:" + topic;

    }

    private static void  check_lang_Category() {
        switch (MainActivity.selected_language) {
            case 0:
                break;
            case 1:
                final_URL += "&langRestrict=en";
                break;
            case 2:
                final_URL += "&langRestrict=ar";
                break;
            case 3:
                final_URL += "&langRestrict=fr";
                break;
            case 4:
                final_URL += "&langRestrict=es";
                break;
            case 5:
                final_URL += "&langRestrict=zh";
                break;
            case 6:
                final_URL += "&langRestrict=ru";
                break;
            case 7:
                final_URL += "&langRestrict=hi";

        }

    }

    private static void check_price_Category() {
        switch (MainActivity.selected_price_category) {
            case 1:
                final_URL += "&filter=free-ebooks";
                break;
            case 2:
                final_URL+="&filter=paid-ebooks";
                break;
            case 3:
                final_URL+="&filter=ebooks";
        }
    }
    private static void maxrresult()
    {
        final_URL+="&maxResults=40";
    }
   private static void addkey()
    {
    final_URL+=key;
    }

}