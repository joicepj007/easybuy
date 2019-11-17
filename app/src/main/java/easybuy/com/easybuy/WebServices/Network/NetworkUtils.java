package easybuy.com.easybuy.WebServices.Network;

import android.net.Uri;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import easybuy.com.easybuy.WebServices.Controller.AppLog;

public class NetworkUtils {


    public static String buildPostParameters(Object content) {
        String output = null;
        if ((content instanceof String) ||
                (content instanceof JSONObject) ||
                (content instanceof JSONArray)) {
            output = content.toString();
        } else if (content instanceof Map) {
            Uri.Builder builder = new Uri.Builder();
            HashMap hashMap = (HashMap) content;
            if (hashMap != null) {
                Iterator entries = hashMap.entrySet().iterator();
                while (entries.hasNext()) {
                    Map.Entry entry = (Map.Entry) entries.next();
                    builder.appendQueryParameter(entry.getKey().toString(), entry.getValue().toString());
                    entries.remove(); // avoids a ConcurrentModificationException
                }
                output = builder.build().getEncodedQuery();
            }
        }

        return output;
    }

    public static URLConnection makeRequest(String method, String apiAddress, String accessToken, String mimeType, String requestBody) throws IOException {
        URL url = new URL(apiAddress);
        HttpURLConnection urlConnection = null;
        if (method.equalsIgnoreCase("POST")) {
            AppLog.logString( "url==" + method + apiAddress);

            urlConnection = (HttpURLConnection) url.openConnection();

            urlConnection.setDoInput(true);
            urlConnection.setDoOutput(!method.equals("GET"));
            urlConnection.setRequestMethod(method);

            //urlConnection.setRequestProperty("Authorization", "Bearer " + accessToken);

            urlConnection.setRequestProperty("Content-Type", mimeType);
            OutputStream outputStream = new BufferedOutputStream(urlConnection.getOutputStream());
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream, "utf-8"));
            AppLog.logString( "requestbody" + requestBody);
            if(requestBody!=null) {
                writer.write(requestBody);
            }
            writer.flush();
            writer.close();
            outputStream.close();

            urlConnection.connect();
            return urlConnection;

        } else {
            try {
                //url = new URL("http://www.mysite.se/index.asp?data=99");

                urlConnection = (HttpURLConnection) url
                        .openConnection();

                return urlConnection;
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
            }

        }
        return urlConnection;
    }

  /*  public static URLConnection sendImage(String apiAddress){
        MultipartEntityBuilder builder = MultipartEntityBuilder.create();


*//* example for setting a HttpMultipartMode *//*
        builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);

*//* example for adding an image part *//*
        //FileBody fileBody = new FileBody(new File(image)); //image should be a String
        //builder.addPart("my_file", fileBody);
   *//*     try {
            URL url = new URL(apiAddress);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Connection", "Keep-Alive");

            MultipartEntity entity = new MultipartEntity(
                    HttpMultipartMode.BROWSER_COMPATIBLE);

            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
            byte[] data = bos.toByteArray();
            ByteArrayBody bab = new ByteArrayBody(data, "test.jpg");
            entity.addPart("file", bab);

            entity.addPart("someOtherStringToSend", new StringBody("your string here"));

            conn.addRequestProperty("Content-length", entity.getContentLength() + "");
            conn.addRequestProperty(entity.getContentType().getName(), entity.getContentType().getValue());

            OutputStream os = conn.getOutputStream();
            entity.writeTo(conn.getOutputStream());
            os.close();
            conn.connect();
            return conn;




        } catch (Exception e) {
            e.printStackTrace();
            // something went wrong. connection with the server error
        }*//*
        return null;
    }*/
}
