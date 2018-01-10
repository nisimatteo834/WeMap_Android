package mainApp;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
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

        // layout section
        TextView myTitle = (TextView) findViewById(R.id.textView);
        myTitle.setText("... and the Location" );

        ViewGroup.LayoutParams params = listView2.getLayoutParams();
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
            listView2.setBackground(gd);


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
