package mainApp;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Array;

/**
 * Created by Stefano on 10/01/2018.
 */

public class RoomGridActivity extends AppCompatActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Get the view from new_activity.xml
        setContentView(R.layout.room_grid);
        Resources res = getResources();
        final String[] position = res.getStringArray(R.array.positions);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.row, position);

        // sostituiamo ListView con GridView
        GridView gridView = (GridView) findViewById(R.id.room_grid);
        gridView.setAdapter(adapter);


        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> av, View v, int pos, long id)
            {
                InstitutionActivity.setChoice(position[pos],5);
                Toast.makeText(getApplicationContext(),
                        "Selected: "+ position[pos], Toast.LENGTH_LONG).show();

                MainActivity.parameters.put("grid",position[pos]);

                Intent myIntent = new Intent(RoomGridActivity.this,
                        SaveActivity.class);
                startActivity(myIntent);
            }
        });

    }
}
