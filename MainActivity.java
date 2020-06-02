package com.example.myapplication;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


public class MainActivity extends AppCompatActivity {
    Button b1, b2;
    EditText user1;
    EditText pass1;
    String username;
    String password;

      public static final int CONNECTION_TIMEOUT=10000;
      public static final int READ_TIMEOUT=15000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        b1 = (Button) findViewById(R.id.bt3);
        b2 = (Button) findViewById(R.id.bt4);
        user1 = (EditText) findViewById(R.id.edt2);
        pass1 = (EditText) findViewById(R.id.edt3);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username = user1.getText().toString();
                password = pass1.getText().toString();
                if (username.equals("") || password.equals("")) {
                    Toast.makeText(MainActivity.this, "Username or password must be filled", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (username.length() <= 1 || password.length() <= 1) {
                    Toast.makeText(MainActivity.this, "Username or password length must be greater than one", Toast.LENGTH_LONG).show();
                    return;
                }
                // request authentication with remote server4
                //  String type="login";
                // AsyncDataClass asyncRequestObject = new AsyncDataClass(MainActivity.this);
                //  asyncRequestObject.execute(type,username,password);
                BackgroundWorker backgroundWorker = new BackgroundWorker();
                backgroundWorker.execute(username, password);
            }
        });

        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this, ThirdActivity.class);
                startActivity(intent);
            }
        });
    }


    class BackgroundWorker extends AsyncTask<String ,String ,String> {

        ProgressDialog pdLoading= new ProgressDialog(MainActivity.this);
        HttpURLConnection http;
        URL url;

        @Override
        protected String doInBackground(String... params)
        {
            try {

                String login_url= "http://localhost/project/regist.php";
                url = new URL(login_url);

            } catch (MalformedURLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                return "exception";
            }
          //  String login_url= "http://192.168.43.58/project/regist.php";

                try {
                  //  String user_name = params[0];
                 //   String password = params[1];
                   //  url = new URL(login_url);
                    http = (HttpURLConnection) url.openConnection();
                    http.setReadTimeout(READ_TIMEOUT);
                    http.setConnectTimeout(CONNECTION_TIMEOUT);
                    http.setRequestMethod("POST");
                    http.setDoInput(true);
                    http.setDoOutput(true);
                    OutputStream ops = http.getOutputStream();
                    BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(ops, "UTF-8"));
                   /* String post_data = URLEncoder.encode("user_name", "UTF-8") + "=" + URLEncoder.encode(user_name, "UTF-8")
                            + "&&" + URLEncoder.encode("password", "UTF-8") + "=" + URLEncoder.encode(password, "UTF-8");*/
                    Uri.Builder builder = new Uri.Builder()
                            .appendQueryParameter("user_name", params[0])
                            .appendQueryParameter("password", params[1]);
                    String query = builder.build().getEncodedQuery();

                    bufferedWriter.write(query);
                    bufferedWriter.flush();
                    bufferedWriter.close();
                    ops.close();
                    http.connect();
                } catch (IOException e1) {

                    e1.printStackTrace();
                    return "exception";
                }

                try
                {


                        int response_code = http.getResponseCode();
                        if (response_code == HttpURLConnection.HTTP_OK) {
                            InputStream inputStream = http.getInputStream();
                            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));
                           // String result = "";
                            StringBuilder result = new StringBuilder();
                            String line = "";
                            while ((line = bufferedReader.readLine()) != null) {
                                result.append(line);

                            }

                          //  bufferedReader.close();
                          //  inputStream.close();
                          //  http.disconnect();
                            return (result.toString());
                        }
                        else{

                            return("unsuccessful");
                        }

                }  catch (IOException e) {
                    e.printStackTrace();
                    return "exception";
                }
                finally {
                    http.disconnect();
                }
             //return null;
        }
        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();

            //this method will be running on UI thread
            pdLoading.setMessage("\tLoading...");
            pdLoading.setCancelable(false);
            pdLoading.show();

        }
        @Override
        protected void onPostExecute(String result)
        {
            pdLoading.dismiss();

            if(result.equalsIgnoreCase("true"))
            {

                Intent intent = new Intent(MainActivity.this,SecondActivity.class);
                startActivity(intent);
                MainActivity.this.finish();

            }else if (result.equalsIgnoreCase("false")){

                // If username and password does not match display a error message
                Toast.makeText(MainActivity.this, "Invalid email or password", Toast.LENGTH_LONG).show();

            } else if (result.equalsIgnoreCase("exception") || result.equalsIgnoreCase("unsuccessful")) {

                Toast.makeText(MainActivity.this, "OOPs! Something went wrong. Connection Problem.", Toast.LENGTH_LONG).show();

            }
        }

    }
}






