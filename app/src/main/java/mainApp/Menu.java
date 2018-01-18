package mainApp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

/**
 * Created by Matteo on 18/01/2018.
 */

public class Menu extends AppCompatActivity {

    @Override
    public boolean onCreateOptionsMenu(android.view.Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId())
        {
            case R.id.action_history:
            {
                Intent myIntent = new Intent(getApplicationContext(),History.class);
                startActivity(myIntent);
                break;
            }
            case R.id.action_add:
            {
                Intent myIntent = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(myIntent);
                break;
            }
            case R.id.action_signup:
            {
                Intent myIntent = new Intent(getApplicationContext(),History.class);
                startActivity(myIntent);
                break;
            }
        }


        return super.onOptionsItemSelected(item);
    }


}
