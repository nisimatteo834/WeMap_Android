package mainApp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TableRow;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

/**
 * Created by Matteo on 13/12/2017.
 */

public class SaveActivity extends Menu {

    RequestQueue requestQueue;
    //String insertUrl = "http://192.168.43.135:8080/insert.php";//"http://192.168.1.65/insert.php";
    //String insertUrl = "http://192.168.1.81:8080/insert.php";//"http://188.216.115.130/insert.php";
    //String insertUrl = "http://5.89.130.153/insert.php";
    String insertUrl = "http://wemapserver.sytes.net/insert.php";
    boolean flag = false;

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
        TextView lat = (TextView) findViewById(R.id.lat);
        TextView lon = (TextView) findViewById(R.id.lon);
        TextView speed = (TextView) findViewById(R.id.speed_test_value);
        speed.setText(MainActivity.parameters.get("speedInternet"));

        if (!(InstitutionActivity.choices[3].equals("Inside a room"))){

            TableRow grid = (TableRow) findViewById(R.id.grid);
            grid.setVisibility(View.INVISIBLE);
        }

        else {
            TextView grid_t = (TextView) findViewById(R.id.grid_t);
            grid_t.setText(InstitutionActivity.getChoices()[5]);
            TableRow grid = (TableRow) findViewById(R.id.grid);
            grid.setVisibility(View.VISIBLE);
        }

        if (InstitutionActivity.choices[3].equals("Outside the building")) {
            room.setText("/");
            TableRow lat_row = (TableRow) findViewById(R.id.lat_row);
            TableRow lon_row = (TableRow) findViewById(R.id.lon_row);
            lat_row.setVisibility(View.VISIBLE);
            lon_row.setVisibility(View.VISIBLE);
            lat.setText(MainActivity.parameters.get("latitude"));
            lon.setText(MainActivity.parameters.get("longitude"));

        } else {
            room.setText(InstitutionActivity.choices[4]);
        }

        if (InstitutionActivity.choices[3].equals("In a corridor/hall")){
            room.setText("/");
            TableRow lat_row = (TableRow) findViewById(R.id.lat_row);
            TableRow lon_row = (TableRow) findViewById(R.id.lon_row);
            lat_row.setVisibility(View.VISIBLE);
            lon_row.setVisibility(View.VISIBLE);
            lat.setText(MainActivity.parameters.get("latitude"));
            lon.setText(MainActivity.parameters.get("longitude"));

            if (MainActivity.parameters.containsKey("qrcode")){
                TableRow QR = (TableRow) findViewById(R.id.qr);
                QR.setVisibility(View.VISIBLE);
                TextView qr_value = (TextView) findViewById(R.id.qr_value);
                qr_value.setText(MainActivity.parameters.get("qrcode"));

                TableRow lat_QR = (TableRow) findViewById(R.id.lat_qr);
                lat_QR.setVisibility(View.VISIBLE);
                TextView lat_qr_value = (TextView) findViewById(R.id.lat_qr_value);
                lat_qr_value.setText(MainActivity.parameters.get("latitude_qr"));

                TableRow long_QR = (TableRow) findViewById(R.id.long_qr);
                long_QR.setVisibility(View.VISIBLE);
                TextView long_qr_value = (TextView) findViewById(R.id.long_qr_value);
                long_qr_value.setText(MainActivity.parameters.get("longitude_qr"));

            }

        }

        requestQueue = Volley.newRequestQueue(getApplicationContext());

        Button save = (Button) findViewById(R.id.buttonSave);

        save.setOnClickListener(new View.OnClickListener() {
            public void onClick(View w) {

                requestQueue = Volley.newRequestQueue(getApplicationContext());


                StringRequest request = new StringRequest(Request.Method.POST, insertUrl, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {


                        if (response.contains("wifiauth.polito.it")) {
                            TextView result = (TextView) findViewById(R.id.results);
                            result.setText("AUTH TO THE NETWORK");
                            result.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://wifiauth.polito.it"));
                                    startActivity(browserIntent);
                                    TextView result = (TextView) findViewById(R.id.results);
                                            result.setText("");
                                    flag = true;
                                }
                            });
                            System.out.println("OK");
                        } else {
                            TextView result = (TextView) findViewById(R.id.results);
                            result.setText("DATA UPLOADED");
                            System.out.println("OK");
                        }
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

                        if (flag)
                        {
                            String result = "";
                            try {
                                result = new SpeedTestTask(getApplicationContext()).execute().get();

                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            } catch (ExecutionException e) {
                                e.printStackTrace();
                            }

                            System.out.println("Result: "+ result);
                            flag = false;
                            MainActivity.parameters.put("speedInternet",result);
                        }

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
