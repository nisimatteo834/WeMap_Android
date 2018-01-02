package mainApp;

import android.app.Activity;
import android.content.Context;
import android.location.GnssMeasurement;
import android.location.GnssMeasurementsEvent;
import android.location.GnssStatus;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.widget.TextView;

import java.util.Collection;

/**
 * Created by Matteo on 02/01/2018.
 */

@RequiresApi(api = Build.VERSION_CODES.N)
public class GnssData {


    public GnssStatus mGnssStatus = null;
    public GnssMeasurementsEvent.Callback mGnssMeasurementListener;
    public GnssStatus.Callback mGnssStatusCallback;
    Context mcontext = null;

    public GnssData(Context context){
        this.mcontext = context;
        this.addGnssStatusCallBack();
        this.addGnssMeasurementListener();

    }

    private void addGnssStatusCallBack()
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            mGnssStatusCallback = new GnssStatus.Callback() {
                @Override
                public void onStarted() {
                    super.onStarted();
                    System.out.println("Gnss started");
                }

                @Override
                public void onStopped() {
                    super.onStopped();
                    System.out.println("Gnss stopped");
                }

                @Override
                public void onFirstFix(int ttffMillis) {
                    super.onFirstFix(ttffMillis);
                }

                @Override
                public void onSatelliteStatusChanged(GnssStatus status) {
                    mGnssStatus = status;
                    super.onSatelliteStatusChanged(status);
                }
            };

        }
    }
    private void addGnssMeasurementListener(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            mGnssMeasurementListener = new GnssMeasurementsEvent.Callback() {
                @RequiresApi(api = Build.VERSION_CODES.N)
                @Override
                public void onGnssMeasurementsReceived(GnssMeasurementsEvent eventArgs) {
                    Collection<GnssMeasurement> measurements = eventArgs.getMeasurements();
                    String s = "";
                    for (GnssMeasurement m : measurements){
                        String constellation = "None";
                        switch (m.getConstellationType())
                        {
                            case GnssStatus.CONSTELLATION_BEIDOU:
                            {
                                constellation = "BDU";
                                break;}
                            case GnssStatus.CONSTELLATION_GALILEO:{
                                constellation = "GAL";
                                break;
                            }
                            case GnssStatus.CONSTELLATION_GLONASS:{
                                constellation = "GLN";
                                break;
                            }
                            case GnssStatus.CONSTELLATION_GPS:{
                                constellation="GPS";
                                break;
                            }

                            case GnssStatus.CONSTELLATION_QZSS:{
                                constellation="QZSS";
                                break;
                            }
                            case GnssStatus.CONSTELLATION_SBAS:{
                                constellation="SBAS";
                                break;
                            }
                            case GnssStatus.CONSTELLATION_UNKNOWN:{
                                constellation="UNK";
                                break;
                            }

                        }


                        s += "sat:" + constellation+Integer.toString(m.getSvid()) + ' ';
                        s += "pdr:"+Double.toString(m.getPseudorangeRateMetersPerSecond()) + ' ';
                        if (m.hasSnrInDb()){
                            s +=  "snr:"+Double.toString(m.getSnrInDb()) + ' ';
                        }

                        if (m.hasCarrierCycles()){
                            s+= "cyc:" + Long.toString(m.getCarrierCycles()) + ' ';
                            if (m.hasCarrierFrequencyHz()){
                                s+="freq:" + Float.toString(m.getCarrierFrequencyHz());
                            }
                        }

                        s+= "Cn0:" + Double.toString(m.getCn0DbHz())+ ' ';

                        s+='\n';


                    }

                    TextView pdr = (TextView) ((Activity)mcontext).findViewById(R.id.pdr_value);
                    pdr.setText(s);

//                    System.out.println("MEASUREMENT RECEIVED");
                     super.onGnssMeasurementsReceived(eventArgs);
                }

                @Override
                public void onStatusChanged(int status) {
                    super.onStatusChanged(status);
                }
            };
        }

        else {
            System.out.print("Devices not enabled for GNSS measurements");
        }
    }

}
