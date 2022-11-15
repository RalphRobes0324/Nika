// CENG-322-0NC Francisco Santos n01423860, Pradeep Singh n00975892
// CENG-322-0NB Ralph Robes n01410324, Elijah Tanimowo n01433560
package ca.nika.it.gear5;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferenceManager {
    /*

    In this java class, we were unable to remove the hardcoded version of these strings used, it would cause an error, and since these strings
    do not display inside of the fragments, it should be fine to not require french translation.

     */
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