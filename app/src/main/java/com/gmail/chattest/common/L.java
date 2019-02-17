package com.gmail.chattest.common;

import android.util.Log;

public class L {
    private static boolean test = true;

    public static void d( String tag , String method , String detail ){
        if ( test ) Log.d(tag , method + " -->> " + detail);
    }

    public static void e(String tag , String method , String error){
        if ( test ) Log.e( tag , method + " -->> " + error );
    }

    public static void i(String tag , String method , String info){
        if ( test ) Log.i(tag , method + " -->> " + info);
    }
}