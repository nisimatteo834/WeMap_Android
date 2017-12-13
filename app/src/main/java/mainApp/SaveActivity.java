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
    String insertUrl = "http://192.168.43.135:8080//insert.php";//"http://192.168.1.65/insert.php";

    @Override
    protected void onCreate(Bundle savedInstanceState){
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

        Button save = (Button) findViewById(R.id.buttonSave);

        save.setOnClickListener(new View.OnClickListener(){
            public void onClick(View w){

                requestQueue = Volley.newRequestQueue(getApplicationContext());


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
                        //add this to remove the ""
                        Map<String, String> parameters = new HashMap<String, String>();
                        parameters = MainActivity.parameters;
                        return parameters;
                    }
                };
                requestQueue.add(request);

            }
        });



    }


}
