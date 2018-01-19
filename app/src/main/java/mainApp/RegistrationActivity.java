package mainApp;

import android.app.ProgressDialog;
import android.app.VoiceInteractor;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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

/**
 * Created by Navid on 1/16/2018.
 */

public class RegistrationActivity extends mainApp.Menu{
    String registrationUrl = "http://wemapserver.sytes.net//registration.php";
    String userUrl = "http://wemapserver.sytes.net//user.php";
    private EditText editTextPhoneNo,editTextEmail;
    private Button buttonRegister,buttonSkip;
    private ProgressDialog progressDialog;
    private Wifi wifi = null;
    private Device device;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        editTextEmail = (EditText) findViewById(R.id.email);
        editTextPhoneNo = (EditText) findViewById(R.id.phoneNumber);

        buttonRegister = (Button) findViewById(R.id.buttonRegister);
        buttonSkip= (Button) findViewById(R.id.buttonSkip);

        progressDialog = new ProgressDialog(this);

        wifi = new Wifi(RegistrationActivity.this);
        device = new Device();
        final String mac = wifi.getMacAddr();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, userUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                System.out.println(response.toString());
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray users = jsonObject.getJSONArray("user");
                    JSONObject user = users.getJSONObject(0);
                    String phone_no = user.getString("phone_no");
                    String email = user.getString("email");
                    editTextEmail.setText(email);
                    editTextPhoneNo.setText(phone_no);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.append(error.getMessage());
                Toast.makeText(RegistrationActivity.this, "Something went wrong",Toast.LENGTH_LONG).show();

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError{
                Map<String,String> parameters = new HashMap<String,String>();
                parameters.put("phone_mac", mac);
                return parameters;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();
            }
        });

        buttonSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(myIntent);
            }
        });

    }
    private void registerUser(){
        final String email = editTextEmail.getText().toString().trim();
        final String phone_No = editTextPhoneNo.getText().toString().trim();
        final String phoneMac = wifi.getMacAddr();
        final String phoneModel = device.getDeviceName();

        progressDialog.setMessage("Registering user...");
        progressDialog.show();
        StringRequest request = new StringRequest(Request.Method.POST, registrationUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();

            }
        },
            new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.hide();
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError{
                Map<String, String> parameters = new HashMap<String, String>();
                parameters.put("email",email );
                parameters.put("phone_no",phone_No );
                parameters.put("phone_mac",phoneMac );
                parameters.put("model",phoneModel );
                return parameters;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);
    }

}
