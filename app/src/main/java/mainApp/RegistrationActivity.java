package mainApp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.SyncStateContract;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Navid on 1/16/2018.
 */

public class RegistrationActivity extends mainApp.Menu implements View.OnClickListener{
    String registraionUrl = "http://5.89.130.153/registration.php";
    private EditText eidtTextPhoneNo,editTextEmail;
    private Button buttonRegister,buttonSkip;
    private ProgressDialog progressDialog;
    private Wifi wifi = null;
    private Device device;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        editTextEmail = (EditText) findViewById(R.id.email);
        eidtTextPhoneNo = (EditText) findViewById(R.id.phoneNumber);

        buttonRegister = (Button) findViewById(R.id.buttonRegister);
        buttonSkip= (Button) findViewById(R.id.buttonSkip);

        progressDialog = new ProgressDialog(this);

        wifi = new Wifi(RegistrationActivity.this);
        device = new Device();
        //progressDialog.setMessage();


    }
    private void registerUser(){
        final String email = editTextEmail.getText().toString().trim();
        final String phone_No = eidtTextPhoneNo.getText().toString().trim();
        final String phoneMac = wifi.getMacAddr();
        final String phoneModel = device.getDeviceName();

        progressDialog.setMessage("Registering user...");
        progressDialog.show();
        StringRequest request = new StringRequest(Request.Method.POST, registraionUrl, new Response.Listener<String>() {
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
                //return super.getParams();
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);
    }

    @Override

    public void onClick(View view) {
        if (view == buttonRegister)
            registerUser();

    }
}
