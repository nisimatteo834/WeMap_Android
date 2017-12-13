package mainApp;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.content.Intent;
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
