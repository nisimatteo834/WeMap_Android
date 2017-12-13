package mainApp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

/**
 * Created by Stefano on 06/12/2017.
 */

public class BuildingActivity extends AppCompatActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Get the view from new_activity.xml
        setContentView(R.layout.second_screen);

        final String[] building = new String[]{"Rooms I", "Rooms M", "Rooms N", "Rooms T", "ISMB"};

        ArrayAdapter<String> adapter3 = new ArrayAdapter<String>(this, R.layout.row, building);
        ListView listView3 = (ListView) findViewById(R.id.newlistview);
        listView3.setAdapter(adapter3);
        listView3.setOnItemClickListener(new OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> av, View v, int pos, long id)
            {
                // Start NewActivity.class
                InstitutionActivity.setChoice(building[pos],2);
                //if (building[pos].equals("Rooms I")) {
                if (true) {
                    Intent myIntent3 = new Intent(BuildingActivity.this,
                            mainApp.InOutCorrActivity.class);
                    startActivity(myIntent3);
                } else {
                    String message = building[pos] + " are not available yet...";
                    Toast.makeText(getApplicationContext(),message, Toast.LENGTH_LONG).show();
                }

            }
        });

    }
}
