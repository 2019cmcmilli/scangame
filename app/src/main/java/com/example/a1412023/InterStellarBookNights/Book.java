package com.example.a1412023.InterStellarBookNights;

import android.graphics.Bitmap;

public class Book {
    private String title;
    private String subtitle;
    private int[] subjs;
    private Bitmap cover;
    private int pubdate;
    private int pagecount;
    private String note;

    public Book(){
        title = "Test Title";
        subtitle = "Test Subtitle";
        subjs = new int[]{20, 40, 0, 0, 4, 0};
        int[] colors = new int[120*200];
        for(int i = 0; i < colors.length; ++i){
            colors[i] = 0;
        }
        cover = Bitmap.createBitmap(colors, 120, 200, Bitmap.Config.ARGB_8888);
        pubdate = 2014;
        pagecount = 101;
        note = "A good test! Would test again";
    }
    //public Book from data

    public String getTitle() {
        return title;
    }
    public String getSubtitle() {
        return subtitle;
    }
    public int[] getSubjs() {
        return subjs;
    }
    public Bitmap getCover() {
        return cover;
    }
    public int getPubdate() {
        return pubdate;
    }
    public int getPagecount() {
        return pagecount;
    }
    public String getNote(){
        return note;
    }

    public void setNote(String note){
        this.note = note;
    }
}
