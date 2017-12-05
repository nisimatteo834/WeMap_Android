
package mainApp;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class SqlQueries extends AppCompatActivity {
    Button show;
    RequestQueue requestQueue;
    String showUrl = "http://192.168.1.65/get_names_allrooms.php";
    TextView result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        show = (Button) findViewById(R.id.buttonShow);
        result = (TextView) findViewById(R.id.textViewResult);

        requestQueue = Volley.newRequestQueue(getApplicationContext());

        show.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                        (Request.Method.GET, showUrl, null, new Response.Listener<JSONObject>() {

                            @Override
                            public void onResponse(JSONObject response) {
                                //System.out.println(response.toString());
                                try {
                                    JSONArray allrooms = response.getJSONArray("allrooms");
                                    for (int i = 0; i < allrooms.length(); i++) {
                                        JSONObject room = allrooms.getJSONObject(i);

                                        String building = room.getString("building");
                                        String name = room.getString("name");
                                        String floor = room.getString("floor");
                                        String campus = room.getString("campus");

                                        result.append(building + " " + name + " " + floor + " " + campus + " \n");
                                    }
                                    result.append("===\n");

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                System.out.append(error.getMessage());

                            }
                        });
                requestQueue.add(jsonObjectRequest);
            }
        });


    }


}
