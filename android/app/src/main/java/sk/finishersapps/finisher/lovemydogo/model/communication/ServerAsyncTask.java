package sk.finishersapps.finisher.lovemydogo.model.communication;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by Filip on 22.11.2017.
 * Used for async communication. I send URL that I want to visit. After connection to URL
 * delegate acts ass callback and triggers method processFinished() so I can handle callback on main
 * thread.
 */
public class ServerAsyncTask extends AsyncTask<String, String, String> {

    public AsyncResponse delegate = null;

    public ServerAsyncTask(AsyncResponse delegate) {
        this.delegate = delegate;

    }

    public ServerAsyncTask() {

    }

    @Override
    protected String doInBackground(String... params) {
        URL providedURL = null;
        providedURL = getUrl(params[0]);
        if (providedURL != null) {
            return connectToURL(providedURL);
        }
        return null;
    }

    private String connectToURL(URL providedURL) {
        Log.d("connectionURL", providedURL.toString());
        HttpURLConnection conn;
        OutputStream os = null;
        BufferedWriter writer = null;
        String response = null;
        try {
            HashMap<String, String> postDataParams = new HashMap<>();
            conn = (HttpURLConnection) providedURL.openConnection();
            conn.setReadTimeout(5000);
            conn.setConnectTimeout(5000);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            conn.setDoOutput(true);
            os = conn.getOutputStream();
            writer = new BufferedWriter(
                    new OutputStreamWriter(os, "UTF-8"));
            writer.write(getPostDataString(postDataParams));
            writer.flush();
            int responseCode = conn.getResponseCode();
            if (responseCode == HttpsURLConnection.HTTP_OK) {
                String line;
                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                response = "";
                while ((line = br.readLine()) != null) {
                    response += line;
                }
            } else {
                response = null;
            }
        } catch (Exception e) {
            Log.e("Server task", "unable to connect " + e.getMessage());
            response = null;
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (Exception e) {
                    Log.e("writer", e.getMessage());
                }
            }
            if (os != null) {
                try {
                    os.close();
                } catch (Exception e) {
                    Log.e("os", e.getMessage());
                }
            }
        }

        return response;
    }

    private URL getUrl(String givenURL) {
        Log.d("Server", "connecting to " + givenURL);
        try {
            return new URL(givenURL);
        } catch (Exception e) {
            Log.e("connecting", "URL problem " + e.getMessage());
        }
        return null;
    }

    private String getPostDataString(HashMap<String, String> params) throws UnsupportedEncodingException {
        StringBuilder result = new StringBuilder();
        boolean first = true;
        for (Map.Entry<String, String> entry : params.entrySet()) {
            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
        }

        return result.toString();
    }

    @Override
    protected void onPostExecute(String result) {
        if (result != null) {
            Log.d("serverResult", result);
        }
        if (delegate != null) {
            delegate.processFinish(result);
        }
    }
}
