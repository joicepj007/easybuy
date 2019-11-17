package easybuy.com.easybuy.WebServices.Backgroundtask;

import android.content.Context;
import android.os.AsyncTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.util.HashMap;

import easybuy.com.easybuy.Interfaces.onApiFinish;
import easybuy.com.easybuy.WebServices.Network.NetworkUtils;
import easybuy.com.easybuy.util.Logging;

public class CallWebServiceTask extends AsyncTask<String, Void, String> {

    onApiFinish listener;
    Context mcontext;
    private String TAG = "CallWebServiceTask";
    HashMap<String, String> values;
    String from = "";

    public CallWebServiceTask(Context mcontext, HashMap<String, String> values) {
        this.listener = (onApiFinish) mcontext;
        this.mcontext = mcontext;
        this.values = values;

    }

    public CallWebServiceTask(Context mContext, onApiFinish listener, HashMap<String, String> values) {
        this.listener = listener;
        this.mcontext = mContext;
        this.values = values;


    }

    @Override
    protected void onPreExecute() {
    }

    @Override
    protected String doInBackground(String... params) {

        String url = params[0];
        this.from = params[1];
        HttpURLConnection urlConnection = null;
        String requestBody = NetworkUtils.buildPostParameters(values);
        try {
            urlConnection = (HttpURLConnection) NetworkUtils.makeRequest(params[2], url, null, "application/x-www-form-urlencoded", requestBody);
            InputStream inputStream;
            // get stream
            if (urlConnection.getResponseCode() < HttpURLConnection.HTTP_BAD_REQUEST) {
                inputStream = urlConnection.getInputStream();
            } else {
                inputStream = urlConnection.getErrorStream();
            }
            // parse stream
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String temp, response = "";
            while ((temp = bufferedReader.readLine()) != null) {
                response += temp;
            }
            return response;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }

    }

    @Override
    protected void onPostExecute(String response) {
        super.onPostExecute(response);
        Logging.d("WebService123", from + "==" + response);
        if (response != null) {

            try {
                response = response.replace("Array()", "");
                JSONObject json = new JSONObject(response);
                if (json.getString("status").equalsIgnoreCase("success")) {
                    if (listener != null)
                        listener.onSuccess(from, response);
                    try {
                       /* if (from.equals("offer_list")) {
                            Gson gson = new Gson();
                            OffersResponse listResponse = gson.fromJson(response, OffersResponse.class);
                            *//*  List<OffersResponse.OfferList> options = item.getOptions();*//*
                            AppSession.getInstance(mcontext).setOffersCount(listResponse.getOfferList().size() + "");
                        }*/
                    } catch (Exception e) {
                        Logging.d("WebService123", e.toString());
                    }

                } else if (json.getString("status").equalsIgnoreCase("error")) {
                    if (listener != null)
                        listener.onFailed(from, response);
                } else {
                    if (listener != null)
                        listener.onFailed(from, response);
                }
            } catch (JSONException je) {
                if (listener != null)
                    listener.onJsonError(from, response);
            }

        } else {
            if (listener != null)
                listener.onServerError();
        }
    }
}

