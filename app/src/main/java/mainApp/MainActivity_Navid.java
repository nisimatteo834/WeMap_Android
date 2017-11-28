package mainApp;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TextView;

import mainApp.R;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class MainActivity_Navid extends AppCompatActivity {
    //Initialize connection variables.
    private static final String host = "wemap.mysql.database.azure.com";// Server name
    private static final String database = "navid_test"; //Data base name
    private static final String user = "wemap_by_MNS@wemap";//Server admin login name
    private static final String password = "Mn78nYSC65";// Admin password

    private static final String url = String.format("jdbc:mysql://%s/%s", host, database);

    private TextView id, name, quantity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_navid);
        TableLayout tl = (TableLayout) findViewById(R.id.tableLayout);

        id = (TextView) findViewById(R.id.textViewId);
        name = (TextView) findViewById(R.id.textViewName);
        quantity = (TextView) findViewById(R.id.textViewQuantity);
        Button buttonLoad = (Button) findViewById(R.id.buttonLoad);
        buttonLoad.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //new MyTask().execute();
                MyTask task = new MyTask();
                task.execute();
                //task.executeOnExecutor(AsyncTask.SERIAL_EXECUTOR);
                //task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            }
        });
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    private class MyTask extends AsyncTask<Void, Void, Void>{
        private String theId="",theName="",theQuantity="";
        //AlertDialog alertDialog;
        //Context context;
        @Override
        protected Void doInBackground(Void... arg0){
            // TODO Auto-generated method stub
            /*String login_url = "http://192.168.1.65/login.php";
            try {
            URL url = new URL(login_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoInput(true);
                httpURLConnection.setDoOutput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));
                //String post_data = URLEncoder.encode()
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();
                InputStream inputSteam = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputSteam,"iso-8859-1"));
                String result ="";
                String line="";
                while((line = bufferedReader.readLine())!= null){
                    result += line;
                }
                bufferedReader.close();
                inputSteam.close();
                httpURLConnection.disconnect();

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }*/

            //Set connection properties.
            Properties properties = new Properties();
            properties.setProperty("user", user);
            properties.setProperty("password", password);
            properties.setProperty("useSSL", "true");
            properties.setProperty("verifyServerCertificate", "true");
            properties.setProperty("requireSSL", "false");
            // get connection
            try {
                try {
                    Class.forName("com.mysql.jdbc.Driver");
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
                Connection con = DriverManager.getConnection(url, properties);

                Statement st = con.createStatement();
                String sql = "select * from inventory";

                final ResultSet rs = st.executeQuery(sql);


                rs.next();
                theId =rs.getString(1);
                theName = rs.getString(2);
                theQuantity = rs.getString(3);

            } catch (SQLException e) {
               e.printStackTrace();
           }
           return null;
        }
        @Override
        protected void onPostExecute(Void result){
            // TODO Auto-generated method stub
            //alertDialog = new AlertDialog.Builder(context).create();
            //alertDialog.setTitle("Login Status");
            id.setText(theId);
            name.setText(theName);
            quantity.setText(theQuantity);
            super.onPostExecute(result);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
