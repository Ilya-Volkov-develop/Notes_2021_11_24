package ru.iliavolkov.notes;

import android.content.res.Configuration;
import android.content.res.Resources;

public class Utils {
    public static boolean isLandscape(Resources res) {
        return res.getConfiguration().orientation
                == Configuration.ORIENTATION_LANDSCAPE;
    }
}
