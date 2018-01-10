package mainApp;

import android.app.Fragment;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.view.View;


/**
 * Created by Stefano on 09/01/2018.
 */

public class OnboardingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);

        final TextView myTitle = (TextView) findViewById(R.id.tutorialTitle);
        final TextView myText = (TextView) findViewById(R.id.tutorialMessage);
        myTitle.setText(R.string.title_1);
        myText.setText(R.string.text_1);

     /*   GradientDrawable gd = new GradientDrawable();

        // Specify the shape of drawable
        gd.setShape(GradientDrawable.RECTANGLE);

        // Set the fill color of drawable
        gd.setColor(Color.WHITE); // make the background transparent

        // Create a 2 pixels width red colored border for drawable
        gd.setStroke(3, Color.BLUE); // border width and color

        // Make the border rounded
        gd.setCornerRadius(15.0f); // border corner radius

        // Finally, apply the GradientDrawable as TextView background
        myTitle.setBackground(gd);
        myText.setBackground(gd);*/

        Button goOn = (Button) findViewById(R.id.bottomButton);
        goOn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Counter.Increment();
                switch (Counter.Total()) {
                    case 2:
                        myTitle.setText(R.string.title_2);
                        myText.setText(R.string.text_2);
                        break;

                    case 3:
                        myTitle.setText(R.string.title_3);
                        myText.setText(R.string.text_3);
                        break;
                    case 4:
                        myTitle.setText(R.string.title_4);
                        myText.setText(R.string.text_4);
                        break;
                    case 5:
                        myTitle.setText(R.string.title_5);
                        myText.setText(R.string.text_5);
                        break;
                    case 6:
                        myTitle.setText(R.string.title_6);
                        myText.setText(R.string.text_6);
                        break;
                    case 7:
                        myTitle.setText(R.string.title_7);
                        myText.setText("");
                        break;
                    case 8:
                        // go to MainActivity
                        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
                        SharedPreferences.Editor edit = prefs.edit();
                        edit.putBoolean(getString(R.string.pref_previously_started), Boolean.TRUE);
                        edit.apply();
                        finish();


                    default:
                        Counter.setCnt(1);
                        myTitle.setText(R.string.title_7);
                        myText.setText("");


                }
            }

        });

    }

}
