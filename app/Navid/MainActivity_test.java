package com.example.asus.azure_mobileapp;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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

import java.util.HashMap;
import java.util.Map;


public class MainActivity extends AppCompatActivity {

    EditText id, name, quantity;
    Button insert, show;
    RequestQueue requestQueue;
    String insertUrl = "http://192.168.1.65/insert_test.php";
    String showUrl = "http://192.168.1.65/read_test.php";
    TextView result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        id = (EditText) findViewById(R.id.editTextId);
        name = (EditText) findViewById(R.id.editTextName);
        quantity = (EditText) findViewById(R.id.editTextQuantity);
        insert = (Button) findViewById(R.id.insert);
        show = (Button) findViewById(R.id.showItems);
        result = (TextView) findViewById(R.id.textView);


        requestQueue = Volley.newRequestQueue(getApplicationContext());

        show.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                //System.out.println("ww");
                //JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                //        (Request.Method.GET, showUrl, new Response.Listener<JSONObject>() {
                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                        (Request.Method.GET, showUrl, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        //System.out.println(response.toString());
                        try {
                            JSONArray inventory = response.getJSONArray("inventory");
                            for (int i = 0; i < inventory.length(); i++) {
                                JSONObject student = inventory.getJSONObject(i);

                                String id = student.getString("id");
                                String name = student.getString("name");
                                String quantity = student.getString("quantity");

                                result.append(id + " " + name + " " + quantity + " \n");
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

        insert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StringRequest request = new StringRequest(Request.Method.POST, insertUrl, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        System.out.println(response.toString());
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }) {

                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String,String> parameters  = new HashMap<String, String>();
                        parameters.put("id",id.getText().toString());
                        parameters.put("name",name.getText().toString());
                        parameters.put("quantity",quantity.getText().toString());

                        return parameters;
                    }
                };
                requestQueue.add(request);
            }

        });
    }
}


