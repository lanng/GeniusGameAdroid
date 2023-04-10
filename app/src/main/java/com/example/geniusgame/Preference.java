package com.example.geniusgame;

import android.content.Context;
import android.content.SharedPreferences;

public class Preference {
    Context context;
    final String SAVE = "RECORDS";
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    String key = "points";

    public Preference(Context context){
        this.context = context;
        preferences = this.context.getSharedPreferences("RECORDS", 0);
        editor = preferences.edit();
    }

    public void saveData(String data){
        editor.putString(key, data);
        editor.commit();
    }

    public String getData(){
        return preferences.getString(key, "0");
    }
}
