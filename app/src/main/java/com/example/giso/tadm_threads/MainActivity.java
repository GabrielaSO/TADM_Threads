package com.example.giso.tadm_threads;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    Button button1;
    Button button2;
    Button button3;
    Button button4;

    ProgressBar progressBar1;
    ProgressBar progressBar2;

    TextView display, display2;
    EditText num;

    int i;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button3 = (Button) findViewById(R.id.button3);
        button4 = (Button) findViewById(R.id.button4);
        progressBar1 = (ProgressBar) findViewById(R.id.progressBar1);
        progressBar2 = (ProgressBar) findViewById(R.id.progressBar2);
        num =findViewById(R.id.num);
        display =findViewById(R.id.display);
        display2=findViewById(R.id.display2);

        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    hilos(Integer.parseInt(num.getText().toString()));

                }catch (Exception e){
                    Log.i("Error: ", e.getMessage(),e.getCause());
                }
            }
        });

        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    AsyncTarea asyncTarea = new AsyncTarea();
                    asyncTarea.execute(Integer.parseInt(num.getText().toString()));
                }catch (Exception e){
                    Log.i("Error: ", e.getMessage(),e.getCause());
                }
            }
        });
    } //End onCreate

    private void UnSegundo() {

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void hilos(final int n) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                i =0;
                progressBar1.setMax(n);
                progressBar1.setProgress(0);
                while(i<=n-1){
                    UnSegundo();
                    i++;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            progressBar1.setProgress(i);
                            display.setText("ProgressBar 1: "+i);
                        }
                    });
                }
            }
        }).start();
    }

    private class  AsyncTarea extends AsyncTask<Integer, Integer,Boolean> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Boolean doInBackground(Integer... params) {
            for (int i=1; i<=params[0]; i++){
                UnSegundo();
                publishProgress(i);
                if (isCancelled()){
                    break;
                }
            }
            return true;
        }
        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);

            //Actualizar la barra de progress
            progressBar2.setProgress(values[0].intValue()*10);
            display2.setText("ProgressBar 2: "+((values[0].intValue())));
        }
        @Override
        protected void onPostExecute(Boolean aVoid) {
            if (aVoid){
                Toast.makeText(getApplicationContext(),"AsyncTask terminó",Toast.LENGTH_SHORT).show();
            }
        }
        @Override
        protected void onCancelled() {
            super.onCancelled();
            Toast.makeText(getApplicationContext(),"O_O",Toast.LENGTH_SHORT).show();
        }
    }
}