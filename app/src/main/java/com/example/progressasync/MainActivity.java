package com.example.progressasync;

import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    private ProgressBar progressBar;
    private TextView statusTextView;
    private Button startButton, cancelButton;
    private MyAsyncTask myAsyncTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progressBar = findViewById(R.id.progressBar);
        statusTextView = findViewById(R.id.statusTextView);
        startButton = findViewById(R.id.startButton);
        cancelButton = findViewById(R.id.cancelButton);

        progressBar.setMax(100);
        statusTextView.setText("Status: Pending");

        startButton.setOnClickListener(v -> {
            if (myAsyncTask == null || myAsyncTask.getStatus() == AsyncTask.Status.FINISHED) {
                myAsyncTask = new MyAsyncTask();
                myAsyncTask.execute(100);
            }
        });

        cancelButton.setOnClickListener(v -> {
            if (myAsyncTask != null && myAsyncTask.getStatus() == AsyncTask.Status.RUNNING) {
                myAsyncTask.cancel(true);
            }
        });
    }


    private class MyAsyncTask extends AsyncTask<Integer, Integer, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            statusTextView.setText("Status: Running");
            progressBar.setProgress(0);
        }

        @Override
        protected Void doInBackground(Integer... params) {
            int iterations = params[0];
            for (int i = 0; i < iterations; i++) {
                if (isCancelled()) break;


                try {
                    Thread.sleep(300);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }


                publishProgress((i + 1) * 100 / iterations);
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            int progress = values[0];
            progressBar.setProgress(progress);
            statusTextView.setText("Progress: " + progress + "%");
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            statusTextView.setText("Status: Finished");
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            statusTextView.setText("Status: Cancelled");
        }
    }
}