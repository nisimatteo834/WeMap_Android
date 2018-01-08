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

public class InOutCorrActivity extends AppCompatActivity {

    public static String inCorr;
    public static void setInCorr(String newInCorr) {
        inCorr = newInCorr;
    }

    public static String getInCorr() {
        return inCorr;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Get the view from new_activity.xml
        setContentView(R.layout.second_screen);

        final String[] inOutCorr = new String[]{"Inside a room", "In a corridor/hall", "Outside the building"};
        ArrayAdapter<String> adapter4 = new ArrayAdapter<String>(this, R.layout.row, inOutCorr);
        ListView listView4 = (ListView) findViewById(R.id.newlistview);
        listView4.setAdapter(adapter4);
        listView4.setOnItemClickListener(new OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> av, View v, int pos, long id)
            {
                // Start NewActivity.class
                InstitutionActivity.setChoice(inOutCorr[pos],3);
                if (inOutCorr[pos].equals("Inside a room") || inOutCorr[pos].equals("In a corridor/hall")) {
                    setInCorr(inOutCorr[pos]);
                    Intent myIntent4 = new Intent(InOutCorrActivity.this,
                            RoomNameActivity.class);
                    startActivity(myIntent4);
                }

                else{
                    setInCorr(inOutCorr[pos]);
                    Intent myIntent4 = new Intent(InOutCorrActivity.this,
                            SaveActivity.class);
                    startActivity(myIntent4);
                }
            }
        });


    }



}
