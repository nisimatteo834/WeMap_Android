package mainApp;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Matteo on 14/01/2018.
 */

public class Corridor extends AppCompatActivity {
    private IntentIntegrator qrScan;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.second_screen);



        final String[] corrChoice = new String[]{"Scan QR code", "Use your Lat and Long","Use corridor name division"};
        qrScan = new IntentIntegrator(this);


        ArrayAdapter<String> adapter4 = new ArrayAdapter<String>(this, R.layout.row, corrChoice);
        ListView listView4 = (ListView) findViewById(R.id.newlistview);
        listView4.setAdapter(adapter4);

        // layout section
        TextView myTitle = (TextView) findViewById(R.id.textView);
        myTitle.setText("Corridor Options" );
        ViewGroup.LayoutParams params = listView4.getLayoutParams();
        //params.height= 110;

        // Initialize a new GradientDrawable
        GradientDrawable gd = new GradientDrawable();

        // Specify the shape of drawable
        gd.setShape(GradientDrawable.RECTANGLE);

        // Set the fill color of drawable
        gd.setColor(Color.DKGRAY); // make the background transparent

        // Create a 2 pixels width red colored border for drawable
        gd.setStroke(7, Color.CYAN); // border width and color

        // Make the border rounded
        gd.setCornerRadius(40.0f); // border corner radius
        // Finally, apply the GradientDrawable as TextView background
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1)
            listView4.setBackground(gd);


        listView4.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> av, View v, int pos, long id)
            {

                if (corrChoice[pos].equals(corrChoice[0])){
                    qrScan.initiateScan();
                }

                else if (corrChoice[pos].equals(corrChoice[2])){
                    Intent myIntent = new Intent(Corridor.this,RoomNameActivity.class);
                    startActivity(myIntent);
                    if (MainActivity.parameters.containsKey("qrcode"))
                        MainActivity.parameters.remove("qrcode");

                }

                else{
                    Intent myIntent4 = new Intent(Corridor.this,
                            SaveActivity.class);
                    startActivity(myIntent4);
                    if (MainActivity.parameters.containsKey("qrcode"))
                        MainActivity.parameters.remove("qrcode");
                }
            }
        });

    }

    //Getting the scan results
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            //if qrcode has nothing in it
            if (result.getContents() == null) {
                Toast.makeText(this, "Result Not Found", Toast.LENGTH_LONG).show();
            } else {
                //if qr contains data
                try {
                    //converting the data to json
                    String qrDetected = result.getContents();
                    qrDetected = qrDetected.replaceAll("\\s+","");
                    qrDetected = qrDetected.replaceAll("[{}]","");
                    Map<String, String> myMap = new HashMap<String, String>();
                    String[] pairs = qrDetected.split(",");
                    for (int i=0;i<pairs.length;i++) {
                        String pair = pairs[i];
                        String[] keyValue = pair.split(":");
                        myMap.put(keyValue[0], keyValue[1]);
                    }

                    MainActivity.parameters.put("latitude_qr",myMap.get("lat"));
                    MainActivity.parameters.put("longitude_qr",myMap.get("long"));
                    MainActivity.parameters.put("qrcode",myMap.get("qrcode"));


                    Toast.makeText(this, qrDetected, Toast.LENGTH_LONG).show();
                    InstitutionActivity.setChoice(qrDetected,7);
                    Intent myIntent = new Intent(getApplicationContext(),SaveActivity.class);
                    startActivity(myIntent);

                } catch (Exception e) {
                    e.printStackTrace();
                    //if control comes here
                    //that means the encoded format not matches
                    //in this case you can display whatever data is available on the qrcode
                    //to a toast
                    System.out.println(e.getMessage());
                    Toast.makeText(this, result.getContents(), Toast.LENGTH_LONG).show();
                }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }




}
