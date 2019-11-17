package easybuy.com.easybuy.util;

import android.util.Log;

public class Logging {
    private static boolean LOGCAT_ENABLED = true;
    private static int MAX_LOG_SIZE=2000;

    public static void d(String TAG, String message) {
        try {
            if (LOGCAT_ENABLED) {
                // Log.d(TAG, message);
                for (int i = 0; i <= message.length() / MAX_LOG_SIZE; i++) {
                    int start = i * MAX_LOG_SIZE;
                    int end = (i + 1) * MAX_LOG_SIZE;
                    end = end > message.length() ? message.length() : end;
                    Log.d(TAG, message.substring(start, end));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void v(String TAG, String message) {
        try {
            if (LOGCAT_ENABLED) {
                // Log.v(TAG, message);
                for (int i = 0; i <= message.length() / MAX_LOG_SIZE; i++) {
                    int start = i * MAX_LOG_SIZE;
                    int end = (i + 1) * MAX_LOG_SIZE;
                    end = end > message.length() ? message.length() : end;
                    Log.v(TAG, message.substring(start, end));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void i(String TAG, String message) {
        try {
            if (LOGCAT_ENABLED) {
                // Log.i(TAG, message);
                for (int i = 0; i <= message.length() / MAX_LOG_SIZE; i++) {
                    int start = i * MAX_LOG_SIZE;
                    int end = (i + 1) * MAX_LOG_SIZE;
                    end = end > message.length() ? message.length() : end;
                    Log.i(TAG, message.substring(start, end));
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void e(String TAG, String message) {
        try {
            if (LOGCAT_ENABLED) {
                //Log.e(TAG, message);
                for (int i = 0; i <= message.length() / MAX_LOG_SIZE; i++) {
                    int start = i * MAX_LOG_SIZE;
                    int end = (i + 1) * MAX_LOG_SIZE;
                    end = end > message.length() ? message.length() : end;
                    Log.e(TAG, message.substring(start, end));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
