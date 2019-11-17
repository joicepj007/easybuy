package easybuy.com.easybuy.WebServices.Controller;

import android.util.Log;

public class AppLog {

    private static final String TAG = "UserApp";

    public static void logString(String message) {
        // (AppConstant.IS_LOG_ENABLED)
        Log.d(TAG, message);
    }
}