package ca.nika.it.gear5;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferenceManager {

    private static  PreferenceManager INSTANCE;
    private  static SharedPreferences preferences;

    synchronized public static PreferenceManager getInstance(Context context){
        if(INSTANCE==null){
            INSTANCE=new PreferenceManager();
            preferences=context.getSharedPreferences("userinfo3",Context.MODE_PRIVATE);
        }
        return INSTANCE;
    }

    public void setString(String key,String value){
        preferences.edit().putString(key, value).apply();
    }

    public String getString(String key){
        return preferences.getString(key,"");
    }

}