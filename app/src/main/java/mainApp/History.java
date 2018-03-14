package mainApp;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Matteo on 18/01/2018.
 */

public class History extends Menu {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String historyurl = "http://wemapserver.sytes.net/history.php";
        //String historyurl = "http://wemapserver.sytes.net/history_Matteo.php";
        Wifi wifi = new Wifi(getApplicationContext());
        final String mac = wifi.getMacAddr();
        historyurl += "?phone_mac="+mac;
        setContentView(R.layout.history_screen);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, historyurl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                System.out.println(response.toString());
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray history = jsonObject.getJSONArray("history");
                    final List<String> result = new ArrayList<String>();


                    for (int i=0; i<history.length(); i++){
                        String toParse = "";
                        JSONObject hist = (JSONObject) history.get(i);
                        toParse += "Room: ";
                        toParse += hist.get("name");
                        toParse += "\n";
                        toParse += "Power: ";
                        toParse += hist.get("rssi");
                        toParse += "\n";
                        toParse += "Speed: ";
                        toParse += hist.get("speedInternet");
                        toParse += "\n";
                        toParse += "Device: ";
                        toParse += hist.get("phone_mac");
                        toParse += "\n";
                        toParse += "Date: ";
                        toParse += hist.get("time");

                        result.add(toParse);
                    }

                    final ArrayAdapter<String> adapter4 = new ArrayAdapter<String>(getApplicationContext(), R.layout.row_history, result);
                    ListView listView4 = (ListView) findViewById(R.id.newlistview);
                    listView4.setAdapter(adapter4);

                    // layout section
                    TextView myTitle = (TextView) findViewById(R.id.textView);
                    myTitle.setText("History" );
                    //params.height= 110;



                    System.out.println(history.toString());


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.append(error.getMessage());
                Toast.makeText(getApplicationContext(), "Something went wrong",Toast.LENGTH_LONG).show();

            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);



    }
}
