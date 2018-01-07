package mainApp;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;

import java.math.BigDecimal;
import java.net.NetworkInterface;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutionException;

import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.text.format.Formatter;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class Wifi extends BroadcastReceiver {

    private Context mContext;
    private WifiManager wifi;
    private static final double DISTANCE_MHZ_M = 27.55;
    private static final int MIN_RSSI = -100;
    private static final int MAX_RSSI = -55;



    public Wifi(Context context){
        this.mContext = context;
        this.wifi =  (WifiManager) this.mContext.getSystemService(Context.WIFI_SERVICE);
        wifi.setWifiEnabled(true);
    }

    public void updateValues() {

        if (isOnline() && this.isEnabled())
        {
            MainActivity.plus.setVisibility(View.VISIBLE);
        }

        else
        {
            MainActivity.plus.setVisibility(View.INVISIBLE);

        }

//wifi values updating
        String ssid = wifi.getConnectionInfo().getSSID();
        String ip = Formatter.formatIpAddress(wifi.getConnectionInfo().getIpAddress());
        Integer speed = wifi.getConnectionInfo().getLinkSpeed();

        int rssi = wifi.getConnectionInfo().getRssi();
        String gw = Formatter.formatIpAddress(wifi.getDhcpInfo().gateway);


        TextView ssid_value = (TextView)((Activity)mContext).findViewById(R.id.SSID_value);
        ssid_value.setText(ssid);

        TextView ip_value = (TextView) ((Activity)mContext).findViewById(R.id.ip_value);
        ip_value.setText(ip);

        TextView speed_value = (TextView) ((Activity)mContext).findViewById(R.id.speed_value);
        String s = String.valueOf(speed) + " Mbit/s";
        speed_value.setText(s);


        TextView mac_value = (TextView) ((Activity)mContext).findViewById(R.id.MAC_value);
        mac_value.setText(Wifi.getMacAddr());

        TextView mac_ap = (TextView) ((Activity)mContext).findViewById(R.id.apMAC_value);
        mac_ap.setText(wifi.getConnectionInfo().getBSSID());

        TextView rssi_tv = (TextView) ((Activity)mContext).findViewById(R.id.rssi_value);
        rssi_tv.setText(String.valueOf(rssi));

        TextView GW = (TextView) ((Activity)mContext).findViewById(R.id.gateway_value);
        GW.setText(gw);

        TextView accesspoints = (TextView) ((Activity)mContext).findViewById(R.id.accespoints_list);
        accesspoints.setText(this.ScanAP());

        TextView distance = (TextView) ((Activity)mContext).findViewById(R.id.distance_value);

        if(this.getFrequencyAndLevel()!=null)
        {
            String[] freqAndLevel = this.getFrequencyAndLevel().split(",");
            distance.setText(String.valueOf(this.calculateDistance(Integer.parseInt(freqAndLevel[0]), Integer.parseInt(freqAndLevel[1]))));
        }
        else {
            distance.setText(String.valueOf(-1));

        }


        TextView device = (TextView) ((Activity)mContext).findViewById(R.id.device_value);
        device.setText(Device.getDeviceName());




    }

    private String getFrequencyAndLevel() {
        Iterator<ScanResult> iterator = wifi.getScanResults().iterator();
        int frequency = 0;

        List<AccessPoint> aps = new ArrayList();
        while (iterator.hasNext()) {
            ScanResult next = iterator.next();
            if (next.BSSID.equals(wifi.getConnectionInfo().getBSSID())) {
                return String.valueOf(next.frequency) + "," + String.valueOf(next.level);
            }


        }
        return null;
    }

    private String ScanAP() {
        Iterator<ScanResult> iterator = wifi.getScanResults().iterator();
        String accesspoints = "";


        List<AccessPoint> aps = new ArrayList();
        while (iterator.hasNext()) {
            ScanResult next = iterator.next();
            aps.add(new AccessPoint(next.SSID, String.valueOf(0), next.level, next.frequency));
        }

        Iterator<AccessPoint> iterator1 = aps.iterator();

        while (iterator1.hasNext()) {
            AccessPoint next = iterator1.next();
            accesspoints = accesspoints + next.toString();
        }

        return accesspoints;
    }


    public BigDecimal calculateDistance(int frequency, int level) {
        BigDecimal bd = new BigDecimal(Double.toString(Math.pow(10.0, (DISTANCE_MHZ_M - (20 * Math.log10(frequency)) + Math.abs(level)) / 20.0)));
        bd.setScale(2, BigDecimal.ROUND_HALF_UP);
        return bd;
    }


    public boolean isEnabled(){
        return wifi.isWifiEnabled();
    }

    public static String getMacAddr() {
        try {
            List<NetworkInterface> all = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface nif : all) {
                if (!nif.getName().equalsIgnoreCase("wlan0")) continue;

                byte[] macBytes = nif.getHardwareAddress();
                if (macBytes == null) {
                    return "";
                }

                StringBuilder res1 = new StringBuilder();
                for (byte b : macBytes) {
                    res1.append(Integer.toHexString(b & 0xFF) + ":");
                }

                if (res1.length() > 0) {
                    res1.deleteCharAt(res1.length() - 1);
                }
                return res1.toString();
            }
        } catch (Exception ex) {
            //handle exception
        }
        return "";
    }


    @Override
    public void onReceive(Context context, Intent intent) {
        ConnectivityManager conMan = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = conMan.getActiveNetworkInfo();
        Toast toast = null;
        if (netInfo != null && netInfo.getType() == ConnectivityManager.TYPE_WIFI)
        {   toast = Toast.makeText(mContext,"Have Wifi Connection", Toast.LENGTH_LONG);
            toast.show();
            Log.d("WifiReceiver", "Have Wifi Connection");}
        else
        {
            toast = Toast.makeText(mContext,"Have Wifi Connection", Toast.LENGTH_LONG);
            toast.show();
            Log.d("WifiReceiver", "Don't have Wifi Connection");}
    }

    public boolean isOnline() {
        ConnectivityManager connMgr = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }

    public void setWifiEnabled(Boolean bool){
        wifi.setWifiEnabled(bool);
    }


}