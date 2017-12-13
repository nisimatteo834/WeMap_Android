package mainApp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import org.json.*;


import android.widget.AdapterView.OnItemClickListener;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;


/**
 * Created by Stefano on 07/12/2017.
 */

public class RoomNameActivity extends AppCompatActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Get the view from new_activity.xml
        setContentView(R.layout.second_screen);


        try {
            JSONObject obj = new JSONObject("....");
            String[] TRooms;
        } catch (JSONException e) {
            e.printStackTrace();
        }


        final String[] roomName;
        final String[] IRooms = new String[]{"1i","2i","3i","4i","5i","6i","7i","8i","9i","10i","11i","12i"};
        final String[] MRooms = new String[]{"1M","2M","3M","4M"};
        final String[] NRooms = new String[]{"1N","2N","3N","4N"};
        if (InstitutionActivity.getChoices()[2].equals("Rooms I")) {
            roomName= IRooms;
        } else if (InstitutionActivity.getChoices()[2].equals("Rooms M")){
            roomName = MRooms;

        } else if (InstitutionActivity.getChoices()[2].equals("Rooms N")) {
            roomName = MRooms;
        } else { roomName= NRooms;}

        final  String[] corridorName = roomName.clone();
        int cnt = 0;
        for (String tmp:roomName){
            corridorName[cnt]=("In front of room " + tmp);
            cnt++;
        }
        final String[] toShow;

        if (InOutCorrActivity.getInCorr().equals("Inside a room")) {
            toShow = roomName;
        } else {
            toShow = corridorName;
        }
        ArrayAdapter<String> adapter5 = new ArrayAdapter<String>(this, R.layout.row, toShow);
        ListView listView5 = (ListView) findViewById(R.id.newlistview);
        listView5.setAdapter(adapter5);
        listView5.setOnItemClickListener(new OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> av, View v, int pos, long id)
            {
                InstitutionActivity.setChoice(toShow[pos],4);
                String[] toPrint = InstitutionActivity.getChoices();

                Intent myIntent = new Intent(RoomNameActivity.this,
                        SaveActivity.class);
                startActivity(myIntent);

                String message = toPrint[0] +"\n"+ toPrint[1]+"\n"+ toPrint[2]+"\n" + toPrint[3]+"\n" + toPrint[4];
                Toast.makeText(getApplicationContext(),message, Toast.LENGTH_LONG).show();


            }
        });


    }
}
