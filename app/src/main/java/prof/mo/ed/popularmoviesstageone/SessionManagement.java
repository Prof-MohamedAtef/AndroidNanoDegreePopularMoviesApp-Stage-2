package prof.mo.ed.popularmoviesstageone;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashMap;

/**
 * Created by Prof-Mohamed Atef on 9/25/2018.
 */
public class SessionManagement {
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Context _context;
    int PRIVATE_MODE = 0;
    public static final String PREFS_ = "StatusFile";
    public static final String lastState= "lastState";
    public static final String AddedState= "AddedState";

    public SessionManagement(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREFS_, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void createLastNumSession(int lastNumber){
        editor.putInt(lastState, lastNumber);
        editor.commit();
    }

    public HashMap<String, Integer> getLastNumSession(){
        HashMap<String, Integer> user = new HashMap<String, Integer>();
        user.put(lastState, pref.getInt(lastState, 2));
        return user;
    }

    public void createAddedNumSession(int lastNumber){
        editor.putInt(lastState, lastNumber);
        editor.commit();
    }

    public HashMap<String, Integer> getAddedNumSession(){
        HashMap<String, Integer> user = new HashMap<String, Integer>();
        user.put(lastState, pref.getInt(lastState, 2));
        return user;
    }
}
