package jaquo.subtitlesinreallife;

import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

public class AudienceText extends AppCompatActivity {
    private RequestQueue queue;
    private static final String TAG = "AudienceText";
    private static TextView mTextView;
    private static String theResponse;
    private String roomID = "";
    Handler timerHandler = new Handler();
    Runnable timerRunnable = new Runnable() {
        @Override
        public void run() {
            getTranscript();
            timerHandler.postDelayed(this, 1000);
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audience_text);
        roomID = getIntent().getStringExtra("roomID");
        mTextView = (TextView) findViewById(R.id.transcript);
        mTextView.setMovementMethod(new ScrollingMovementMethod());
        getTranscript();
        mTextView.setText(theResponse);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getTranscript();
            }
        });
        timerHandler.postDelayed(timerRunnable, 0);
    }

    public void getTranscript() {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "https://transcribe.space/transcripts/"+roomID+".txt";
        // Request a string response from the provided URL.
        Log.i(TAG, "url=" + url);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        theResponse = response;
                        Log.i(TAG, "theResponse = " + response);
                        mTextView.setText(theResponse);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
        queue.add(stringRequest);   //...dunno. Just works.
        queue.start();  //...dunno.
    }
    protected void onPause() {
        super.onPause();
    }
    protected void onStop()    {
        super.onStop();
        timerHandler.removeCallbacks(timerRunnable);
        finish();
    }
    protected void onDestroy()  {
        super.onDestroy();
    }
}
