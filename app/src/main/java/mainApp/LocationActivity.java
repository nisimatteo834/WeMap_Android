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
 * Created by Stefano on 07/12/2017.
 */

public class LocationActivity extends AppCompatActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Get the view from new_activity.xml
        setContentView(R.layout.second_screen);

        final String[] location = new String[]{"Cittadella Politecnica"};
        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this, R.layout.row, location);
        ListView listView2 = (ListView) findViewById(R.id.newlistview);
        listView2.setAdapter(adapter2);
        listView2.setOnItemClickListener(new OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> av, View v, int pos, long id)
            {
                // Start NewActivity.class
                InstitutionActivity.setChoice(location[pos],1);
                Intent myIntent2 = new Intent(LocationActivity.this,
                        BuildingActivity.class);
                startActivity(myIntent2);

            }
        });


    }
}
