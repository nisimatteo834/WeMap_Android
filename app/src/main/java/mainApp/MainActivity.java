package mainApp;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.provider.Settings;
import android.text.format.Formatter;
import android.widget.TextView;

import org.json.JSONArray;

import java.net.MalformedURLException;

import mainApp.R;


public class MainActivity extends Activity
{
    LocationManager locationManager = null;
    WifiManager wifi = null;
    SqlQueries sqlquery = new SqlQueries();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        locationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        wifi = (WifiManager)getApplicationContext().getSystemService(Context.WIFI_SERVICE);


        try {
            this.updateValues();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void onPause(){
        super.onPause();
    }

    @Override
    protected void onStop(){
        super.onStop();
    }

    protected  void onResume(){
        super.onResume();
        try {
            this.updateValues();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

    }

    protected void onDestroy(){
        super.onDestroy();
    }

    private void updateValues() throws MalformedURLException {

        this.checkWifi();
        this.checkGPS();


        TextView ssid_value = (TextView) findViewById(R.id.SSID_value);
        ssid_value.setText(wifi.getConnectionInfo().getSSID());

        TextView ip_value = (TextView) findViewById(R.id.ip_value);
        ip_value.setText(Formatter.formatIpAddress(wifi.getConnectionInfo().getIpAddress()));

        TextView speed_value = (TextView) findViewById(R.id.speed_value);
        String s = String.valueOf(wifi.getConnectionInfo().getLinkSpeed()) + " Mbit/s";
        speed_value.setText(s);


        TextView mac_value = (TextView) findViewById(R.id.MAC_value);
        mac_value.setText(Wifi.getMacAddr());

        TextView rssi = (TextView) findViewById(R.id.rssi_value);
        rssi.setText(String.valueOf(wifi.getConnectionInfo().getRssi()));

        TextView GW = (TextView) findViewById(R.id.gateway_value);
        GW.setText(Formatter.formatIpAddress(wifi.getDhcpInfo().gateway));

        System.out.println("ciao");
        JSONArray jsonArray = null;

        jsonArray = sqlquery.getNamesAllRooms();


    }

    private boolean isLocationEnabled(LocationManager locationManager) {
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    private void showAlertGPS() {

        final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Enable Location")
                .setMessage("Your Locations Settings is set to 'Off'.\nPlease Enable Location to " +
                        "use this app")
                .setPositiveButton("Location Settings", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                        Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivity(myIntent);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                    }
                });
        dialog.show();
    }

    private void showAlertWIFI() {
        final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Enable Location")
                .setMessage("Your Wi-Fi is not Enabled.\nPlease Enable Wi-Fi to " +
                        "use this app")
                .setPositiveButton("Wi-Fi Settings", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                        Intent myIntent = new Intent(Settings.ACTION_WIFI_SETTINGS);
                        startActivity(myIntent);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                    }
                });
        dialog.show();
    }

    private void checkWifi(){

        try{
            if (!wifi.isWifiEnabled()) {

                this.showAlertWIFI();

            }

        }
        catch (NullPointerException npo){
            System.out.println(npo.getMessage());
        }


    }

    private void checkGPS(){
        if (!this.isLocationEnabled(locationManager))
        {
            this.showAlertGPS();
        }
    }

}
