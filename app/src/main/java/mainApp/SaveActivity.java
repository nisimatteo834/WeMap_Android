package mainApp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Matteo on 13/12/2017.
 */

public class SaveActivity extends AppCompatActivity {

    RequestQueue requestQueue;
    //String insertUrl = "http://192.168.43.135:8080/insert.php";//"http://192.168.1.65/insert.php";
    String insertUrl = "http://188.216.115.130/insert.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.content_add);

        TextView insitution = (TextView) findViewById(R.id.institution);
        insitution.setText(InstitutionActivity.choices[0]);


        TextView location = (TextView) findViewById(R.id.location);
        location.setText(InstitutionActivity.choices[1]);


        TextView building = (TextView) findViewById(R.id.building);
        building.setText(InstitutionActivity.choices[2]);


        TextView enviroment = (TextView) findViewById(R.id.environment);
        enviroment.setText(InstitutionActivity.choices[3]);


        TextView room = (TextView) findViewById(R.id.room);
        room.setText(InstitutionActivity.choices[4]);

        requestQueue = Volley.newRequestQueue(getApplicationContext());

        Button save = (Button) findViewById(R.id.buttonSave);

        save.setOnClickListener(new View.OnClickListener() {
            public void onClick(View w) {


                StringRequest request = new StringRequest(Request.Method.POST, insertUrl, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        TextView result = (TextView) findViewById(R.id.results);
                        result.setText("DATA UPLOADED");
                        System.out.println("OK");

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        TextView result = (TextView) findViewById(R.id.results);
                        result.setText("PROBLEM IN UPLOADING");
                        System.out.println("NO" + error.getMessage());

                    }
                }) {

                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        //add this to remove the ""
                        Map<String, String> parameters = new HashMap<String, String>();
                        parameters = MainActivity.parameters;
                        return parameters;
                    }
                };
                request.setRetryPolicy(new RetryPolicy() {
                    @Override
                    public int getCurrentTimeout() {
                        return 5000;
                    }

                    @Override
                    public int getCurrentRetryCount() {
                        return 5;
                    }

                    @Override
                    public void retry(VolleyError error) throws VolleyError {
                        System.out.println("Retrying in upload" + this.getCurrentRetryCount());

                    }
                });
                requestQueue.add(request);
            }

        });


    }


}
