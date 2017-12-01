package mainApp;
import android.util.Log;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import java.io.IOException;


/**
 * Created by Navid on 12/1/2017.
 */

public class SqlQueries {

    public HttpEntity readingUrl(String url){
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
        }
        return httpEntity;
    }

    public JSONArray getAllRooms() {

        String url = "http://192.168.1.65/get_allrooms.php";
        HttpEntity httpEntity = null;
        httpEntity = readingUrl(url);
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
        httpEntity = readingUrl(url);
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
