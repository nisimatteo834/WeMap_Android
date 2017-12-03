package mainApp;

/**
 * Created by Matteo on 29/11/2017.
 */

public class AccessPoint {

    private String ssid;
    private String ip;
    private String rssi;
    private int frequency;

    public AccessPoint(String ssid, String ip, int rssi, int frequency) {
        this.ssid = ssid;
        this.ip = ip;
        this.rssi = String.valueOf(rssi);
        this.frequency = frequency;
    }

    public int getFrequency() {
        return frequency;
    }

    public String getSsid() {
        return ssid;
    }

    public String getIp() {
        return ip;
    }

    public String getRssi() {
        return rssi;
    }

    public String toString(){
        return "ssid:" + this.ssid +", frequency:" + String.valueOf(this.frequency) + ", power:" + this.rssi + "\n";
    }
}
