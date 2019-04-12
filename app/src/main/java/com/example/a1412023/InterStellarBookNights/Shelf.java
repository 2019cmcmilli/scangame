package com.example.a1412023.InterStellarBookNights;

import android.content.Context;

import com.google.gson.Gson;

import java.io.FileOutputStream;

public class Shelf {
    private Book[] books;
    private Context context;

    public Shelf(Context context){
        books = new Book[6];
        this.context = context;
    }
    //public Shelf from data

    public boolean addBook(Book b){
        for(int i = 0; i < books.length; ++i){
            if(books[i] == null){
                books[i] = b;
                if(updateStoredModel(i)) {
                    return true;
                }else{
                    books[i] = null;
                    return false;
                }
            }
        }
        return false;
    }

    public boolean replaceBook(Book b, int i){
        Book prev = books[i];
        books[i] = b;
        if(updateStoredModel(i)) {
            return true;
        }else{
            books[i] = prev;
            return false;
        }
    }

    public boolean deleteBook(int i){
        Book prev = books[i];
        books[i] = null;
        if(updateStoredModel(i)) {
            return true;
        }else{
            books[i] = prev;
            return false;
        }
    }

    public Book getBook(int i){
        return books[i];
    }

    private boolean updateStoredModel(int i){
        Gson gson = new Gson();
        try {
            FileOutputStream fos;
            fos = context.openFileOutput("book"+i+".json", Context.MODE_PRIVATE);
            fos.write(gson.toJson(books[i]).getBytes());
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
