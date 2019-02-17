package com.gmail.chattest.common;

import android.content.Context;
import android.content.SharedPreferences;

import com.gmail.chattest.constant.ConstantPrefs;

public class PreferenceUtil {

    private static SharedPreferences shared;
    private static SharedPreferences.Editor editor;

    private static SharedPreferences getShared(Context context){
        if ( shared == null )
            shared = context.getSharedPreferences(ConstantPrefs.PREFS_NAME , Context.MODE_PRIVATE);
        return shared;
    }

    private static SharedPreferences.Editor getEditor(Context context){
        if ( editor == null ) editor = getShared(context).edit();
        return editor;
    }

    public static String getString( Context context , String key ){
        return getShared(context).getString(key , ConstantPrefs.DEFAULT_STRING);
    }

    public static int getInt(Context context , String key ) {
        return getShared(context).getInt(key , ConstantPrefs.DEFAULT_INT);
    }

    public static boolean getBoolean( Context context , String key ) {
        return getShared(context).getBoolean(key , ConstantPrefs.DEFAULT_BOOLEAN);
    }

    public static void put(Context context , String key , String value ){
        getEditor(context).putString(key , value ).apply();
    }

    public static void put(Context context , String key , int value ) {
        getEditor(context).putInt(key , value).apply();
    }

    public static void put(Context context , String key , boolean value ) {
        getEditor(context).putBoolean(key , value).apply();
    }
}
