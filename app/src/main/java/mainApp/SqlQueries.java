package mainApp;
import android.os.AsyncTask;
import android.util.Log;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import java.io.IOException;
import java.net.URL;


/**
 * Created by Navid on 12/1/2017.
 */

class DownloadFilesTask extends AsyncTask<String, String, HttpEntity> {

    @Override
    protected HttpEntity doInBackground(String... urls) {
        HttpEntity httpEntity = null;
        try {
            DefaultHttpClient httpClient = new DefaultHttpClient();  // Default HttpClient
            System.out.println(String.valueOf(urls));
            HttpPost httpPost = new HttpPost(String.valueOf(urls));
            HttpResponse httpResponse = httpClient.execute(httpPost);
            httpEntity = httpResponse.getEntity();

        } catch (ClientProtocolException e) {
            // Signals error in http protocol
            e.printStackTrace();
            //Log Errors Here
        } catch (IOException e) {
            e.printStackTrace();
        }
        return httpEntity;
    }}

public class SqlQueries {
    public JSONArray getAllRooms() {

        String url = "http://192.168.1.65/get_allrooms.php";
        HttpEntity httpEntity = null;
        httpEntity = (HttpEntity) new DownloadFilesTask().execute(url);//readingUrl(url);
        /*
        HttpEntity httpEntity = null;
        try {
            DefaultHttpClient httpClient = new DefaultHttpClient();  // Default HttpClient
            HttpPost httpPost = new HttpPost(url);
            HttpResponse httpResponse = httpClient.execute(httpPost);
            httpEntity = httpResponse.getEntity();

        } catch (ClientProtocolException e) {
        // Signals error in http protocol
        e.printStackTrace();
        //Log Errors Here
        } catch (IOException e) {
        e.printStackTrace();
        }*/
        // Convert HttpEntity into JSON Array
        JSONArray jsonArray = null;
        if (httpEntity != null) {
            try {
                String entityResponse = EntityUtils.toString(httpEntity);
                Log.e("Entity Response  : ", entityResponse);
                jsonArray = new JSONArray(entityResponse);
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return jsonArray;
    }

    public JSONArray getNamesAllRooms() {

        String url = "http://192.168.1.65/get_names_allrooms.php";
        HttpEntity httpEntity = null;
        httpEntity = (HttpEntity) new DownloadFilesTask().execute(url);
        //httpEntity = readingUrl(url);
        // Convert HttpEntity into JSON Array
        JSONArray jsonArray = null;
        if (httpEntity != null) {
            try {
                String entityResponse = EntityUtils.toString(httpEntity);
                Log.e("Entity Response  : ", entityResponse);
                jsonArray = new JSONArray(entityResponse);
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return jsonArray;
    }
}
