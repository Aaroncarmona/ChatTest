package com.gmail.chattest.common;

import android.content.Context;
import android.content.Intent;

import com.gmail.chattest.common.enums.ENavigation;
import com.gmail.chattest.constant.ConstantPrefs;
import com.gmail.chattest.ui.MainActivity;
import com.gmail.chattest.ui.firebasecloud.CloudActivity;
import com.gmail.chattest.ztest.TestActivity;

public class NavigationHelper {

    private static boolean test = true;

    public static void next(Context context){
        int aux = PreferenceUtil.getInt(context , ConstantPrefs.INT_NAVIGATION);
        int navigation = ( aux == -1 ) ? 0 : aux;

        if ( test ) {
            test(context);
            return;
        }

        if ( navigation == ENavigation.DEFAULT.ordinal() ) {
            main(context);
        } else if ( navigation == ENavigation.TEST.ordinal()) {
            test(context);
        } else if ( navigation == ENavigation.MAIN.ordinal()) {

        } else if ( navigation == ENavigation.CLOUD.ordinal()) {
            cloud(context);
        }
    }

    public static void cloud( Context context ){
        context.startActivity( new Intent(context , CloudActivity.class)
            .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
    }

    public static void test( Context context ) {
        context.startActivity( new Intent( context , TestActivity.class)
            .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK)
        );
    }

    public static void main( Context context ) {
        context.startActivity( new Intent(context , MainActivity.class)
            .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK)
        );
    }
}
