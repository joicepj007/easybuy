package easybuy.com.easybuy.util;

import android.content.Context;

public class SharedPreferenceUtil {

    private static SharedPreferenceUtil instance;
    private static int PRIVATE_MODE = 0;

    private SharedPreferenceUtil getInstance() {
        if (instance != null)
            return new SharedPreferenceUtil().getInstance();
        return instance;
    }

    public static void addBooleanToSharedPreference(Context context, String sharedPreferenceName,
                                                    String propertyIsLoggedIn, Boolean isLoggedIn) {
        context.getSharedPreferences(sharedPreferenceName, PRIVATE_MODE).edit().putBoolean(propertyIsLoggedIn, isLoggedIn).apply();

    }

    public static boolean readBooleanFromSharedPreference(Context context,
                                                          String sharedPreferenceName, String propertyBoolean) {
        return context.getSharedPreferences(sharedPreferenceName, PRIVATE_MODE).getBoolean(propertyBoolean, false);
    }
}
