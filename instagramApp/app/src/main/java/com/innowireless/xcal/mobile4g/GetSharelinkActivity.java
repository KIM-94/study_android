package com.innowireless.xcal.mobile4g;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;

public class GetSharelinkActivity extends AppCompatActivity {
    EditText searchETV;
    Button searchDLbtn;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_sharelink);

        searchETV = (EditText)findViewById(R.id.searchETV);
        searchDLbtn = (Button)findViewById(R.id.searchDLbtn);

        searchDLbtn.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){

                if(searchETV.getText().toString().contains("instagram")){
                    Intent intent = new Intent(GetSharelinkActivity.this, SearchMediaActivity.class);
                    intent.putExtra("copylink",searchETV.getText().toString());
                    finish();
                    startActivity(intent);
                }
            }
        });
    }
}