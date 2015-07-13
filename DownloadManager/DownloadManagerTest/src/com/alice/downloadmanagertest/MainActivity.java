package com.alice.downloadmanagertest;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends Activity implements View.OnClickListener {

    private TextView txt1;
    private TextView txt2;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        txt1 = (TextView) findViewById(R.id.txt1);
        txt2 = (TextView) findViewById(R.id.txt2);
        txt1.setOnClickListener(this);
        txt2.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        Log.d("", "btnDownload");
        String url = null;
        switch (v.getId()){
            case R.id.txt1:
                url = txt1.getText().toString();
                break;
            case R.id.txt2:
                url = txt2.getText().toString();
                break;
        }
        Uri uri = Uri.parse(url);
        //使用隐式意图，Intent.ACTION_VIEW
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(uri);
//        intent.setDataAndType(uri, "http/*");
        startActivity(intent);
    }
}
