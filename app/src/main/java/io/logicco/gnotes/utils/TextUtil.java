package io.logicco.gnotes.utils;

import android.content.Context;
import android.widget.Toast;

public final class TextUtil {

    private TextUtil(){}

    public static void toast(Context context, String string){
        Toast.makeText(context, string, Toast.LENGTH_SHORT).show();
    }
}
