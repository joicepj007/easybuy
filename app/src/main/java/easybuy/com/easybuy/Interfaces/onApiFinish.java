package easybuy.com.easybuy.Interfaces;

public interface onApiFinish {
    void onSuccess(String tag, String response);
    void onFailed(String tag, String response);
    void onJsonError(String tag, String response);
    void onServerError();
}
