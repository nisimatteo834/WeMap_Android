package mainApp;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.math.BigDecimal;

import fr.bmartel.speedtest.SpeedTestReport;
import fr.bmartel.speedtest.SpeedTestSocket;
import fr.bmartel.speedtest.inter.IRepeatListener;
import fr.bmartel.speedtest.inter.ISpeedTestListener;
import fr.bmartel.speedtest.model.SpeedTestError;

import static java.lang.Thread.sleep;
import static java.util.logging.Logger.global;

/**
 * Created by Matteo on 03/12/2017.
 */

public class SpeedTestTask extends AsyncTask<Void, Integer, String> {

    private final Context context;

    public SpeedTestTask(Context context){
        this.context = context;
    }

    public static String bitrate = "";
    @Override
    protected String doInBackground(Void... params) {

        SpeedTestSocket speedTestSocket = new SpeedTestSocket();
        speedTestSocket.setUploadSetupTime(4000);
        // add a listener to wait for speedtest completion and progress
        speedTestSocket.addSpeedTestListener(new ISpeedTestListener() {

            @Override
            public void onCompletion(SpeedTestReport report) {
                // called when download/upload is finished
                Log.v("speedtest", "[COMPLETED] rate in octet/s : " + report.getTransferRateOctet());
                Log.v("speedtest", "[COMPLETED] rate in bit/s   : " + report.getTransferRateBit());

                BigDecimal powerofsix = new BigDecimal(Math.pow(10,6));
                BigDecimal inMbit = report.getTransferRateBit().divide(powerofsix,2,BigDecimal.ROUND_HALF_UP);
                SpeedTestTask.bitrate = inMbit.toString();

            }

            @Override
            public void onError(SpeedTestError speedTestError, String errorMessage) {
                // called when a download/upload error occur
            }

            @Override
            public void onProgress(float percent, SpeedTestReport report) {
                // called to notify download/upload progress
//                Log.v("speedtest", "[PROGRESS] progress : " + percent + "%");
//                Log.v("speedtest", "[PROGRESS] rate in octet/s : " + report.getTransferRateOctet());
//                Log.v("speedtest", "[PROGRESS] rate in bit/s   : " + report.getTransferRateBit());
            }
        });

        //speedTestSocket.startFixedDownload("2.testdebit.info",80);
        speedTestSocket.startDownload("ftp://speedtest.tele2.net/20MB.zip");

        //speedTestSocket.startFixedDownload("ftp://speedtest.tele2.net/20MB.zip",10000);

        return SpeedTestTask.bitrate;
    }
}