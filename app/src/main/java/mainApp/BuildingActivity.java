package mainApp;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
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

        // layout section
        TextView myTitle = (TextView) findViewById(R.id.textView);
        myTitle.setText("Select the closest building" );
        ViewGroup.LayoutParams params = listView3.getLayoutParams();
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
            listView3.setBackground(gd);


        //layout section end



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
