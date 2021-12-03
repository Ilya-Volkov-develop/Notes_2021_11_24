package ru.iliavolkov.notes;

import android.content.res.Configuration;
import android.content.res.Resources;

public class Utils {

    public static boolean portable = true;
    public static String title = null;
    public static String description = null;
    public static int index = 0;

    public static boolean isLandscape(Resources res) {
        return res.getConfiguration().orientation
                == Configuration.ORIENTATION_LANDSCAPE;
    }
}
