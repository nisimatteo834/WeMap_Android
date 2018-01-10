package mainApp;

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
import android.content.Intent;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;





public class InstitutionActivity extends AppCompatActivity {




    public static String[] choices = new String[]{"Institution","location","building","inOutCorr", "roomName"};

    public static String[] getChoices() {
        return choices;
    }
    public static void setChoice(String choice, int position) {
        InstitutionActivity.choices[position] = choice;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.second_screen);

        final String[] institution = new String[]{"PoliTo"};
        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this, R.layout.row,institution);
        ListView listView1 = (ListView) findViewById(R.id.newlistview);
        listView1.setAdapter(adapter1);


        // layout section
        TextView myTitle =(TextView) findViewById(R.id.textView);
        myTitle.setText("Select the Institution..." );

        ViewGroup.LayoutParams params = listView1.getLayoutParams();
        //params.height= params.height + 50;

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
                listView1.setBackground(gd);


        listView1.setOnItemClickListener(new OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> av, View v, int pos, long id)
            {
                // Start NewActivity.class
                setChoice(institution[pos],0);
                Intent myIntent = new Intent(InstitutionActivity.this,
                        LocationActivity.class);
                startActivity(myIntent);

            }
        });
        /*ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this, R.layout.second_screen,location);
        listView.setAdapter(adapter2);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> av, View v, int pos, long id)
            {
                while (choices[1].equals("location")) {}
                choices[1] = location[pos];
            }
        });*/


    }
}
