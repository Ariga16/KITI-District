package com.dacasa.sdakitidistrict.Models;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPref {
    SharedPreferences mySharedPref ;
    public SharedPref(Context context){
        mySharedPref = context.getSharedPreferences("filename",Context.MODE_PRIVATE);
    }
    // this method will save the night mode state:True or False

    public void setNightModeState(Boolean state) {
        SharedPreferences.Editor editor = mySharedPref.edit();
        editor.putBoolean("NightMode",state);
        editor.commit();
    }
    //this method will load the night mode state
    public Boolean loadNightModeState () {
        Boolean state = mySharedPref.getBoolean("NightMode",false);
        return state;
    }
}
