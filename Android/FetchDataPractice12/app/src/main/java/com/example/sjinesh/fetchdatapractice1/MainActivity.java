package com.example.sjinesh.fetchdatapractice1;

import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private TextView targetTextView;
    private ImageView targetImageView;
    private Drawable toPlaceInImageView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button sendRequestButton = (Button) findViewById(R.id.send_request);
        targetTextView = (TextView) findViewById(R.id.target_text);
        targetImageView = (ImageView) findViewById(R.id.target_image);

        if (sendRequestButton != null) {
            sendRequestButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new JsonTask().execute("http://jsonparsing.parseapp.com/jsonData/moviesDemoItem.txt");
                    toPlaceInImageView = LoadImageFromWebOperations("http://romancebandits.com/wp-content/uploads/2012/09/Great-job1.jpg");

                    targetImageView.setImageDrawable(toPlaceInImageView);
                }
            });
        }
    }

    public class JsonTask extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {
            //first need to set up an http url connection.
            HttpURLConnection connection = null;
            BufferedReader reader = null;

            try {
                URL url = new URL(params[0]);   //url we're trying to get data from
                connection = (HttpURLConnection) url.openConnection();  //opening initial connection
                connection.connect();   //attempting to connect

                InputStream stream = connection.getInputStream(); //this will return the stream of input we get
                //If you want to make it an well formatted JSON Json, do
                //Json stream = new JSONObject(new JSONTokener(connection.getInputStream());

                //need a parser to read the InputStream, or use JSON methods if using that
                reader = new BufferedReader(new InputStreamReader(stream));

                //To keep run time short and reduce big O, use string buffer for parsing.
                StringBuffer buffer = new StringBuffer();

                String line = "";
                while ((line = reader.readLine()) != null) {
                    buffer.append(line);
                }

                return buffer.toString();

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally { //need to close the connection and the reader
                if (connection != null) {
                    connection.disconnect();
                }
                try {
                    if (reader != null) {
                        reader.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            targetTextView.setText(result);
        }
    }

    public static Drawable LoadImageFromWebOperations(String url) {
        try {
            InputStream is = (InputStream) new URL(url).getContent();
            return Drawable.createFromStream(is, "targetImageView");
        } catch (Exception e) {
            return null;
        }
    }

}
